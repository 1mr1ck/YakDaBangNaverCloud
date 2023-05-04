package com.jxjtech.yakmanager.entity;

import com.jxjtech.yakmanager.dto.DrugPriceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "newdrugprice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewDrugPriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drugPriceId")
    private Long drugPriceId;
    @Column(name = "drugCode")
    private Integer drugCode;
    @Column(name = "productCode")
    private Integer productCode;
    @Column(name = "drugName")
    private String drugName;
    @Column(name = "drugPrice")
    private String drugPrice;
    @Column(name = "drugQuantity")
    private String drugQuantity;
    @Column(name = "drugUnit")
    private String drugUnit;

    public static NewDrugPriceEntity changeName(NewDrugPriceEntity entity) {
        String drugName = entity.getDrugName();
        if(drugName.contains("_")) {
            String[] s = drugName.split("_");
            if(s[0].contains("(")) {
                s[0] = s[0].split("\\(")[0];
            }
            drugName = s[0] + s[1];
        } else if(drugName.contains("(") && entity.getProductCode() == null) {
            drugName = drugName.split("\\(")[0];
        }

        entity.setDrugName(drugName);

        return entity;
    }

    public NewDrugPriceEntity(DrugPriceDTO dto) {
        this.drugName = dto.getDrugName();
        this.drugCode = dto.getDrugCode();
    }
}
