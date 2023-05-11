package com.jxjtech.yakmanager.entity;

import com.jxjtech.yakmanager.dto.NarcoticDrugRecordDTO;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "narcoticdrugrecord")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NarcoticDrugRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "narcoticDrugRecordId")
    private Long narcoticDrugRecordId;

    @Column(name = "narcoticTitleId")
    private Long narcoticTitleId;

    @Column(name = "drugName")
    private String drugName;

    @Column(name = "drugQuantity")
    private String drugQuantity;

    @Column(name = "drugCode")
    private Integer drugCode;

    @Column(name = "productCode")
    private Integer productCode;

    @Column(name = "nowDrugQuantity")
    private String nowDrugQuantity;

    @Column(name = "`check`")
    // 0 수정안됨 , 1 값 일치 , 2 값 불일치
    private int check;



    public static List<NarcoticDrugRecordEntity> ofList(List<NarcoticDrugRecordDTO> narcoticDrugRecordDTOS) {
        List<NarcoticDrugRecordEntity> result = new ArrayList<>();
        for(NarcoticDrugRecordDTO dto : narcoticDrugRecordDTOS) {
            NarcoticDrugRecordEntity entity = NarcoticDrugRecordEntity.builder()
                    .drugName(dto.getDrugName())
                    .drugQuantity(dto.getDrugQuantity())
                    .drugCode(dto.getDrugCode())
                    .productCode(dto.getProductCode())
                    .narcoticTitleId(dto.getNarcoticTitleId())
                    .build();

            result.add(entity);
        }

        return result;
    }

    public static NarcoticDrugRecordDTO of(NarcoticDrugRecordEntity entity) {
        NarcoticDrugRecordDTO result = new NarcoticDrugRecordDTO();

        result.setNarcoticDrugRecordId(entity.getNarcoticDrugRecordId());
        result.setCheck(entity.getCheck());
        result.setDrugName(entity.getDrugName());
        result.setDrugQuantity(entity.getDrugQuantity());
        result.setDrugCode(entity.getDrugCode());
        result.setProductCode(entity.getProductCode());
        result.setNarcoticTitleId(entity.getNarcoticTitleId());
        result.setNowDrugQuantity(entity.getNowDrugQuantity());

        return result;
    }

}
