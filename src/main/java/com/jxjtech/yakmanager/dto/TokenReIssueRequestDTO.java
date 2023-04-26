package com.jxjtech.yakmanager.dto;

import lombok.Data;

@Data
public class TokenReIssueRequestDTO {

    private String Authorization;
    private String RefreshToken;
}
