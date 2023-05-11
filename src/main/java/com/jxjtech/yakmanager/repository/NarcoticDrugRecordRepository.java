package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.NarcoticDrugRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NarcoticDrugRecordRepository extends JpaRepository<NarcoticDrugRecordEntity, Long> {

    @Query(value = "SELECT * FROM narcoticdrugrecord where narcotictitleId = ?", nativeQuery = true)
    List<NarcoticDrugRecordEntity> findAllByTitleId(Long titleId);
}
