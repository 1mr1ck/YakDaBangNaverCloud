package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.DrugRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DrugRecordRepository extends JpaRepository<DrugRecordEntity, Long> {

    @Query(value = "SELECT * FROM drugrecord WHERE titleId = ? and drugCode = ?", nativeQuery = true)
    Optional<DrugRecordEntity> findByTitleIdAndDrugCode(Long titleId, int drugCode);

    @Query(value = "SELECT * FROM drugrecord WHERE titleId = ?", nativeQuery = true)
    List<DrugRecordEntity> findAllByTitleId(Long titleId);


    Optional<DrugRecordEntity> findByTitleId(Long titleId);
}
