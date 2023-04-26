package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.NoticeEntity;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class NoticeDTO {

    private Long noticeId;
    private String noticeTitle;
    private String noticeContent;
    private String noticeCategory;
    private Timestamp noticeRegDate;

    public static NoticeDTO of(NoticeEntity notice) {
        NoticeDTO result = new NoticeDTO();
        result.setNoticeId(notice.getNoticeId());
        result.setNoticeContent(notice.getNoticeContent());
        result.setNoticeTitle(notice.getNoticeTitle());
        result.setNoticeCategory(notice.getNoticeCategory());
        result.setNoticeRegDate(notice.getNoticeRegDate());

        return result;
    }

    public static List<NoticeDTO> ofList(List<NoticeEntity> noticeEntityList) {
        List<NoticeDTO> result = new ArrayList<>();

        for(NoticeEntity entity : noticeEntityList) {
            NoticeDTO dto = NoticeDTO.of(entity);
            result.add(dto);
        }

        return result;
    }
}
