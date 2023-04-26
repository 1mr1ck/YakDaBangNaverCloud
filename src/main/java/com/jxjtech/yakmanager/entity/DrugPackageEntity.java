package com.jxjtech.yakmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "drugpackage")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class DrugPackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drugPackageId")
    private Long drugPackageId;
    @Column(name = "drugCode")
    private Integer drugCode;
    @Column(name = "drugName")
    private String drugName;
    @Column(name = "drugStandard")
    private String drugStandard;
    @Column(name = "drugQuantity")
    private Integer drugQuantity;
    @Column(name = "drugForm")
    private String drugForm;
    @Column(name = "drugUnitPack")
    private String drugUnitPack;

    public static DrugPackageEntity changeName(DrugPackageEntity entity) {
        String drugName = entity.getDrugName();
        if(drugName.contains("(")) {
            String[] s = drugName.split("\\(");
            drugName = s[0];
        }

        if(drugName.contains("[")) {
            String[] s = drugName.split("\\[");
            drugName = s[0];
        }

        if(drugName.contains("{")) {
            String[] s = drugName.split("\\{");
            drugName = s[0];
        }

        entity.setDrugName(drugName);

        return entity;
    }
}
