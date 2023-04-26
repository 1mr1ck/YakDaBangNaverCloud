package com.jxjtech.yakmanager.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxjtech.yakmanager.dto.DrugRecordDTO;
import com.jxjtech.yakmanager.dto.DrugRecordRequestDTO;
import com.jxjtech.yakmanager.dto.PackageInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Table(name = "drugrecord")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DrugRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drugId")
    private Long drugId;
    @Column(name = "titleId")
    private Long titleId;
    @Column(name = "drugCode")
    private Integer drugCode;
    @Column(name = "productCode")
    private Integer productCode;
    @Column(name = "drugName")
    private String drugName;
    @Column(name = "drugPackage")
    private String drugPackage;
    @Column(name = "drugQuantity")
    private String drugQuantity;
    @Column(name = "drugTotalQuantity")
    private Integer drugTotalQuantity;
    @Column(name = "drugPrice")
    private Integer drugPrice;
    @Column(name = "drugTotalPrice")
    private Integer drugTotalPrice;
    @Column(name = "regDate")
    @UpdateTimestamp
    private Timestamp regDate;

    public DrugRecordEntity(DrugRecordDTO dto) {
        this.titleId = dto.getTitleId();
        this.drugCode = dto.getDrugCode();
        this.productCode = dto.getProductCode();
        this.drugName = dto.getDrugName();
        this.drugPackage = dto.getDrugPackage();
        this.drugQuantity = dto.getDrugQuantity();
        this.drugTotalQuantity = dto.getDrugTotalQuantity();
        this.drugPrice = dto.getDrugPrice();
        this.drugTotalPrice = dto.getDrugTotalPrice();
    }

    public static DrugRecordEntity modify(DrugRecordEntity entity, DrugRecordRequestDTO dto) throws JsonProcessingException {
        DrugRecordEntity result = entity;
        entity.setDrugPrice(dto.getDrugPrice());
        entity.setDrugTotalPrice(dto.getDrugTotalPrice());
        entity.setDrugTotalQuantity(dto.getDrugTotalQuantity());
        entity.setDrugPackage(getPackage(dto.getPackageInfo()));
        entity.setDrugQuantity(getQuantity(dto.getPackageInfo()));

        return result;
    }

    private static String getPackage(List<PackageInfoDTO> packageInfo) throws JsonProcessingException {
        List<String> resultList = new ArrayList<>();

        for(PackageInfoDTO dto : packageInfo) {
            resultList.add(dto.getDrugPackage());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(resultList);

        return result;
    }

    private static String getQuantity(List<PackageInfoDTO> packageInfo) throws JsonProcessingException {
        List<Integer> resultList = new ArrayList<>();

        for(PackageInfoDTO dto : packageInfo) {
            resultList.add(dto.getNum());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(resultList);

        return result;
    }
}
