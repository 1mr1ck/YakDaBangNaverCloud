package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.NarcoticDrugRecordEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NarcoticDrugRecordDTO {

    private Long narcoticDrugRecordId;
    private String drugName;
    private String drugQuantity;
    private Integer drugCode;
    private Integer productCode;
    private Long memberId;
    private int check;

}
