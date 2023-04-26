package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.NoticeEntity;
import lombok.Data;

import javax.persistence.Column;

@Data
public class NoticeRequestDTO {

    private String noticeTitle;
    private String noticeContent;
    private String noticeCategory;

    public static NoticeEntity toEntity(NoticeRequestDTO dto) {
        return new NoticeEntity().builder()
                .noticeTitle(dto.getNoticeTitle())
                .noticeContent(dto.getNoticeContent())
                .noticeCategory(dto.getNoticeCategory())
                .build();
    }
}
