package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.DrugPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DrugPriceRepository extends JpaRepository<DrugPriceEntity, Long> {

    @Query(value = "SELECT * FROM drugprice where drugName LIKE ? ORDER BY drugName", nativeQuery = true)
    List<DrugPriceEntity> findAllByDrugName(String drugName);

    @Query(value = "SELECT * FROM drugprice where drugCode LIKE ? ORDER BY drugName", nativeQuery = true)
    List<DrugPriceEntity> findAllByDrugCode(Integer drugCode);

    Optional<DrugPriceEntity> findByProductCode(Integer productCode);

    @Query(value = "SELECT * FROM drugprice where drugCode = ?", nativeQuery = true)
    DrugPriceEntity findByDrugCode(Integer drugCode);
}
