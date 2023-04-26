package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.PharmacyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PharmacyRepository extends JpaRepository<PharmacyEntity, Long> {


    @Query(value = "select * from pharmacy where pharmacyId = (SELECT MAX(pharmacyId) from pharmacy where memberId = ?)", nativeQuery = true)
    PharmacyEntity findByMemberIdMax(Long memberId);
}
