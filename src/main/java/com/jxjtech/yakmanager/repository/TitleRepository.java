package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.TitleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TitleRepository extends JpaRepository<TitleEntity, Long> {
    @Query(value = "SELECT * FROM title WHERE pharmacyId = ?", nativeQuery = true)
    List<TitleEntity> findAllByPharmacyId(Long pharmacyId);

    void deleteAllByPharmacyId(Long pharmacyId);

    @Query(value = "SELECT titleId FROM title WHERE pharmacyId = ?", nativeQuery = true)
    List<Long> findAllTitleIdByPharmacyId(Long pharmacyId);
}
