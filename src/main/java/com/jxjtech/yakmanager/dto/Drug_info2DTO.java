package com.jxjtech.yakmanager.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class Drug_info2DTO {

    private Long di2Id;
    private String product_name;
    private int drug_code;
    private String ee_data;
    private String ud_data;
    private String nb_data;
}
