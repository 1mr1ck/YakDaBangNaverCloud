package com.jxjtech.yakmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationDTO {

    private Long invitationId;
    private String invitationCode;
    private Long pharmacyId;
    private Long regDate;
    private Long expiredTime;

    public InvitationDTO(Long pharmacyId, String invitationCode, Long regDate, Long expiredTime) {
        this.pharmacyId = pharmacyId;
        this.invitationCode = invitationCode;
        this.regDate = regDate;
        this.expiredTime = expiredTime;
    }
}
