package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.PharmacyMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PharmacyMemberRepository extends JpaRepository<PharmacyMemberEntity, Long> {

    @Query(value = "SELECT * FROM pharmacy_member WHERE memberId = ?", nativeQuery = true)
    List<PharmacyMemberEntity> findByMemberId(Long memberId);

    @Query(value = "SELECT * FROM pharmacy_member WHERE pharmacyId = ?", nativeQuery = true)
    List<PharmacyMemberEntity> findAllByPharmacyId(Long pharmacyId);

    @Query(value = "SELECT * FROM pharmacy_member WHERE pharmacyId = ? and memberId = ?", nativeQuery = true)
    Optional<PharmacyMemberEntity> findByPharmacyId(Long pharmacyId, Long memberId);
}
