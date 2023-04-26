package com.jxjtech.yakmanager.dto;

import lombok.Data;

@Data
public class RememberMeRequestDTO {

    private String Authorization;
    private String RefreshToken;
    private String timeZone;
}
