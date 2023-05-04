package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.DrugPriceEntity;
import com.jxjtech.yakmanager.entity.Drug_info1Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrugPriceDTO {

    private Long drugPackageId;
    private Integer drugCode;
    private Integer productCode;
    private String drugName;
    private Integer drugPrice;
    private String drugQuantity;
    private String drugUnit;

    public DrugPriceDTO(Drug_info1Entity info1) {
        this.drugCode = info1.getDrug_code();
        this.drugName = info1.getProduct_name();
    }

    public DrugPriceDTO(DrugPriceEntity entity) {
        this.drugCode = entity.getDrugCode();
        this.productCode = entity.getProductCode();
        this.drugName = entity.getDrugName();
    }
}
