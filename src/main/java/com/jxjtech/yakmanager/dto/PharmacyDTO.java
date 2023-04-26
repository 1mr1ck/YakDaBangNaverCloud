package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.PharmacyEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyDTO {

    private Long pharmacyId;
    private String pharmacyName;
    private Long memberId;
    private int totalTitle;

    public PharmacyDTO(PharmacyEntity pharmacyEntity) {
        this.pharmacyId = pharmacyEntity.getPharmacyId();
        this.pharmacyName = pharmacyEntity.getPharmacyName();
        this.memberId = pharmacyEntity.getMemberId();
        this.totalTitle = pharmacyEntity.getTotalTitle();
    }
}
