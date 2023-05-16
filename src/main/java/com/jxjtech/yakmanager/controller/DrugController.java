package com.jxjtech.yakmanager.controller;

import com.jxjtech.yakmanager.dto.QRCodeURLDTO;
import com.jxjtech.yakmanager.service.DrugService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Drug API")
@RequiredArgsConstructor
@RequestMapping("/api/drugs")
public class DrugController {

    private final DrugService drugService;

    @GetMapping("/code/{drugCode}")
    @Operation(summary = "약코드 -> 약정보 조회")
    public ResponseEntity<?> drugInfoByDrugCode(@PathVariable int drugCode) {
        return ResponseEntity.ok(drugService.drugInfoByDrugCode(drugCode));
    }

    @PostMapping("/name")
    @Operation(summary = "약이름 -> 약정보 조회")
    public ResponseEntity<?> drugInfoByDrugName(String drugName) {
        return ResponseEntity.ok(drugService.drugInfoByDrugName(drugName));
    }

    @PostMapping("/search")
    @Operation(summary = "의약품 정보 약 검색")
    public ResponseEntity<?> searchDrug(String drugName) {
        return ResponseEntity.ok(drugService.search(drugName));
    }

    @PostMapping("/QRCode")
    @Operation(summary = "QR인식 -> 의약품 상세정보")
    public ResponseEntity<?> drugInfoByQRCode(@RequestBody QRCodeURLDTO dto) {
        return ResponseEntity.ok(drugService.drugInfoByQRCode(dto));
    }

//    @PostMapping("/DBUpdate")
//    @Operation(summary = "의약품 성분 DB 수정")
//    public ResponseEntity<?> dbUpdate() {
//        return ResponseEntity.ok(drugService.dbUpdate());
//    }
//    @PostMapping("/DBUpdateMaterial")
//    @Operation(summary = "의약품 성분 DB 맨끝 | 지우기")
//    public void dbUpdateMaterial() {
//        drugService.MaterialDBUpdate();
//    }
//
//    @PostMapping("/DBUpdate2")
//    @Operation(summary = "의약품 DB 이름수정")
//    public ResponseEntity<?> dbUpdate2() {
//        return ResponseEntity.ok(drugService.dbUpdate2());
//    }
//
//    @PostMapping("/DBUpdate6")
//    @Operation(summary = "포장정보테이블 이름변경")
//    public void dbUpdate6() {
//        drugService.dbUpdate6();
//    }
//
//    @PostMapping("/DBUpdate7")
//    @Operation(summary = "info1에 img 추가")
//    public void dbUpdate7() {
//        drugService.dbUpdate7();
//    }

//    @PostMapping("/DBUpdate8")
//    @Operation(summary = "재고관리 약기록 패키지 \" 바꾸기")
//    public void dbUpdate8() {
//        drugService.dbUpdate8();
//    }
}
