package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.NarcoticTitleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NarcoticTitleDTO {

    private Long narcoticTitleId;
    private Long memberId;
    private String narcoticTitleName;
    private int drugRecordCnt;
    private Timestamp modDate;

    public NarcoticTitleDTO(int size, Long memberId, String origName) {
        this.memberId = memberId;
        this.drugRecordCnt = size;
        this.narcoticTitleName = origName;
    }
}
