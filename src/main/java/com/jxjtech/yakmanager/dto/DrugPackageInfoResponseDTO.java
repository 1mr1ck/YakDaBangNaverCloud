package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.DrugPackageEntity;
import com.jxjtech.yakmanager.entity.DrugPriceEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class DrugPackageInfoResponseDTO {

    private Integer drugCode;
    private Integer productCode;
    private String drugName;
    private List<String> packageInfo;
    private String drugPrice;

    // drugCode, productCode is not null & (drugUnit 캡슐 || 정)
    public static DrugPackageInfoResponseDTO ofDrugCodeAndProductCodeNotNull(List<DrugPackageEntity> drugPackageEntities, DrugPriceEntity drugPriceEntity) {
        String s = "";
        List<String> packageInfos = new ArrayList<>();
        if(drugPackageEntities.size() > 1) {
            for(DrugPackageEntity entity : drugPackageEntities) {
                int drugQuantity = entity.getDrugQuantity();
                if(drugQuantity == 0) {
                    log.info("양 0");
                    s = 1 + "낱알/낱알";
                    packageInfos.add(s);
                } else {
                    log.info("양 0 초과");
                    s = drugQuantity + entity.getDrugForm() + "/" + entity.getDrugUnitPack();
                    packageInfos.add(s);
                }
            }
        } else {
            log.info("check size 1");
            s = 1 + "낱알/낱알";
            packageInfos.add(s);
        }
        Collections.reverse(packageInfos);

        return DrugPackageInfoResponseDTO.builder()
                .drugCode(drugPriceEntity.getDrugCode())
                .productCode(drugPriceEntity.getProductCode())
                .drugName(drugPriceEntity.getDrugName())
                .packageInfo(packageInfos)
                .drugPrice(drugPriceEntity.getDrugPrice())
                .build();
    }

    // Not Capsule, Not Pill
    public static DrugPackageInfoResponseDTO ofNotCapsuleAndNotPill(DrugPriceEntity drugPriceEntity) {
        List<String> packageInfos = new ArrayList<>();
        String s = "";
        // 가격 수정이 필요하다.
        String drugQuantity = drugPriceEntity.getDrugQuantity();
        String price = drugPriceEntity.getDrugPrice();
        if(drugQuantity.contains("(")) {
            drugQuantity = drugQuantity.split("\\(")[0];
            price = String.valueOf(Integer.parseInt(drugQuantity) * Integer.parseInt(drugPriceEntity.getDrugPrice()));
        }

        s = "1개";

        packageInfos.add(s);
        Collections.reverse(packageInfos);

        return DrugPackageInfoResponseDTO.builder()
                .drugCode(drugPriceEntity.getDrugCode())
                .productCode(drugPriceEntity.getProductCode())
                .drugName(drugPriceEntity.getDrugName())
                .packageInfo(packageInfos)
                .drugPrice(price)
                .build();
    }

    // drugCode is null, productCode is not null
    public static DrugPackageInfoResponseDTO ofOnlyProductCode(DrugPriceEntity drugPriceEntity) {
        List<String> packageInfos = new ArrayList<>();
        String s = "";
        // 가격 수정이 필요하다.
        String drugQuantity = drugPriceEntity.getDrugQuantity();
        String price = drugPriceEntity.getDrugPrice();
        if(drugQuantity.contains("(")) {
            drugQuantity = drugQuantity.split("\\(")[0];
            price = String.valueOf(Integer.parseInt(drugQuantity) * Integer.parseInt(drugPriceEntity.getDrugPrice()));
        }

        if(drugPriceEntity.getDrugUnit().equals("캡슐") || drugPriceEntity.getDrugUnit().equals("정")) {
            s = "1낱알/낱알";
        } else {
            s = "1개";
        }
        packageInfos.add(s);
        Collections.reverse(packageInfos);

        return DrugPackageInfoResponseDTO.builder()
                .drugCode(drugPriceEntity.getDrugCode())
                .productCode(drugPriceEntity.getProductCode())
                .drugName(drugPriceEntity.getDrugName())
                .packageInfo(packageInfos)
                .drugPrice(price)
                .build();
    }

    // drugCode is not null, productCode is null
    public static DrugPackageInfoResponseDTO ofOnlyDrugCode(List<DrugPackageEntity> drugPackageEntities, DrugPriceEntity drugPriceEntity) {
        String s = "";
        List<String> packageInfos = new ArrayList<>();
        if(drugPackageEntities.size() > 1) {
            for(DrugPackageEntity entity : drugPackageEntities) {
                int drugQuantity = entity.getDrugQuantity();
                if(drugQuantity == 0) {
                    log.info("양 0");
                    s = 1 + "개";
                    packageInfos.add(s);
                } else {
                    log.info("양 0 초과");
                    if(drugPriceEntity.getDrugUnit().equals("캡슐") || drugPriceEntity.getDrugUnit().equals("정")) {
                        s = drugQuantity + entity.getDrugForm() + "/" + entity.getDrugUnitPack();
                    } else {
                        s = "1개";
                    }
                    packageInfos.add(s);
                }
            }
        } else {
            log.info("check size 1");
            s = 1 + "개";
            packageInfos.add(s);
        }

        Collections.reverse(packageInfos);

        return DrugPackageInfoResponseDTO.builder()
                .drugCode(drugPriceEntity.getDrugCode())
                .productCode(drugPriceEntity.getProductCode())
                .drugName(drugPriceEntity.getDrugName())
                .packageInfo(packageInfos)
                .drugPrice(drugPriceEntity.getDrugPrice())
                .build();
        
        
    }
}
