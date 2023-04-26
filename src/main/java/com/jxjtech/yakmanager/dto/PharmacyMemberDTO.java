package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.Authority;
import com.jxjtech.yakmanager.entity.PharmacyMemberEntity;
import lombok.Data;

@Data
public class PharmacyMemberDTO {

    private Long pharmacyMemberId;
    private Long memberId;
    private Long pharmacyId;
    private Authority pharmacyMemberRole;

    public PharmacyMemberDTO(PharmacyMemberEntity pm) {
        this.pharmacyMemberId = pm.getPharmacyMemberId();
        this.memberId = pm.getMemberId();
        this.pharmacyId = pm.getPharmacyId();
        this.pharmacyMemberRole = pm.getPharmacyMemberRole();
    }
}
