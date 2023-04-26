package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.DrugPriceEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DrugSearchDTO {

    Integer drugCode;
    Integer productCode;
    String drugName;

    public static List<DrugSearchDTO> getList(List<DrugPriceEntity> list) {
        List<DrugSearchDTO> result = new ArrayList<>();

        for(DrugPriceEntity entity : list) {
            DrugSearchDTO dto = new DrugSearchDTO();
            dto.setDrugCode(entity.getDrugCode());
            dto.setProductCode(entity.getProductCode());
            dto.setDrugName(entity.getDrugName());

            result.add(dto);
        }

        return result;
    }
}
