package com.jxjtech.yakmanager.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxjtech.yakmanager.entity.DrugRecordEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DrugPackageInDrugRecordDTO {

    private List<String> drugRecord;

    public DrugPackageInDrugRecordDTO modifyPackage(DrugRecordEntity entity) throws JsonProcessingException {
        DrugPackageInDrugRecordDTO result = new DrugPackageInDrugRecordDTO();

        List<List<Object>> beforeDrugRecord = getBeforeDrugRecord(entity.getDrugPackage());
        List<String> afterRecord = new ArrayList<>();

        for(List<Object> obj : beforeDrugRecord) {
            String s = "";
            for(int i=0; i<obj.size(); i++) {
                if(i == obj.size()-1) {
                    s += "/";
                }
                s += String.valueOf(obj.get(i));
            }
            afterRecord.add(s);
        }

        result.setDrugRecord(afterRecord);
        return result;
    }

    public List<List<Object>> getBeforeDrugRecord(String drugRecord) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(drugRecord, new TypeReference<List<List<Object>>>() {});
    }

    public static String saveDrugPackage(DrugPackageInDrugRecordDTO dto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String drugPackageInfo = objectMapper.writeValueAsString(dto.getDrugRecord());

        return drugPackageInfo;
    }
}
