package com.jxjtech.yakmanager.service;

import com.jxjtech.yakmanager.dto.MemberDTO;
import com.jxjtech.yakmanager.dto.MemberUpdateDTO;
import com.jxjtech.yakmanager.dto.ResultDTO;
import com.jxjtech.yakmanager.entity.MemberEntity;
import com.jxjtech.yakmanager.exception.AppException;
import com.jxjtech.yakmanager.exception.ErrorCode;
import com.jxjtech.yakmanager.repository.MemberRepository;
import com.jxjtech.yakmanager.repository.PolicyRepository;
import com.jxjtech.yakmanager.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PolicyRepository policyRepository;

    public Long getMemberId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Transactional
    public ResultDTO update(MemberUpdateDTO memberUpdateDTO) {
        MemberEntity member = memberRepository.findById(getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        member.setMemberNickName(memberUpdateDTO.getMemberNickName());

        return new ResultDTO("닉네임 변경 완료");
    }

    @Transactional
    public ResultDTO delete() {
        memberRepository.deleteById(getMemberId());
        refreshTokenRepository.deleteByMemberId(getMemberId());

        if(policyRepository.findByMemberId(getMemberId()).isPresent()) {
            policyRepository.delete(policyRepository.findByMemberId(getMemberId()).get());
        }

        return new ResultDTO("회원탈퇴 완료");
    }

    public MemberDTO getInfo() {
        MemberEntity member = memberRepository.findById(getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return new MemberDTO(member);
    }
}
