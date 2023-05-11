package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.DrugPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DrugPackageRepository extends JpaRepository<DrugPackageEntity, Long> {
    List<DrugPackageEntity> findAllByDrugCode(Integer drugCode);

    @Query(value = "SELECT * FROM drugpackage where standardCode = ?", nativeQuery = true)
    Optional<DrugPackageEntity> findByStandardCode(Long standardCode);
}
