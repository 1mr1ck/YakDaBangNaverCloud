package com.jxjtech.yakmanager.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "drugname")
@Getter
public class DrugNameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drugNameId")
    private Long drugNameId;
    @Column(name = "drugCode")
    private Integer drugCode;
    @Column(name = "drugName")
    private String drugName;
//    @Column(name = "productCode")
//    private String productCode;
}
