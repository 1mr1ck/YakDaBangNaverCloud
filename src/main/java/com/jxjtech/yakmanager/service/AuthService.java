package com.jxjtech.yakmanager.service;

import com.jxjtech.yakmanager.dto.*;
import com.jxjtech.yakmanager.entity.MemberEntity;
import com.jxjtech.yakmanager.entity.PolicyEntity;
import com.jxjtech.yakmanager.entity.RecentLoginEntity;
import com.jxjtech.yakmanager.entity.RefreshTokenEntity;
import com.jxjtech.yakmanager.exception.AppException;
import com.jxjtech.yakmanager.exception.ErrorCode;
import com.jxjtech.yakmanager.jwt.TokenProvider;
import com.jxjtech.yakmanager.repository.MemberRepository;
import com.jxjtech.yakmanager.repository.PolicyRepository;
import com.jxjtech.yakmanager.repository.RecentLoginRepository;
import com.jxjtech.yakmanager.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PolicyRepository policyRepository;
    private final RecentLoginRepository recentLoginRepository;

    /**
     * Access Token이 필요한 API에서
     * MemberId 추츨
     */
    public Long getMemberId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * 회원가입
     */
    public RegisterResponseDTO register(String snsType, RegisterRequestDTO registerRequestDTO) {

        RegisterResponseDTO result = new RegisterResponseDTO(registerRequestDTO, snsType);

        MemberDTO memberDTO = MemberDTO.of(result);

        MemberEntity member = new MemberEntity(memberDTO);
        memberRepository.save(member);

        return result;
    }

    /**
     * 닉네임 중복체크
     * 중복 : true / 중복x : false
     */
    public boolean isDuplicateNickName(IsDuplicateNickNameDTO dto) {
        String nickName = dto.getMemberNickName();
        // 존재하면 true // 존재하지 않으면 false
        return memberRepository.existsByMemberNickName(nickName);
    }

    /**
     * 이메일 중복체크
     * 중복 : true / 중복x : false
     */
    public boolean isDuplicateEmail(IsDuplicateEmailDTO isDuplicateEmailDTO) {
        // 존재하면 true // 존재하지 않으면 false
        return memberRepository.existsByMemberEmailAndSnsType(isDuplicateEmailDTO.getMemberEmail(), isDuplicateEmailDTO.getSnsType());
    }

    /**
     * 로그인
     */
    @Transactional
    public LoginResponseDTO login(String snsType, LoginRequestDTO loginRequestDTO, String osType, String phoneValue, String buildVersion) {
        log.info(loginRequestDTO.getMemberEmail());
        String memberEmail = loginRequestDTO.getMemberEmail();

        if (!memberRepository.existsByMemberEmailAndSnsType(memberEmail, snsType)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        MemberEntity member = memberRepository.findByMemberEmailAndSnsType(memberEmail, snsType)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA));
        if (!member.getSnsType().equals(snsType)) {
            throw new AppException(ErrorCode.NOT_EQUAL_SNS_TYPE);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getMemberId(), null);
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        if (authentication != null) {
            TokenDTO tokenDTO = tokenProvider.generateTokenDto(authentication, member);
            Map<String, String> token = new LinkedHashMap<>();
            token.put("Authorization", tokenDTO.getAuthorization());
            token.put("Authorization_Exp", String.valueOf(tokenDTO.getAuthorization_Exp()));
            token.put("RefreshToken", tokenDTO.getRefreshToken());
            token.put("RefreshToken_Exp", String.valueOf(tokenDTO.getRefreshToken_Exp()));

            LoginResponseDTO result = new LoginResponseDTO(token);
            // 로그인 성공하니까 action++
            member = MemberEntity.actionUp(member);

            // 리프레쉬토큰 DB 저장
            RefreshTokenEntity refreshTokenEntity;
            if (refreshTokenRepository.findByMemberId(member.getMemberId()).isPresent()) {
                refreshTokenEntity = refreshTokenRepository.findByMemberId(member.getMemberId()).get();
                refreshTokenEntity.setRefreshToken(tokenDTO.getRefreshToken());
            } else {
                refreshTokenEntity = RefreshTokenEntity.of(tokenDTO);
                refreshTokenRepository.save(refreshTokenEntity);
            }

            if(recentLoginRepository.findByPhoneValue(phoneValue).isPresent()) {
                RecentLoginEntity recentLoginEntity = recentLoginRepository.findByPhoneValue(phoneValue).get();
                recentLoginEntity.update(recentLoginEntity, buildVersion, snsType);
            } else {
                recentLoginRepository.save(new RecentLoginEntity(phoneValue, osType, snsType, buildVersion));
            }

            return result;
        }
        return new LoginResponseDTO();
    }

    /**
     * 개인정보정책 확인
     */
    public AuthCheckResponseDTO policyCheck(String refreshToken) {
        refreshToken = cutBearer(refreshToken);
        AuthCheckResponseDTO result = new AuthCheckResponseDTO();
        if (tokenProvider.certifyRefreshToken(refreshToken)) {
            Long memberId = Long.valueOf(tokenProvider.refreshParseClaims(refreshToken).getSubject());
            if (policyRepository.existsByMemberId(memberId)) {
                result.setResult(true);
                result.setMsg("POLICY_IS_EXIST");
            }
        } else {
            result.setResult(false);
            result.setMsg("POLICY_IS_NOT_EXIST");
        }

        return result;
    }

    /**
     * 개인정보정책 등록
     */
    public AuthCheckResponseDTO policyRegister(PolicyRegisterDTO policyRegisterDTO, String refreshToken) {
        AuthCheckResponseDTO result = new AuthCheckResponseDTO();
        refreshToken = cutBearer(refreshToken);
        if (tokenProvider.certifyRefreshToken(refreshToken)) {
            Long memberId = Long.valueOf(tokenProvider.refreshParseClaims(refreshToken).getSubject());
            PolicyEntity policyEntity = PolicyRegisterDTO.toPolicy(policyRegisterDTO, memberId);
            policyRepository.save(policyEntity);

            result.setResult(true);
            result.setMsg("POLICY_REGISTER_SUCCESS");
        } else {
            result.setResult(false);
            result.setMsg("POLICY_REGISTER_FAILED");
        }

        return result;
    }

    /**
     * Bearer token-----
     * 토큰의 Bearer 짤라주는 메소드
     */
    private String cutBearer(String token) {
        log.info("cutBearer");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public RecentLoginResponseDTO recentLogin(String phoneValue) {
        boolean check = false;
        Optional<RecentLoginEntity> entity = recentLoginRepository.findByPhoneValue(phoneValue);
        return entity.map(RecentLoginResponseDTO::new).orElseGet(() -> new RecentLoginResponseDTO(check));
    }
}
