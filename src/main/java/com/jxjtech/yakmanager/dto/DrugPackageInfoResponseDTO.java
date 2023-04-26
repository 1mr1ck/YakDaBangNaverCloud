package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.DrugPackageEntity;
import com.jxjtech.yakmanager.entity.DrugPriceEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DrugPackageInfoResponseDTO {

    private Integer drugCode;
    private Integer productCode;
    private String drugName;
    private List<String> packageInfo;
    private String price;


    // 보험코드가 있고, 정이나 캡슐일 때
    public static DrugPackageInfoResponseDTO toResponseDTO(List<DrugPackageEntity> packageEntity, DrugPriceEntity priceEntity) {
        DrugPackageInfoResponseDTO result = new DrugPackageInfoResponseDTO();
        result.setDrugCode(priceEntity.getDrugCode());
        result.setProductCode(priceEntity.getProductCode());
        result.setDrugName(priceEntity.getDrugName());
        result.setPrice(priceEntity.getDrugPrice());
        List<String> packageInfo = new ArrayList<>();

        if(packageEntity.size() > 0) {
            for(DrugPackageEntity entity : packageEntity) {
                String s = "";
                if(entity.getDrugQuantity() == 0) {
                    s = priceEntity.getDrugQuantity() + "/" + priceEntity.getDrugUnit();
                } else {
                    s = entity.getDrugQuantity() + entity.getDrugQuantity() + "/" + entity.getDrugUnitPack();
                }
                packageInfo.add(s);
            }
        } else {
            String s = 1 + priceEntity.getDrugQuantity() + "/" + priceEntity.getDrugUnit();
            packageInfo.add(s);
        }

        result.setPackageInfo(packageInfo);

        return result;
    }

    // 보험코드가 있고, 정이나 캡슐이 아닐 때
    public static DrugPackageInfoResponseDTO toResponseDTO2(DrugPriceEntity priceEntity) {
        String s = "";
        DrugPackageInfoResponseDTO result = new DrugPackageInfoResponseDTO();
        result.setDrugCode(priceEntity.getDrugCode());
        result.setProductCode(priceEntity.getProductCode());
        result.setDrugName(priceEntity.getDrugName());
        result.setPrice(priceEntity.getDrugPrice());
        List<String> packageInfo = new ArrayList<>();

        if (priceEntity.getDrugQuantity().contains("(")) {
            String standard = priceEntity.getDrugQuantity().split("\\(")[0];
            s += standard + priceEntity.getDrugUnit();
            String price = "";
            if(!standard.contains(".")) {
                price = String.valueOf(Integer.parseInt(priceEntity.getDrugPrice()) * Integer.parseInt(standard));
            } else {
                price = String.valueOf(Integer.parseInt(priceEntity.getDrugPrice()) * Double.parseDouble(standard));
            }
            result.setPrice(price);
            packageInfo.add(s);
        } else {
            s += priceEntity.getDrugQuantity() + priceEntity.getDrugUnit();
            packageInfo.add(s);
        }
        result.setPackageInfo(packageInfo);

        return result;
    }

    // 보험코드가 없을 때,
    public static DrugPackageInfoResponseDTO toResponseDTO3(List<DrugPackageEntity> packageEntity, DrugPriceEntity priceEntity) {
        DrugPackageInfoResponseDTO result = new DrugPackageInfoResponseDTO();
        List<String> packageInfo = new ArrayList<>();
        if (packageEntity.size() > 0) {
            for (DrugPackageEntity entity : packageEntity) {
                String s = "";
                String drugName = entity.getDrugName();
                result.setDrugCode(entity.getDrugCode());
                result.setProductCode(priceEntity.getProductCode());
                result.setDrugName(priceEntity.getDrugName());
                result.setPrice(priceEntity.getDrugPrice());
                if (entity.getDrugQuantity() == 0) {
                    if (!drugName.contains("캡슐") && !drugName.contains("정")) {
                        s = 1 + "개";
                    } else if (drugName.contains("캡슐")) {
                        s = 1 + "낱알/낱알" + "(" + entity.getDrugStandard() + ")";
                    } else if (drugName.contains("정") && drugName.matches(".*\\d.*")) {
                        drugName = drugName.split("\\d+")[0];
                        if (drugName.charAt(drugName.length() - 1) == '정') {
                            s = 1 + "낱알/낱알"  + "(" + entity.getDrugStandard() + ")";
                        }
                    } else {
                        s = 1 + "개";
                    }
                } else {
                    s = entity.getDrugQuantity() + "/" + entity.getDrugUnitPack()   + "(" + entity.getDrugStandard() + ")";
                }
                packageInfo.add(s);
            }
            result.setPackageInfo(packageInfo);
        } else {
            result.setDrugCode(priceEntity.getDrugCode());
            result.setProductCode(priceEntity.getProductCode());
            result.setDrugName(priceEntity.getDrugName());
            result.setPrice(priceEntity.getDrugPrice());
            packageInfo.add(1 + "개");
            result.setPackageInfo(packageInfo);
        }
        return result;
    }
}
