package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.DrugNameEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrugSearchForInfoDTO {

    private String drugName;
    private Integer drugCode;

    public static List<DrugSearchForInfoDTO> getList(List<DrugNameEntity> drugNameEntities) {
        List<DrugSearchForInfoDTO> result = new ArrayList<>();

        for(DrugNameEntity entity : drugNameEntities) {
            result.add(new DrugSearchForInfoDTO(entity));
        }

        for(int i=0; i<result.size(); i++) {
            String searchDrugName = result.get(i).getDrugName();

            if(searchDrugName.contains("(")) {
                String[] array = searchDrugName.split("\\(");

                searchDrugName = array[0];
                result.get(i).setDrugName(searchDrugName);
            }
        }

        return result;
    }

    private DrugSearchForInfoDTO(DrugNameEntity entity) {
        this.drugCode = entity.getDrugCode();
        this.drugName = entity.getDrugName();
    }
}
