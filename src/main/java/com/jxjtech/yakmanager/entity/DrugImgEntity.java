package com.jxjtech.yakmanager.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "drugimg")
@Data
public class DrugImgEntity {

    @Id
    @Column(name = "drugImgId")
    private Long drugImgId;
    @Column(name = "drugCode")
    private int drugCode;
    @Column(name = "drugImg")
    private String drugImg;

}
