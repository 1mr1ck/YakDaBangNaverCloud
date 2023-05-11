package com.jxjtech.yakmanager.dto;

import lombok.Data;

import java.util.Map;

@Data
public class LoginResponseDTO {

    private boolean result;
    private Map<String, String> token;
    private String msg;

    public LoginResponseDTO(Map<String, String> token) {
        this.result = true;
        this.token = token;
        this.msg = "Login Success";
    }

    public LoginResponseDTO(){
        this.result = false;
        this.msg = "Login Failed";
    }
}
