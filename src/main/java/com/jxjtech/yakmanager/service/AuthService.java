package com.jxjtech.yakmanager.service;

import com.jxjtech.yakmanager.dto.*;
import com.jxjtech.yakmanager.entity.MemberEntity;
import com.jxjtech.yakmanager.entity.PolicyEntity;
import com.jxjtech.yakmanager.entity.RefreshTokenEntity;
import com.jxjtech.yakmanager.exception.AppException;
import com.jxjtech.yakmanager.exception.ErrorCode;
import com.jxjtech.yakmanager.jwt.TokenProvider;
import com.jxjtech.yakmanager.repository.MemberRepository;
import com.jxjtech.yakmanager.repository.PolicyRepository;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PolicyRepository policyRepository;

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
    public boolean isDuplicateNickName(IsDuplicateNickNameDTO isDuplicateNickNameDTO) {
        // 존재하면 true // 존재하지 않으면 false
        return memberRepository.existsByMemberNickName(isDuplicateNickNameDTO.getMemberNickName());
    }

    /**
     * 이메일 중복체크
     * 중복 : true / 중복x : false
     */
    public boolean isDuplicateEmail(IsDuplicateEmailDTO isDuplicateEmailDTO) {
        // 존재하면 true // 존재하지 않으면 false
        return memberRepository.existsByMemberEmail(isDuplicateEmailDTO.getMemberEmail());
    }

    /**
     * 로그인
     */
    @Transactional
    public LoginResponseDTO login(String snsType, LoginRequestDTO loginRequestDTO) {
        log.info("login");
        String memberEmail = loginRequestDTO.getMemberEmail();
        String timeZone = loginRequestDTO.getTimeZone();

        if (!memberRepository.existsByMemberEmail(memberEmail)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        MemberEntity member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA));
        if (!member.getSnsType().equals(snsType)) {
            throw new AppException(ErrorCode.NOT_EQUAL_SNS_TYPE);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getMemberId(), null);
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        if (authentication != null) {
            TokenDTO tokenDTO = tokenProvider.generateTokenDto(authentication, member.getMemberId());

            LoginResponseDTO result = new LoginResponseDTO(tokenDTO);
            // 로그인 성공하니까 action++
            member = MemberEntity.actionUp(member, timeZone);

            // 리프레쉬토큰 DB 저장
            RefreshTokenEntity refreshTokenEntity;
            if (refreshTokenRepository.findByMemberId(member.getMemberId()).isPresent()) {
                refreshTokenEntity = refreshTokenRepository.findByMemberId(member.getMemberId()).get();
                refreshTokenEntity.setRefreshToken(tokenDTO.getRefreshToken());
            } else {
                refreshTokenEntity = RefreshTokenEntity.of(tokenDTO);
                refreshTokenRepository.save(refreshTokenEntity);
            }

            return result;
        }
        return new LoginResponseDTO();
    }

    /**
     * 개인정보정책 확인
     */
    public boolean policyCheck(JWTRequestDTO dto) {
        String refreshToken = cutBearer(dto.getRefreshToken());
        if (tokenProvider.certifyRefreshToken(refreshToken)) {
            Long memberId = Long.valueOf(tokenProvider.parseClaims(refreshToken).getSubject());
            return policyRepository.existsByMemberId(memberId);
        } else {
            return false;
        }
    }

    /**
     * 개인정보정책 등록
     */
    public ResultDTO policyRegister(PolicyRegisterDTO policyRegisterDTO) {
        String refreshToken = cutBearer(policyRegisterDTO.getRefreshToken());
        if (tokenProvider.certifyRefreshToken(refreshToken)) {
            Long memberId = Long.valueOf(tokenProvider.parseClaims(refreshToken).getSubject());
            PolicyEntity policyEntity = PolicyRegisterDTO.toPolicy(policyRegisterDTO, memberId);
            policyRepository.save(policyEntity);

            return new ResultDTO("정책 등록완료");
        }

        return new ResultDTO("정책 등록실패");
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
}
