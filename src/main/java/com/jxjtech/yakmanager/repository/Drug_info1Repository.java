package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.Drug_info1Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface Drug_info1Repository extends JpaRepository<Drug_info1Entity, Long> {

    @Query(value = "SELECT * FROM drug_info1 WHERE drug_code = ?", nativeQuery = true)
    Optional<Drug_info1Entity> findByDrug_code(int drugCode);

    @Query(value = "SELECT A.* FROM drug_info1 A LEFT OUTER JOIN drugprice B ON A.drug_code=B.drugcode WHERE B.drugcode is null", nativeQuery = true)
    List<Drug_info1Entity> findOuterJoinAll();
}
