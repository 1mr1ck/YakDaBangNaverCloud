package com.jxjtech.yakmanager.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class RefreshTokenDTO {

    private Long refreshTokenId;
    private Long memberId;
    private String refreshToken;
    private Timestamp modDate;
}
