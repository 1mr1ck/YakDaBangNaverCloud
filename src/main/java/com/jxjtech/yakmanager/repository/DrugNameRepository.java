package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.DrugNameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DrugNameRepository extends JpaRepository<DrugNameEntity, Long> {

    @Query(value = "SELECT drugCode FROM drugname where drugName = ?", nativeQuery = true)
    int getDrugCodeByDrugName(String drugName);

    @Query(value = "SELECT * FROM drugname2 WHERE drugName LIKE ?", nativeQuery = true)
    List<DrugNameEntity> findAllByDrugName(String name);
}
