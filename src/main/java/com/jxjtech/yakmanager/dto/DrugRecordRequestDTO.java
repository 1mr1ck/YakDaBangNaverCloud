package com.jxjtech.yakmanager.dto;

import lombok.Data;

import java.util.List;

@Data
public class DrugRecordRequestDTO {

    private Integer drugCode;
    private Integer productCode;
    private String drugName;
    private List<PackageInfoDTO> packageInfo;
    private Integer drugPrice;
    private Integer drugTotalPrice;
    private Integer drugTotalQuantity;
}
