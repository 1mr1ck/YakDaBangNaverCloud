package com.jxjtech.yakmanager.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxjtech.yakmanager.entity.DrugRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDrugRecordDTO {

    private Long drugId;
    private Long titleId;
    private Integer drugCode;
    private Integer productCode;
    private List<String> drugPackage;
    private String drugName;
    private List<Integer> drugQuantity;
    private Integer drugTotalQuantity;
    private Integer drugTotalPrice;
    private Integer drugPrice;
    private Integer priceProfit;
    private Integer nowDrugPrice;
    private Timestamp regDate;

    public static List<GetDrugRecordDTO> toDTOList(List<DrugRecordEntity> drugRecordEntities) throws JsonProcessingException {
        List<GetDrugRecordDTO> result = new ArrayList<>();

        for(DrugRecordEntity entity : drugRecordEntities) {
            GetDrugRecordDTO dto = new GetDrugRecordDTO(entity);
            result.add(dto);
        }

        return result;
    }

    public GetDrugRecordDTO(DrugRecordEntity entity) throws JsonProcessingException {
        this.drugId = entity.getDrugId();
        this.titleId = entity.getTitleId();
        this.drugCode = entity.getDrugCode();
        this.productCode = entity.getProductCode();
        this.drugPackage = getStringList(entity.getDrugPackage());
        this.drugName = entity.getDrugName();
        this.drugPrice = entity.getDrugPrice();
        this.drugQuantity = getIntList(entity.getDrugQuantity());
        this.drugTotalQuantity = entity.getDrugTotalQuantity();
        this.drugTotalPrice = entity.getDrugTotalPrice();
        this.regDate = entity.getRegDate();
    }

    private List<Integer> getIntList(String drugQuantity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(drugQuantity, new TypeReference<List<Integer>>() {});
    }

    public List<String> getStringList(String packageInfo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(packageInfo, new TypeReference<List<String>>() {});
    }
}
