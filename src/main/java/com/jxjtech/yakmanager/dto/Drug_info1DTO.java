package com.jxjtech.yakmanager.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class Drug_info1DTO {

    private Long di1Id;
    private String product_name;
    private int drug_code;
    private String company_name;
    private String characteristic;
    private String product_barcode;
    private String material_element;
    private String storage_method;
    private String expiration;
    private String unit_pack;
    private String main_component;
    private String additives;
}
