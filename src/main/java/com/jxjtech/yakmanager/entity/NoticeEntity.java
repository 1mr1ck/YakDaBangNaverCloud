package com.jxjtech.yakmanager.entity;

import com.jxjtech.yakmanager.dto.NoticeRequestDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "notice")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noticeId")
    private Long noticeId;
    @Column(name = "noticeTitle")
    private String noticeTitle;
    @Column(name = "noticeContent")
    private String noticeContent;
    @Column(name = "noticeCategory")
    private String noticeCategory;
    @Column(name = "noticeRegDate")
    @CreationTimestamp
    private Timestamp noticeRegDate;

    public NoticeEntity update(NoticeEntity entity, NoticeRequestDTO dto) {
        entity.setNoticeCategory(dto.getNoticeCategory());
        entity.setNoticeContent(dto.getNoticeContent());
        entity.setNoticeTitle(dto.getNoticeTitle());

        return entity;
    }
}
