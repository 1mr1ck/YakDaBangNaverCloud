package com.jxjtech.yakmanager.entity;

import com.jxjtech.yakmanager.dto.NarcoticDrugRecordDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NarcoticDrugRecord")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NarcoticDrugRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "narcoticDrugRecordId")
    private Long narcoticDrugRecordId;

    @Column(name = "drugName")
    private String drugName;

    @Column(name = "drugQuantity")
    private String drugQuantity;

    @Column(name = "drugCode")
    private Integer drugCode;

    @Column(name = "productCode")
    private Integer productCode;

    @Column(name = "memberId")
    private Long memberId;

    @Column(name = "`check`")
    private int check;



    public static List<NarcoticDrugRecordEntity> ofList(List<NarcoticDrugRecordDTO> narcoticDrugRecordDTOS) {
        List<NarcoticDrugRecordEntity> result = new ArrayList<>();
        for(NarcoticDrugRecordDTO dto : narcoticDrugRecordDTOS) {
            NarcoticDrugRecordEntity entity = NarcoticDrugRecordEntity.builder()
                    .drugName(dto.getDrugName())
                    .drugQuantity(dto.getDrugQuantity())
                    .drugCode(dto.getDrugCode())
                    .productCode(dto.getProductCode())
                    .build();

            result.add(entity);
        }

        return result;
    }

    public static NarcoticDrugRecordDTO of(NarcoticDrugRecordEntity entity) {
        NarcoticDrugRecordDTO result = new NarcoticDrugRecordDTO();

        result.setNarcoticDrugRecordId(entity.getNarcoticDrugRecordId());
        result.setDrugName(entity.getDrugName());
        result.setDrugQuantity(entity.getDrugQuantity());
        result.setDrugCode(entity.getDrugCode());
        result.setProductCode(entity.getProductCode());
        result.setMemberId(entity.getMemberId());

        return result;
    }
}
