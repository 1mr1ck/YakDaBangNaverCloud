package com.jxjtech.yakmanager.jwt;

import com.jxjtech.yakmanager.dto.TokenDTO;
import com.jxjtech.yakmanager.entity.MemberEntity;
import com.jxjtech.yakmanager.entity.RefreshTokenEntity;
import com.jxjtech.yakmanager.exception.AppException;
import com.jxjtech.yakmanager.exception.ErrorCode;
import com.jxjtech.yakmanager.repository.MemberRepository;
import com.jxjtech.yakmanager.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private static final String AUTHORITIES_KEY = "authoritiesKey";
    private static final String REFRESH_KEY = "refreshTokenKey";
    private static final String BEARER_TYPE = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;  //30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14L;  // 2주
    private final Key accessKey;
    private final Key refreshKey;

    public TokenProvider(@Value("${jwt.accessSecret}") String accessSecret, @Value("${jwt.refreshSecret}") String refreshSecret,
                         MemberRepository memberRepository,
                         RefreshTokenRepository refreshTokenRepository) {

        byte[] accessKeyBytes = Decoders.BASE64.decode(accessSecret);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecret);
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     *  로그인 -> 토큰 생성
     */
    public TokenDTO generateTokenDto(Authentication authentication, Long memberId) {

        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        MemberEntity entity = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        String accessToken =
                Jwts.builder().
                setHeaderParam(Header.TYPE, Header.JWT_TYPE).
                setSubject(authentication.getName()).
                claim(AUTHORITIES_KEY, authorities).
                claim("email", entity.getMemberEmail()).
                claim("socialType", entity.getSnsType()).
                claim("nickName", entity.getMemberNickName()).
                setIssuedAt(new Date(System.currentTimeMillis())). // 토큰발행날짜
                setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME)).
                signWith(accessKey, SignatureAlgorithm.HS256).
                compact();

        String refreshToken =
                Jwts.builder().
                setHeaderParam(Header.TYPE, Header.JWT_TYPE).
                setSubject(authentication.getName()).
                claim(REFRESH_KEY, authorities).
                setIssuedAt(new Date(System.currentTimeMillis())). // 토큰발행날짜
                setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME)).
                signWith(refreshKey, SignatureAlgorithm.HS256).
                compact();

        Long accessTokenExpire = Jwts.parserBuilder().setSigningKey(this.accessKey).build().parseClaimsJws(accessToken).getBody().getExpiration().getTime();
        Long refreshTokenExpire = Jwts.parserBuilder().setSigningKey(this.refreshKey).build().parseClaimsJws(refreshToken).getBody().getExpiration().getTime();

        return TokenDTO.builder().result(true).Authorization(BEARER_TYPE + accessToken).Authorization_Exp(accessTokenExpire).RefreshToken(BEARER_TYPE + refreshToken).RefreshToken_Exp(refreshTokenExpire).memberId(memberId.toString()).build();
    }

    /**
     * 토큰 인증
     * -> JwtFilter
     */
    public Authentication getAuthentication(String accessToken) {

        Claims claims = accessParseClaims(accessToken);

        if (claims.get(REFRESH_KEY) == null) {
            if (claims.get(AUTHORITIES_KEY) == null) {
                throw new JwtException("잘못된 토큰입니다.", new AppException(ErrorCode.INVALID_JWT_TOKEN));
            }
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     *  JwtFilter를 거칠 때,
     *  Header로 준 accesstoken 유효성 검사
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     *  토큰재발급 -> 리프레쉬토큰 검사
     */
    public boolean certifyRefreshToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(token);
            log.info("정상 : " + token);
            return true;
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException | IllegalArgumentException |
                 UnsupportedJwtException e) {
            throw new JwtException("잘못된 토큰입니다.", new AppException(ErrorCode.INVALID_JWT_TOKEN));
        }
    }

    /**
     *  액세스, 리프레쉬 검사 후
     *  true -> 토큰 재발급 시작
     */
    public Map<String, String> ReIssueToken(String refreshToken) {
        try {
            Claims claimsJws2 = Jwts.parserBuilder().setSigningKey(this.refreshKey).build().parseClaimsJws(refreshToken).getBody();

            Map<String, String> reIssuedToken = new LinkedHashMap<>();
            log.info("refresh 기간 : " + claimsJws2.getExpiration() + "\n현재시간 : " + new Date());
            reIssuedToken.put("Authorization", BEARER_TYPE + recreationAccessToken(claimsJws2.get("sub").toString(), claimsJws2.get("refreshTokenKey")));
            String accessToken = reIssuedToken.get("Authorization").substring(7);
            Long accessTokenExpire = Jwts.parserBuilder().setSigningKey(this.accessKey).build().parseClaimsJws(accessToken).getBody().getExpiration().getTime(); // long 타입
            reIssuedToken.put("Authorization_Exp", String.valueOf(accessTokenExpire));
            if (claimsJws2.getExpiration().getTime() - new Date().getTime() < 1000 * 60 * 60 * 24 * 7L) {
                reIssuedToken.put("RefreshToken", BEARER_TYPE + recreationRefreshToken(claimsJws2.get("sub").toString(), claimsJws2.get("refreshTokenKey")));
                String refreshTokens = reIssuedToken.get("RefreshToken");
                Long refreshTokenExpire = Jwts.parserBuilder().setSigningKey(this.refreshKey).build().parseClaimsJws(refreshTokens).getBody().getExpiration().getTime(); // long 타입
                reIssuedToken.put("RefreshToken_Exp", String.valueOf(refreshTokenExpire));
            } else {
                reIssuedToken.put("RefreshToken", null);
                reIssuedToken.put("RefreshToken_Exp", null);
            }
            reIssuedToken.put("memberKey", claimsJws2.get("sub").toString());

            return reIssuedToken;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     *  액세스 토큰 재발급
     *  -> ReIssueToken
     */
    public String recreationAccessToken(String memberId, Object roles) {
        MemberEntity entity = memberRepository.findById(Long.valueOf(memberId)).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        String accessToken =
                Jwts.builder().
                        setHeaderParam(Header.TYPE, Header.JWT_TYPE).
                        setSubject(memberId).
                        claim(AUTHORITIES_KEY, roles).
                        claim("email", entity.getMemberEmail()).
                        claim("socialType", entity.getSnsType()).
                        claim("nickName", entity.getMemberNickName()).
                        setIssuedAt(new Date(System.currentTimeMillis())). // 토큰발행날짜
                        setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME)).
                        signWith(accessKey, SignatureAlgorithm.HS256).
                        compact();

        return accessToken;
    }

    /**
     *  액세스 토큰 재발급
     *  -> ReIssueToken
     */
    public String recreationRefreshToken(String memberId, Object roles) {
        String refreshToken =
                Jwts.builder().
                setHeaderParam(Header.TYPE, Header.JWT_TYPE).
                setSubject(memberId).
                claim(REFRESH_KEY, roles).
                setIssuedAt(new Date(System.currentTimeMillis())). // 토큰발행날짜
                setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME)).
                signWith(refreshKey, SignatureAlgorithm.HS256).
                compact();

        MemberEntity member = memberRepository.findByMemberId(Long.valueOf(memberId));

        if(refreshTokenRepository.findByMemberId(member.getMemberId()).isPresent()) {
            RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByMemberId(member.getMemberId()).get();
            refreshTokenEntity.setRefreshToken(refreshToken);
            refreshTokenRepository.save(refreshTokenEntity);
        }

        memberRepository.save(member);

        return refreshToken;
    }

    /**
     * 토큰복호화
     * -> getAuthentication
     */
    public Claims accessParseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(accessToken).getBody();
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException | IllegalArgumentException |
                 UnsupportedJwtException e) {
            throw new JwtException("잘못된 토큰입니다.", new AppException(ErrorCode.INVALID_JWT_TOKEN));
        }
    }
    public Claims refreshParseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(accessToken).getBody();
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException | IllegalArgumentException |
                 UnsupportedJwtException e) {
            throw new JwtException("잘못된 토큰입니다.", new AppException(ErrorCode.INVALID_JWT_TOKEN));
        }
    }
}
