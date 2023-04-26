package com.jxjtech.yakmanager.jwt;

import com.jxjtech.yakmanager.dto.TokenDTO;
import com.jxjtech.yakmanager.entity.MemberEntity;
import com.jxjtech.yakmanager.entity.RefreshTokenEntity;
import com.jxjtech.yakmanager.repository.MemberRepository;
import com.jxjtech.yakmanager.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         MemberRepository memberRepository,
                         RefreshTokenRepository refreshTokenRepository) {
        log.info("TokenProvider");
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     *  로그인 -> 토큰 생성
     */
    public TokenDTO generateTokenDto(Authentication authentication, Long memberId) {
        log.info("generateTokenDto");

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
                signWith(key, SignatureAlgorithm.HS256).
                compact();

        String refreshToken =
                Jwts.builder().
                setHeaderParam(Header.TYPE, Header.JWT_TYPE).
                setSubject(authentication.getName()).
                claim(REFRESH_KEY, authorities).
                setIssuedAt(new Date(System.currentTimeMillis())). // 토큰발행날짜
                setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME)).
                signWith(key, SignatureAlgorithm.HS256).
                compact();

        Long accessTokenExpire = Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(accessToken).getBody().getExpiration().getTime();
        Long refreshTokenExpire = Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(refreshToken).getBody().getExpiration().getTime();

        return TokenDTO.builder().result(true).Authorization(BEARER_TYPE + accessToken).Authorization_Exp(accessTokenExpire).RefreshToken(BEARER_TYPE + refreshToken).RefreshToken_Exp(refreshTokenExpire).memberId(memberId.toString()).build();
    }

    /**
     * 토큰 인증
     * -> JwtFilter
     */
    public Authentication getAuthentication(String accessToken) {
        log.info("getAuthentication");

        Claims claims = parseClaims(accessToken);

        if (claims.get(REFRESH_KEY) == null) {
            if (claims.get(AUTHORITIES_KEY) == null) {
                throw new RuntimeException("권한 정보가 없는 토큰입니다.");
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
        log.info("validateToken");
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            throw new MalformedJwtException("구조적인 문제가 있는 JWT인 경우");
            return false;
        } catch (UnsupportedJwtException e) {
//            throw new UnsupportedJwtException("수신한 JWT의 형식이 애플리케이션에서 원하는 형식과 맞지 않은 경우 예를 들면, 암호화된 JWT를 사용하는 애플리케이션에 암호화되지 않은 JWT가 전달되는 경우에 이예외 발생");
            return false;
        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("잘못된 토큰");
            return false;
        }
    }

    /**
     *  자동로그인 액세스토큰 유효성 검사
     */
    public boolean validateAccessToken(String token) {
        log.info("validateToken");
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new MalformedJwtException("구조적인 문제가 있는 JWT인 경우");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("수신한 JWT의 형식이 애플리케이션에서 원하는 형식과 맞지 않은 경우 예를 들면, 암호화된 JWT를 사용하는 애플리케이션에 암호화되지 않은 JWT가 전달되는 경우에 이예외 발생");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 토큰");
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException: 유효 기간이 지난 액세스토큰을 수신한 경우");
            return false;
        }
    }

    /**
     *  토큰재발급 -> 액세스토큰 검사
     */
    public boolean certifyAccessToken(String token) {
        log.info("certifyAccessToken");
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return false;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new MalformedJwtException("구조적인 문제가 있는 JWT인 경우");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("수신한 JWT의 형식이 애플리케이션에서 원하는 형식과 맞지 않은 경우 예를 들면, 암호화된 JWT를 사용하는 애플리케이션에 암호화되지 않은 JWT가 전달되는 경우에 이예외 발생");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 토큰");
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException: 유효 기간이 지난 액세스토큰을 수신한 경우");
            return true;
        }
    }

    /**
     *  토큰재발급 -> 리프레쉬토큰 검사
     */
    public boolean certifyRefreshToken(String token) {
        log.info("certifyRefreshToken");
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            log.info("정상 : " + token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new MalformedJwtException("구조적인 문제가 있는 JWT인 경우");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("수신한 JWT의 형식이 애플리케이션에서 원하는 형식과 맞지 않은 경우 예를 들면, 암호화된 JWT를 사용하는 애플리케이션에 암호화되지 않은 JWT가 전달되는 경우에 이예외 발생");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 토큰");
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("RefreshToken Expired");
        }
    }

    /**
     *  액세스, 리프레쉬 검사 후
     *  true -> 토큰 재발급 시작
     */
    public Map<String, String> ReIssueToken(String refreshToken) {
        log.info("ReIssueToken");

        try {
            Claims claimsJws2 = Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(refreshToken).getBody();

            Map<String, String> reIssuedToken = new LinkedHashMap<>();
            log.info("refresh 기간 : " + claimsJws2.getExpiration() + "\n현재시간 : " + new Date());
            if (claimsJws2.getExpiration().getTime() - new Date().getTime() < 1000 * 60 * 60 * 24 * 7L) {
                reIssuedToken.put("accessToken", BEARER_TYPE + recreationAccessToken(claimsJws2.get("sub").toString(), claimsJws2.get("refreshTokenKey")));
                reIssuedToken.put("refreshToken", BEARER_TYPE + recreationRefreshToken(claimsJws2.get("sub").toString(), claimsJws2.get("refreshTokenKey")));
                String refreshTokens = reIssuedToken.get("refreshToken");
                Long refreshTokenExpire = Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(refreshTokens).getBody().getExpiration().getTime(); // long 타입
                reIssuedToken.put("refreshTokenExpiresIn", String.valueOf(refreshTokenExpire));
            } else {
                reIssuedToken.put("accessToken", BEARER_TYPE + recreationAccessToken(claimsJws2.get("sub").toString(), claimsJws2.get("refreshTokenKey")));
            }
            String accessToken = reIssuedToken.get("accessToken").substring(7);
            Long accessTokenExpire = Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(accessToken).getBody().getExpiration().getTime(); // long 타입
            reIssuedToken.put("accessTokenExpiresIn", String.valueOf(accessTokenExpire));
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
        log.info("recreationAccessToken");

        String accessToken =
                Jwts.builder().
                setHeaderParam(Header.TYPE, Header.JWT_TYPE).
                setSubject(memberId).
                claim(AUTHORITIES_KEY, roles).
                setIssuedAt(new Date(System.currentTimeMillis())). // 토큰발행날짜
                setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME)).
                signWith(key, SignatureAlgorithm.HS256).
                compact();

        return accessToken;
    }

    /**
     *  액세스 토큰 재발급
     *  -> ReIssueToken
     */
    public String recreationRefreshToken(String memberId, Object roles) {
        log.info("recreationRefreshToken");

        String refreshToken =
                Jwts.builder().
                setHeaderParam(Header.TYPE, Header.JWT_TYPE).
                setSubject(memberId).
                claim(REFRESH_KEY, roles).
                setIssuedAt(new Date(System.currentTimeMillis())). // 토큰발행날짜
                setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME)).
                signWith(key, SignatureAlgorithm.HS256).
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
    public Claims parseClaims(String accessToken) {
        log.info("parseClaims");
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
