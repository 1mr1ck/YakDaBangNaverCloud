package com.jxjtech.yakmanager.service;

import com.jxjtech.yakmanager.dto.AuthCheckResponseDTO;
import com.jxjtech.yakmanager.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class JWTService {

    private final TokenProvider tokenProvider;

    /**
     * 액세스토큰 재발급
     */
    @Transactional
    public Map<String, String> reIssue(String refreshToken) {
        Map<String, String> reIssueToken = new LinkedHashMap<>();
        refreshToken = cutBearer(refreshToken);

        if (tokenProvider.certifyRefreshToken(refreshToken))
        {
            reIssueToken = tokenProvider.ReIssueToken(refreshToken);

            return reIssueToken;
        }
        return null;
    }

    /**
     * 자동로그인
     */
    @Transactional
    public AuthCheckResponseDTO rememberMe(String refreshToken) {
        AuthCheckResponseDTO result = new AuthCheckResponseDTO();
        refreshToken = cutBearer(refreshToken);

        if(tokenProvider.certifyRefreshToken(refreshToken)) {
                result.setResult(true);
                result.setMsg("valid token complete");

                return result;
        }
        result.setResult(false);
        result.setMsg("Invalid token");
        return result;
    }

    /**
     * Bearer token-----
     * 토큰의 Bearer 짤라주는 메소드
     */

    private String cutBearer(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
