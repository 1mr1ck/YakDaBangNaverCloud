package com.jxjtech.yakmanager.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PolicyDTO {

    private Long policyId;
    private Long memberId;
    private boolean pushAgree;
    private boolean marketingAgree;
    private Timestamp acceptDate;
}
