package com.jxjtech.yakmanager.entity;

import com.jxjtech.yakmanager.dto.NarcoticTitleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "narcotictitle")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NarcoticTitleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "narcoticTitleId")
    private Long narcoticTitleId;
    @Column(name = "memberId")
    private Long memberId;
    @Column(name = "narcoticTitleName")
    private String narcoticTitleName;
    @Column(name = "drugRecordCnt")
    private int drugRecordCnt;
    @Column(name = "modDate")
    @UpdateTimestamp
    private Timestamp modDate;

    public static NarcoticTitleEntity of(NarcoticTitleDTO titleDTO) {
        return NarcoticTitleEntity.builder()
                .memberId(titleDTO.getMemberId())
                .narcoticTitleName(titleDTO.getNarcoticTitleName())
                .drugRecordCnt(titleDTO.getDrugRecordCnt())
                .memberId(titleDTO.getMemberId())
                .build();
    }
}
