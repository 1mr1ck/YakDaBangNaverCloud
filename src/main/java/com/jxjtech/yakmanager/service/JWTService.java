package com.jxjtech.yakmanager.service;

import com.jxjtech.yakmanager.dto.AuthCheckResponseDTO;
import com.jxjtech.yakmanager.entity.RecentLoginEntity;
import com.jxjtech.yakmanager.entity.RefreshTokenEntity;
import com.jxjtech.yakmanager.exception.AppException;
import com.jxjtech.yakmanager.exception.ErrorCode;
import com.jxjtech.yakmanager.jwt.TokenProvider;
import com.jxjtech.yakmanager.repository.RecentLoginRepository;
import com.jxjtech.yakmanager.repository.RefreshTokenRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final RecentLoginRepository recentLoginRepository;

    /**
     * 액세스토큰 재발급
     */
    @Transactional
    public Map<String, String> reIssue(String refreshToken) {
        Map<String, String> reIssueToken = new LinkedHashMap<>();
        refreshToken = cutBearer(refreshToken);

        if (tokenProvider.certifyRefreshToken(refreshToken)) {
            reIssueToken = tokenProvider.ReIssueToken(refreshToken);

            return reIssueToken;
        }
        return null;
    }

    /**
     * 자동로그인
     */
    @Transactional
    public AuthCheckResponseDTO rememberMe(String refreshToken, String phoneValue, String buildVersion, String snsType) {
        AuthCheckResponseDTO result = new AuthCheckResponseDTO();
        String cutRefreshToken = cutBearer(refreshToken);


        if (tokenProvider.certifyRefreshToken(cutRefreshToken)) {
            result.setResult(true);
            result.setMsg("valid token complete");

            RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA));

            if(!refreshTokenEntity.getRefreshToken().equals(refreshToken)) {
                throw new AppException(ErrorCode.FORBID_DUPLICATE_LOGIN);
            }

            if(recentLoginRepository.findByPhoneValue(phoneValue).isPresent()) {
                RecentLoginEntity recentLoginEntity = recentLoginRepository.findByPhoneValue(phoneValue).get();
                recentLoginEntity.update(recentLoginEntity, buildVersion, snsType);
            }
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
