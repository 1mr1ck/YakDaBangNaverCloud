package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.Drug_info1Entity;
import com.jxjtech.yakmanager.entity.Drug_info2Entity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Drug_infoAllDTO {

    private Long di1Id;
    private Long di2Id;
    private String product_name;
    private Integer drug_code;
    private String company_name;
    private String characteristic;
    private String product_barcode;
    private List<String> material_element;
    private String storage_method;
    private String expiration;
    private String unit_pack;
    private String main_component;
    private String additives;
    private String ee_data;
    private String ud_data;
    private String nb_data;

    public static Drug_infoAllDTO of(Drug_info1Entity drug1, Drug_info2Entity drug2) {
        Drug_infoAllDTO drug = new Drug_infoAllDTO();
        String name = drug1.getProduct_name();
        if(name.contains("(")) {
            name = name.split("\\(")[0];
        }

        drug.setDi1Id(drug1.getDi1Id());
        drug.setDi2Id(drug2.getDi2Id());
        drug.setProduct_name(name);
        drug.setDrug_code(drug1.getDrug_code());
        drug.setCompany_name(drug1.getCompany_name());
        drug.setCharacteristic(drug1.getCharacteristic());
        drug.setProduct_barcode(drug1.getProduct_barcode());
        drug.setMaterial_element(toList(drug1.getMaterial_element()));
        drug.setExpiration(drug1.getExpiration());
        drug.setUnit_pack(drug1.getUnit_pack());
        drug.setMain_component(drug1.getMain_component());
        drug.setAdditives(drug1.getAdditives());
        drug.setEe_data(drug2.getEe_data());
        drug.setUd_data(drug2.getUd_data());
        drug.setNb_data(drug2.getNb_data());

        return drug;
    }

    public static Drug_infoAllDTO of2(Drug_info1Entity drug1) {
        String name = drug1.getProduct_name();
        if(name.contains("(")) {
            name = name.split("\\(")[0];
        }

        Drug_infoAllDTO drug = new Drug_infoAllDTO();
        drug.setDi1Id(drug1.getDi1Id());
        drug.setProduct_name(name);
        drug.setDrug_code(drug1.getDrug_code());
        drug.setCompany_name(drug1.getCompany_name());
        drug.setCharacteristic(drug1.getCharacteristic());
        drug.setProduct_barcode(drug1.getProduct_barcode());
        drug.setMaterial_element(toList(drug1.getMaterial_element()));
        drug.setExpiration(drug1.getExpiration());
        drug.setUnit_pack(drug1.getUnit_pack());
        drug.setMain_component(drug1.getMain_component());
        drug.setAdditives(drug1.getAdditives());

        return drug;
    }

    public static Drug_infoAllDTO of3(Drug_info2Entity drug2) {
        String name = drug2.getProduct_name();
        if(name.contains("(")) {
            name = name.split("\\(")[0];
        }

        Drug_infoAllDTO drug = new Drug_infoAllDTO();
        drug.setProduct_name(name);
        drug.setDi2Id(drug2.getDi2Id());
        drug.setEe_data(drug2.getEe_data());
        drug.setUd_data(drug2.getUd_data());
        drug.setNb_data(drug2.getNb_data());

        return drug;
    }

    private static List<String> toList(String material) {
        List<String> result = new ArrayList<>();
        if(material.contains("|")) {
            String[] firstSplit = material.split("\\|");
            for(String add : firstSplit) {
                result.add(add);
            }
        } else {
            result.add(material);
        }

        return result;
    }

}
