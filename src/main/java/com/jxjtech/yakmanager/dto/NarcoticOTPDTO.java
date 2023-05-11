package com.jxjtech.yakmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NarcoticOTPDTO {

    private Long narcoticOTPId;
    private String OTPCode;
    private Long memberId;
    private Timestamp regDate;
}
