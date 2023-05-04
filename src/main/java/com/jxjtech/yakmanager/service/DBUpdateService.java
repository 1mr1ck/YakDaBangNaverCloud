package com.jxjtech.yakmanager.service;

import com.jxjtech.yakmanager.dto.DrugPriceDTO;
import com.jxjtech.yakmanager.entity.DrugPriceEntity;
import com.jxjtech.yakmanager.entity.Drug_info1Entity;
import com.jxjtech.yakmanager.entity.NewDrugPriceEntity;
import com.jxjtech.yakmanager.repository.DrugPriceRepository;
import com.jxjtech.yakmanager.repository.Drug_info1Repository;
import com.jxjtech.yakmanager.repository.NewDrugPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DBUpdateService {

    private final DrugPriceRepository drugPriceRepository;
    private final NewDrugPriceRepository newDrugPriceRepository;
    private final Drug_info1Repository drug_info1Repository;

    @Transactional
    public void drugPrice() {
        List<NewDrugPriceEntity> newDrugPriceEntities = newDrugPriceRepository.findAll();

//        newDrugPriceEntities.stream().map(NewDrugPriceEntity::changeName).collect(Collectors.toList());

//        List<Drug_info1Entity> drug_info1Entities = drug_info1Repository.findAll();
//
//        for(NewDrugPriceEntity entity : newDrugPriceEntities) {
//            for(int i=0; i<drug_info1Entities.size(); i++) {
//                if(drug_info1Entities.get(i).getProduct_barcode().equals("")) {
//                    continue;
//                }
//                if(drug_info1Entities.get(i).getProduct_barcode().contains(",")) {
//                    String[] barcode = drug_info1Entities.get(i).getProduct_barcode().split(",");
//                    for(String code : barcode) {
//                        if(entity.getProductCode().equals(Integer.parseInt(code))) {
//                            entity.setDrugCode(drug_info1Entities.get(i).getDrug_code());
//                        }
//                    }
//                } else {
//                    if(entity.getProductCode().equals(Integer.parseInt(drug_info1Entities.get(i).getProduct_barcode()))) {
//                        entity.setDrugCode(drug_info1Entities.get(i).getDrug_code());
//                    }
//                }
//            }
//        }

        addDrug();
    }

    @Transactional
    public void addDrug() {
        List<Drug_info1Entity> drug_info1Entities = drug_info1Repository.findOuterJoinAll();
        log.info(drug_info1Entities.size() + "");
        List<NewDrugPriceEntity> addResult = new ArrayList<>();

        for(Drug_info1Entity info1 : drug_info1Entities) {
            DrugPriceDTO dto = new DrugPriceDTO(info1);
            NewDrugPriceEntity add = new NewDrugPriceEntity(dto);
            addResult.add(add);
        }
        newDrugPriceRepository.saveAll(addResult);
    }
}
