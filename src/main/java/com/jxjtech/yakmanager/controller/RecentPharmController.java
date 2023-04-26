package com.jxjtech.yakmanager.controller;

import com.jxjtech.yakmanager.dto.ImgUrlDTO;
import com.jxjtech.yakmanager.service.RecentPharmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/img")
@Tag(name = "RecentPharm API")
public class RecentPharmController {

    private final RecentPharmService recentPharmService;

    @PostMapping("/getList")
    @Operation(summary = "이미지 얻기")
    public ResponseEntity<?> getImgUrlList() {
        return ResponseEntity.ok(recentPharmService.imgUrlList());
    }

    @PostMapping("/createImg")
    @Operation(summary = "이미지 저장")
    public ResponseEntity<?> createImg(@RequestBody ImgUrlDTO dto) {
        return ResponseEntity.ok(recentPharmService.create(dto));
    }

    @DeleteMapping("/deleteImgBox/{img_url_id}")
    @Operation(summary = "미리보기 이미지 삭제")
    public ResponseEntity<?> deleteImg(@PathVariable Long img_url_id) {
        return ResponseEntity.ok(recentPharmService.delete(img_url_id));
    }
}
