package com.jxjtech.yakmanager.dto;

import lombok.Data;

@Data
public class RegisterResponseDTO {

    private String memberEmail;
    private String memberNickName;
    private String snsType;
    private boolean result;

    public RegisterResponseDTO(RegisterRequestDTO dto, String snsType) {
        this.memberEmail = dto.getMemberEmail();
        this.memberNickName = dto.getMemberNickName();
        this.snsType = snsType;
        this.result = true;
    }
}
