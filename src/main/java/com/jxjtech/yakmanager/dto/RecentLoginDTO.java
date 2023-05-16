package com.jxjtech.yakmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentLoginDTO {

    private Long recentLoginId;
    private String phoneValue;
    private String snsType;
    private String osType;
    private String buildVersion;
    private Timestamp modDate;
}
