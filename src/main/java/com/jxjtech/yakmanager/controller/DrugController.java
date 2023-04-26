package com.jxjtech.yakmanager.controller;

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
//    @PostMapping("/DBUpdate3")
//    @Operation(summary = "의약품가격DB 의약품코드 추가")
//    public ResponseEntity<?> dbUpdate3() {
//        return ResponseEntity.ok(drugService.dbUpdate3());
//    }
//
//    @PostMapping("/DBUpdate4")
//    @Operation(summary = "의약품가격테이블 이름변경")
//    public ResponseEntity<?> dbUpdate4() {
//        return ResponseEntity.ok(drugService.dbUpdate4());
//    }
//
//    @PostMapping("/DBUpdate5")
//    @Operation(summary = "가격테이블 의약품 DB추가")
//    public void dbUpdate5() {
//        drugService.dbUpdate5();
//    }
//
//    @PostMapping("/DBUpdate6")
//    @Operation(summary = "포장정보테이블 이름변경")
//    public void dbUpdate6() {
//        drugService.dbUpdate6();
//    }
}