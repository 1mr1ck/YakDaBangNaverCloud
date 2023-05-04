package com.jxjtech.yakmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrugPackageInfoRequestDTO {

    private Integer drugCode;
    private Integer productCode;

}
