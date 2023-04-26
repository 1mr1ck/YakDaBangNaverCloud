package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.Drug_info2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface Drug_info2Repository extends JpaRepository<Drug_info2Entity, Long> {

    @Query(value = "SELECT * FROM drug_info2 WHERE drug_code = ?", nativeQuery = true)
    Optional<Drug_info2Entity> findByDrug_code(int drugCode);
}
