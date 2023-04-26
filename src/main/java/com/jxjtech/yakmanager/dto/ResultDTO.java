package com.jxjtech.yakmanager.dto;

import lombok.Data;

@Data
public class ResultDTO {

    private String message;

    public ResultDTO(String msg) {
        this.message = msg;
    }
}
