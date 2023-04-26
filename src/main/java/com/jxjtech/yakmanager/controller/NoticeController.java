package com.jxjtech.yakmanager.controller;

import com.jxjtech.yakmanager.dto.NoticeRequestDTO;
import com.jxjtech.yakmanager.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Notice API")
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class NoticeController {

    private final NoticeService noticeService;

    // 공지 리스트
    @GetMapping("/notice")
    @Operation(summary = "공지 리스트")
    public ResponseEntity<?> getNoticeList() {
        return ResponseEntity.ok(noticeService.noticeList());
    }

    // 공지작성
    @PostMapping("/notice")
    @Operation(summary = "공지 작성")
    public ResponseEntity<?> createNotice(@RequestBody NoticeRequestDTO dto) {
        return ResponseEntity.ok(noticeService.create(dto));
    }

    // 공지 카테고리 리스트
    @GetMapping("/notice/{category}")
    @Operation(summary = "카테고리 별 공지리스트")
    public ResponseEntity<?> getNoticeListByCategory(@PathVariable String category) {
        return ResponseEntity.ok(noticeService.noticeListByCategory(category));
    }

    // 공지 수정
    @PutMapping("/notice/{noticeId}")
    @Operation(summary = "공지 수정")
    public ResponseEntity<?> noticeUpdate(@PathVariable Long noticeId, @RequestBody NoticeRequestDTO dto) {
        return ResponseEntity.ok(noticeService.update(noticeId, dto));
    }

    // 공지 조회
    @PostMapping("/notice/{noticeId}")
    @Operation(summary = "공지 Detail")
    public ResponseEntity<?> getNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.detail(noticeId));
    }
}
