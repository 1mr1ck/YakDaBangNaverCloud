package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByMemberId(Long memberId);

    Optional<MemberEntity> findByMemberEmail(String email);

    boolean existsByMemberEmail(String email);

    boolean existsByMemberNickName(String nickName);
}
