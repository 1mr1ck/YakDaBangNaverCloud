package com.jxjtech.yakmanager.service;

import com.jxjtech.yakmanager.dto.RememberMeRequestDTO;
import com.jxjtech.yakmanager.dto.TokenDTO;
import com.jxjtech.yakmanager.dto.TokenReIssueRequestDTO;
import com.jxjtech.yakmanager.entity.MemberEntity;
import com.jxjtech.yakmanager.jwt.TokenProvider;
import com.jxjtech.yakmanager.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class JWTService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    /**
     * 액세스토큰 재발급
     */
    @Transactional
    public TokenDTO reIssue(TokenReIssueRequestDTO dto) {
        TokenDTO result = new TokenDTO();
        String accessToken = cutBearer(dto.getAuthorization());
        String refreshToken = cutBearer(dto.getRefreshToken());

        if (tokenProvider.certifyAccessToken(accessToken)
                && tokenProvider.certifyRefreshToken(refreshToken))
        {
            Map<String, String> reIssueToken = tokenProvider.ReIssueToken(refreshToken);

            result = TokenDTO.of(reIssueToken);
            result.setMemberId(tokenProvider.parseClaims(cutBearer(result.getAuthorization())).getSubject());
            return result;
        }
        result.setResult(false);
        return result;
    }

    /**
     * 자동로그인
     */
    @Transactional
    public TokenDTO rememberMe(RememberMeRequestDTO dto) {
        TokenDTO result;
        String accessToken = cutBearer(dto.getAuthorization());
        String refreshToken = cutBearer(dto.getRefreshToken());

        if(tokenProvider.certifyRefreshToken(refreshToken)) {
            if(tokenProvider.validateAccessToken(accessToken)) {
                return new TokenDTO(true);
            } else {
                Map<String, String> reIssueToken = tokenProvider.ReIssueToken(refreshToken);
                result = TokenDTO.of(reIssueToken);

                String memberId = tokenProvider.parseClaims(cutBearer(result.getAuthorization())).getSubject();
                result.setMemberId(memberId);

                MemberEntity member = memberRepository.findByMemberId(Long.valueOf(memberId));
                MemberEntity.actionUp(member, dto.getTimeZone());

                return result;
            }
        }

        return new TokenDTO(false);
    }

    /**
     * Bearer token-----
     * 토큰의 Bearer 짤라주는 메소드
     */

    private String cutBearer(String token) {
        log.info("resolveToken");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
