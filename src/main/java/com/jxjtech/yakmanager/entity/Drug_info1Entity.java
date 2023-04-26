package com.jxjtech.yakmanager.entity;

import com.jxjtech.yakmanager.dto.Drug_info1DTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "drug_info1")
@Getter
@Setter
@NoArgsConstructor
public class Drug_info1Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "di1Id")
    private Long di1Id;
    /** 품목명, ITEM_NAME */
    @Column(name = "product_name")
    private String product_name;
    /** 품목일련번호, ITEM_SEQ */
    @Column(name = "drug_code")
    private Integer drug_code;
    /** 업체명, ENTP_NAME */
    @Column(name = "company_name")
    private String company_name;
    /** 성상, CHART */
    @Column(name = "characteristic")
    private String characteristic;
    /** 표준코드, BAR_CODE */
    @Column(name = "product_barcode")
    private String product_barcode;
    /** 원료성분, MATERIAL_NAME */
    @Column(name = "material_element")
    private String material_element;
    /** 저장방법, STORAGE_METHOD */
    @Column(name = "storage_method")
    private String storage_method;
    /** 유통기한, VALID_TERM */
    @Column(name = "expiration")
    private String expiration;
    /** 포장단위, ?? */
    @Column(name = "unit_pack")
    private String unit_pack;
    /** 주성분명, MAIN_ITEM_INGR */
    @Column(name = "main_component")
    private String main_component;
    /** 첨가제명, INGR_NAME */
    @Column(name = "additives")
    private String additives;

    public Drug_info1Entity(Drug_info1DTO dto) {
        this.product_name = dto.getProduct_name();
        this.drug_code = dto.getDrug_code();
        this.company_name = dto.getCompany_name();
        this.characteristic = dto.getCharacteristic();
        this.product_barcode = dto.getProduct_barcode();
        this.material_element = dto.getMaterial_element();
        this.storage_method = dto.getStorage_method();
        this.expiration = dto.getExpiration();
        this.unit_pack = dto.getUnit_pack();
        this.main_component = dto.getMain_component();
        this.additives = dto.getAdditives();
    }

    public Drug_info1Entity dbUpdate(Drug_info1Entity entity, String result) {
        entity.setMaterial_element(result);
        return entity;
    }

    public static Drug_info1Entity deleteBar(Drug_info1Entity entity) {
        String material = entity.getMaterial_element();
        if(material.contains("|")) {
            material = material.substring(0, material.length()-1);
            System.out.println(material);
            String[] array = material.split("\\|");
            System.out.println(array[0] + " " + array[1] + " " + array.length);
        }

        return entity;
    }
}
