package com.jxjtech.yakmanager.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "drug_info2")
@Getter
public class Drug_info2Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "di2Id")
    private Long di2Id;
    @Column(name = "product_name")
    private String product_name;
    @Column(name = "drug_code")
    private int drug_code;
    @Column(name = "ee_data")
    private String ee_data;
    @Column(name = "ud_data")
    private String ud_data;
    @Column(name = "nb_data")
    private String nb_data;
}
