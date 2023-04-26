package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.Authority;
import com.jxjtech.yakmanager.entity.PharmacyEntity;
import com.jxjtech.yakmanager.entity.TitleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyTitleDTO {

    private Long pharmacyId;
    private String pharmacyName;
    private Long memberId;
    private int totalTitle;
    private Authority pharmacyMemberRole;
    private List<TitleDTO> titleList;

    public PharmacyTitleDTO(PharmacyEntity pharmacyEntity, Authority role, List<TitleEntity> titleEntityList) {
        this.pharmacyId = pharmacyEntity.getPharmacyId();
        this.pharmacyName = pharmacyEntity.getPharmacyName();
        this.memberId = pharmacyEntity.getMemberId();
        this.totalTitle = pharmacyEntity.getTotalTitle();
        this.pharmacyMemberRole = role;
        List<TitleDTO> titleDTOList = new ArrayList<>();
        for(TitleEntity title : titleEntityList) {
            TitleDTO dto = new TitleDTO(title);
            titleDTOList.add(dto);
        }
        this.titleList = titleDTOList;
    }
}
