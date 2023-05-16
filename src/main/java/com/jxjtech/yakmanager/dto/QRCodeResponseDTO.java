package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.DrugPackageEntity;
import com.jxjtech.yakmanager.entity.Drug_info1Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QRCodeResponseDTO {

    Integer drugCode;
    String drugName;
    Long memberId;

    public QRCodeResponseDTO(DrugPackageEntity entity, QRCodeURLDTO dto) {
        this.drugCode = entity.getDrugCode();
        this.drugName = entity.getDrugName();
        this.memberId = dto.getMemberId();
    }
}
