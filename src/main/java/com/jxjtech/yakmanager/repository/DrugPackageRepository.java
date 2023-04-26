package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.DrugPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrugPackageRepository extends JpaRepository<DrugPackageEntity, Long> {
    List<DrugPackageEntity> findAllByDrugCode(Integer drugCode);
}
