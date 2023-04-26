package com.jxjtech.yakmanager.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private boolean result;
    private TokenDTO token;
    private String msg;

    public LoginResponseDTO(TokenDTO dto) {
        this.result = true;
        this.token = dto;
        this.msg = "Login Success";
    }

    public LoginResponseDTO(){
        this.result = false;
        this.msg = "Login Failed";
    }
}
