package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.TitleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TitleDTO {

    private Long titleId;
    private String titleName;
    private Long pharmacyId;
    private int drugRecordCnt;
    private int totalPrice;
    private Timestamp modDate;

    public TitleDTO(TitleEntity titleEntity) {
        this.titleId = titleEntity.getTitleId();
        this.titleName = titleEntity.getTitleName();
        this.pharmacyId = titleEntity.getPharmacyId();
        this.drugRecordCnt = titleEntity.getDrugRecordCnt();
        this.totalPrice = titleEntity.getTotalPrice();
        this.modDate = titleEntity.getModDate();
    }

    public TitleDTO(Long pharmacyId, String titleName) {
        this.titleName = titleName;
        this.pharmacyId = pharmacyId;
    }

    public TitleDTO(TitleEntity titleEntity, String titleName) {
        this.titleId = titleEntity.getTitleId();
        this.titleName = titleName;
        this.pharmacyId = titleEntity.getPharmacyId();
        this.drugRecordCnt = titleEntity.getDrugRecordCnt();
        this.totalPrice = titleEntity.getTotalPrice();
        this.modDate = titleEntity.getModDate();
    }

    public static List<TitleDTO> getDTOList(List<TitleEntity> titleEntityList) {
        List<TitleDTO> result = new ArrayList<>();

        for(TitleEntity title : titleEntityList) {
            TitleDTO dto = new TitleDTO(title);
            result.add(dto);
        }

        return result;
    }
}
