package com.jxjtech.yakmanager.dto;

import lombok.Data;

@Data
public class DrugPackageDTO {

    private Integer drugCode;
    private Integer drugQuantity;
    private String standard;
    private String unitPack;
}
