package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PolicyRepository extends JpaRepository<PolicyEntity, Long> {

    @Query(value = "SELECT * FROM policy where memberId = ?", nativeQuery = true)
    Optional<PolicyEntity> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}
