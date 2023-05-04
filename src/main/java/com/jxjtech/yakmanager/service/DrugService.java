package com.jxjtech.yakmanager.service;

import com.jxjtech.yakmanager.dto.DrugPriceDTO;
import com.jxjtech.yakmanager.dto.DrugSearchForInfoDTO;
import com.jxjtech.yakmanager.dto.Drug_infoAllDTO;
import com.jxjtech.yakmanager.entity.*;
import com.jxjtech.yakmanager.exception.AppException;
import com.jxjtech.yakmanager.exception.ErrorCode;
import com.jxjtech.yakmanager.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrugService {
    private final DrugPackageRepository drugPackageRepository;

    private final Drug_info1Repository drug_info1Repository;
    private final Drug_info2Repository drug_info2Repository;
    private final DrugNameRepository drugNameRepository;
    private final DrugPriceRepository drugPriceRepository;
    private final DrugImgRepository drugImgRepository;


    public Drug_infoAllDTO drugInfoByDrugCode(int drugCode) {

        Optional<Drug_info1Entity> info1 = drug_info1Repository.findByDrug_code(drugCode);
        Optional<Drug_info2Entity> info2 = drug_info2Repository.findByDrug_code(drugCode);

        if (info1.isEmpty() && info2.isEmpty()) {
            throw new AppException(ErrorCode.NOT_EXIST_DATA);
        }
        else if (info1.isPresent() && info2.isPresent()) {
            return Drug_infoAllDTO.of(info1.get(), info2.get());
        }
        else if(info1.isPresent()) {
            return Drug_infoAllDTO.of2(info1.get());
        }
        else {
            return Drug_infoAllDTO.of3(info2.get());
        }
    }

    public Drug_infoAllDTO drugInfoByDrugName(String drugName) {
        if (drugName.length() < 2) {
            return null;
        }

        int drugCode = drugNameRepository.getDrugCodeByDrugName(drugName);

        return drugInfoByDrugCode(drugCode);
    }

    public List<DrugSearchForInfoDTO> search(String drugName) {
        if (drugName.length() < 2) {
            return null;
        }

        String name = drugName + "%";
        List<DrugNameEntity> drugNameEntities = drugNameRepository.findAllByDrugName(name);

        return DrugSearchForInfoDTO.getList(drugNameEntities);
    }

    @Transactional
    public boolean dbUpdate() {
        List<Drug_info1Entity> drug_info1Entities = drug_info1Repository.findAll();

        for (int i = 0; i < drug_info1Entities.size(); i++) {
            String material_element = drug_info1Entities.get(i).getMaterial_element();

            if (material_element.equals("") || isNumeric(material_element)) {
                continue;
            }

            String result = "";
            if (material_element.contains(";")) {
                log.info(material_element);
                String[] me1 = material_element.split(";");
                for (String s : me1) {
                    String[] me2 = s.split("\\|");
                    for(int j=0; j<me2.length; j++) {
                        if(isNumeric(me2[j])) {
                            me2[j] = " " + me2[j];
                        }
                    }
                    log.info("index i : " + i);
                    if (me2[0].equals("") && me2.length < 5) {
                        result += me2[1] + me2[3];
                    } else if(me2.length == 4) {
                        result += me2[1] + me2[3];
                    } else if(me2.length == 1) {
                        result += "," + me2[0];
                    }
                    else {
                        result += me2[1] + me2[3] + me2[4];
                    }
                    result += "|";
                }
            } else {
                log.info(material_element);
                String[] me2 = material_element.split("\\|");
                for(int j=0; j<me2.length; j++) {
                    if(isNumeric(me2[j])) {
                        me2[j] = " " + me2[j];
                    }
                }
                log.info("index i : " + i + " / me2 = " + me2[0]);
                if (me2.length < 5) {
                    result += me2[1] + me2[3];
                } else {
                    result += me2[1] + me2[3] + me2[4];
                }
            }

            drug_info1Entities.get(i).dbUpdate(drug_info1Entities.get(i), result);
        }

        return true;
    }

    private static boolean isNumeric(String s) {
        return NumberUtils.isCreatable(s);
    }


    @Transactional
    public boolean dbUpdate2() {
        List<DrugNameEntity> drugNameEntities = drugNameRepository.findAll();
        List<Drug_info1Entity> drug_info1Entities = drug_info1Repository.findAll();

        for(Drug_info1Entity entity : drug_info1Entities) {
            for(DrugNameEntity entity1 : drugNameEntities) {
                if(entity1.getDrugCode().equals(entity.getDrug_code())) {
                    entity.setProduct_name(entity1.getDrugName());
                    drug_info1Repository.save(entity);
                }
            }
        }

        return true;
    }

    @Transactional
    public void dbUpdate6() {
        List<DrugPackageEntity> drugPackageEntities = drugPackageRepository.findAll();

        drugPackageEntities.stream().map(DrugPackageEntity::changeName).collect(Collectors.toList());
    }

    public void MaterialDBUpdate() {
        List<Drug_info1Entity> drug_info1Entities = drug_info1Repository.findAll();

        drug_info1Entities.stream().map(Drug_info1Entity::deleteBar).collect(Collectors.toList());
    }

    @Transactional
    public void dbUpdate7() {
        List<Drug_info1Entity> drug_info1Entities = drug_info1Repository.findAll();

        List<DrugImgEntity> drugImgEntities = drugImgRepository.findAll();

        int count = 0;
        for(Drug_info1Entity entity : drug_info1Entities) {
            for(DrugImgEntity entity1 : drugImgEntities) {
                int info1_dc = entity.getDrug_code();

                if(info1_dc == entity1.getDrugCode()) {
                    entity.setDrugImg(entity1.getDrugImg());
                }
            }
        }
        drug_info1Repository.saveAll(drug_info1Entities);
        log.info("count : " + count);
    }
}
