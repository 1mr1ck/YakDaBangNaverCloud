package com.jxjtech.yakmanager.service;

import com.jxjtech.yakmanager.dto.NoticeDTO;
import com.jxjtech.yakmanager.dto.NoticeRequestDTO;
import com.jxjtech.yakmanager.entity.MemberEntity;
import com.jxjtech.yakmanager.entity.NoticeEntity;
import com.jxjtech.yakmanager.exception.AppException;
import com.jxjtech.yakmanager.exception.ErrorCode;
import com.jxjtech.yakmanager.repository.MemberRepository;
import com.jxjtech.yakmanager.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeService {
    private final MemberRepository memberRepository;

    private final NoticeRepository noticeRepository;

    public List<NoticeDTO> noticeList() {
        List<NoticeEntity> noticeEntityList = noticeRepository.findAll();
        List<NoticeDTO> resultList = new ArrayList<>();

        for(NoticeEntity en : noticeEntityList) {
            NoticeDTO dto = NoticeDTO.of(en);
            resultList.add(dto);
        }

        return resultList;
    }


    public NoticeDTO detail(Long noticeId) {
        Optional<NoticeEntity> optNotice = noticeRepository.findById(noticeId);

        if(optNotice.isEmpty()) {
            throw new AppException(ErrorCode.NOT_EXIST_DATA);
        }

        return NoticeDTO.of(optNotice.get());
    }

    @Transactional
    public NoticeDTO create(NoticeRequestDTO dto) {
        MemberEntity member = memberRepository.findById(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(!member.getMemberRole().toString().equals("admin")) {
            throw new AppException(ErrorCode.NOT_ADMIN);
        }

        NoticeEntity result = NoticeRequestDTO.toEntity(dto);
        noticeRepository.save(result);

        return NoticeDTO.of(result);
    }

    public List<NoticeDTO> noticeListByCategory(String category) {
        List<NoticeEntity> noticeEntityList = noticeRepository.findAllByCategory(category);

        return NoticeDTO.ofList(noticeEntityList);
    }

    @Transactional
    public NoticeDTO update(Long noticeId, NoticeRequestDTO dto) {
        NoticeEntity entity = noticeRepository.findById(noticeId).orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA));

        NoticeDTO noticeDTO = NoticeDTO.of(entity);
        noticeDTO.setNoticeTitle(dto.getNoticeTitle());
        noticeDTO.setNoticeContent(dto.getNoticeContent());
        noticeDTO.setNoticeCategory(dto.getNoticeCategory());

        entity.update(entity, dto);

        return noticeDTO;
    }
}
