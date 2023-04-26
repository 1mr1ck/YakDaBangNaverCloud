package com.jxjtech.yakmanager.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxjtech.yakmanager.entity.DrugRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrugRecordDTO {

    private Long drugId;
    private Long titleId;
    private Integer drugCode;
    private Integer productCode;
    private String drugName;
    private String drugPackage;
    private String drugQuantity;
    private Integer drugTotalQuantity;
    private Integer drugPrice;
    private Integer drugTotalPrice;
    private Timestamp regDate;

    public DrugRecordDTO(DrugRecordEntity dr) {
        this.drugId = dr.getDrugId();
        this.titleId = dr.getTitleId();
        this.drugCode = dr.getDrugCode();
        this.productCode = dr.getProductCode() != null ? dr.getProductCode() : null;
        this.drugName = dr.getDrugName();
        this.drugPackage = dr.getDrugPackage();
        this.drugQuantity = dr.getDrugQuantity();
        this.drugTotalQuantity = dr.getDrugTotalQuantity();
        this.drugPrice = dr.getDrugPrice();
        this.drugTotalPrice = dr.getDrugTotalPrice();
        this.regDate = dr.getRegDate();
    }

    public DrugRecordDTO(Long titleId, DrugRecordRequestDTO dto) throws JsonProcessingException {
        this.titleId = titleId;
        this.drugName = dto.getDrugName();
        this.drugCode = dto.getDrugCode();
        this.productCode = dto.getProductCode() != null ? dto.getProductCode() : null;
        this.drugPrice = dto.getDrugPrice();
        this.drugPackage = getPackage(dto.getPackageInfo());
        this.drugQuantity = getQuantity(dto.getPackageInfo());
        this.drugTotalQuantity = dto.getDrugTotalQuantity();
        this.drugTotalPrice = dto.getDrugTotalPrice();
    }

    private String getPackage(List<PackageInfoDTO> packageInfo) throws JsonProcessingException {
        List<String> resultList = new ArrayList<>();

        for(PackageInfoDTO dto : packageInfo) {
            resultList.add(dto.getDrugPackage());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(resultList);

        return result;
    }

    private String getQuantity(List<PackageInfoDTO> packageInfo) throws JsonProcessingException {
        List<Integer> resultList = new ArrayList<>();

        for(PackageInfoDTO dto : packageInfo) {
            resultList.add(dto.getNum());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(resultList);

        return result;
    }

    public static List<DrugRecordDTO> of(List<DrugRecordEntity> data) {
        List<DrugRecordDTO> result = new ArrayList<>();

        for(DrugRecordEntity dr : data) {
            result.add(new DrugRecordDTO(dr));
        }

        return result;
    }
}
