package com.jxjtech.yakmanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jxjtech.yakmanager.dto.*;
import com.jxjtech.yakmanager.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

@Tag(name = "Inventory API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

//    @PostMapping("/drugRecord/DBUpdate")
//    @Operation(summary = "서버이전 약기록 디비 수정")
//    public ResponseEntity<?> drugRecordDBUpdate() throws JsonProcessingException {
//        return ResponseEntity.ok(inventoryService.DBUpdate());
//    }

    @GetMapping("/drugs/{drugId}")
    @Operation(summary = "약기록 상세정보 조회")
    public  ResponseEntity<?> getDrugRecord(@PathVariable Long drugId) throws JsonProcessingException {
        return ResponseEntity.ok(inventoryService.getDrugRecord(drugId));
    }

    @PutMapping("/drugs/{drugId}")
    @Operation(summary = "약기록 수정")
    public ResponseEntity<?> modifyDrugRecord(@PathVariable Long drugId, @RequestBody DrugRecordRequestDTO drugRecordRequestDTO) throws JsonProcessingException {
        return ResponseEntity.ok(inventoryService.modifyDrugRecord(drugId, drugRecordRequestDTO));
    }

    @DeleteMapping("/drugs/{drugId}")
    @Operation(summary = "의약품 기록 삭제")
    public ResponseEntity<?> deleteDrugRecord(@PathVariable Long drugId) {
        return ResponseEntity.ok(inventoryService.deleteDrugRecord(drugId));
    }

    @PostMapping("/drugs/package")
    @Operation(summary = "의약품 포장정보 조회")
    public ResponseEntity<?> getDrugPackageInfo(@RequestBody DrugPackageInfoRequestDTO drugPackageInfoRequestDTO) {
        return ResponseEntity.ok(inventoryService.getPackageInfo(drugPackageInfoRequestDTO));
    }

    @PostMapping("/drugs/search")
    @Operation(summary = "재고관리 약 검색")
    public ResponseEntity<?> drugSearchByDrugName(@RequestBody DrugSearchByKeyWordDTO dto) {
        return ResponseEntity.ok(inventoryService.drugSearCh(dto));
    }

    @GetMapping("/pharmacy")
    @Operation(summary = "약국 리스트")
    public ResponseEntity<?> getPharmacyList() {
        return ResponseEntity.ok(inventoryService.pharmacyList());
    }

    @PostMapping("/pharmacy")
    @Operation(summary = "약국 생성")
    public ResponseEntity<?> createPharmacy(@RequestBody PharmacyRequestDTO pharmacyRequestDTO) throws Exception {
        return ResponseEntity.ok(inventoryService.createPharmacy(pharmacyRequestDTO));
    }

    @PutMapping("/pharmacy/{pharmacyId}")
    @Operation(summary = "약국명 변경")
    public ResponseEntity<?> updatePharmacy(@RequestBody PharmacyRequestDTO pharmacyRequestDTO, @PathVariable Long pharmacyId) throws Exception {
        return ResponseEntity.ok(inventoryService.updatePharmacy(pharmacyRequestDTO, pharmacyId));
    }

    @DeleteMapping("/pharmacy/{pharmacyId}")
    @Operation(summary = "약국 삭제")
    public ResponseEntity<?> deletePharmacy(@PathVariable Long pharmacyId) throws Exception {
        return ResponseEntity.ok(inventoryService.deletePharmacy(pharmacyId));
    }

    @PostMapping("/pharmacy/{pharmacyId}/excel")
    @Operation(summary = "약국 데이터 엑셀")
    public ResponseEntity<?> exportPharmacyExcel(@PathVariable Long pharmacyId, @RequestParam String email) throws MessagingException, IOException {
        return ResponseEntity.ok(inventoryService.exportPharmacy(pharmacyId, email));
    }

    @GetMapping("/pharmacy/{pharmacyId}/invitationCode")
    @Operation(summary = "초대코드생성")
    public ResponseEntity<?> createInvitationCode(@PathVariable Long pharmacyId) {
        return ResponseEntity.ok(inventoryService.createInvitationCode(pharmacyId));
    }

    @GetMapping("/pharmacy/{pharmacyId}/members")
    @Operation(summary = "약국 멤버 조회")
    public ResponseEntity<?> getPharmacyMemberList(@PathVariable Long pharmacyId) {
        return ResponseEntity.ok(inventoryService.pharmacyMemberList(pharmacyId));
    }

    @DeleteMapping("/pharmacy/{pharmacyId}/members")
    @Operation(summary = "약국 멤버 탈퇴")
    public ResponseEntity<?> deletePharmacyMember(@PathVariable Long pharmacyId) throws Exception {
        return ResponseEntity.ok(inventoryService.deletePharmacyMember(pharmacyId));
    }

    @GetMapping("/pharmacy/{pharmacyId}/title")
    @Operation(summary = "타이틀 리스트")
    public ResponseEntity<?> getTitleList(@PathVariable Long pharmacyId) {
        return ResponseEntity.ok(inventoryService.titleList(pharmacyId));
    }

    @PostMapping("/pharmacy/{pharmacyId}/title")
    @Operation(summary = "타이틀 생성")
    public ResponseEntity<?> createTitle(@PathVariable Long pharmacyId, @RequestBody TitleRequestDTO titleRequestDTO) {
        return ResponseEntity.ok(inventoryService.createTitle(pharmacyId, titleRequestDTO));
    }

    @DeleteMapping("/pharmacy/{pharmacyId}/title")
    @Operation(summary = "타이틀 전체 삭제")
    public ResponseEntity<?> deleteTitleAll(@PathVariable Long pharmacyId) {
        return ResponseEntity.ok(inventoryService.deleteTitleAll(pharmacyId));
    }

    @PostMapping("/pharmacy/members/{invitationCode}")
    @Operation(summary = "초대코드 입력 -> 약국 가입")
    public ResponseEntity<?> joinPharmacy(@PathVariable String invitationCode) throws Exception {
        return ResponseEntity.ok(inventoryService.joinPharmacy(invitationCode));
    }

    @DeleteMapping("/pharmacy/members/{pharmacyMemberId}")
    @Operation(summary = "약국 멤버 추방")
    public ResponseEntity<?> expelMemberInPharmacy(@PathVariable Long pharmacyMemberId) {
        return ResponseEntity.ok(inventoryService.expel(pharmacyMemberId));
    }

    @PutMapping("/title/{titleId}")
    @Operation(summary = "타이틀 이름 수정")
    public ResponseEntity<?> updateTitle(@PathVariable Long titleId, @RequestBody TitleRequestDTO dto) {
        return ResponseEntity.ok(inventoryService.updateTitle(titleId, dto));
    }

    @DeleteMapping("/title/{titleId}")
    @Operation(summary = "타이틀 삭제")
    public ResponseEntity<?> deleteTitle(@PathVariable Long titleId) {
        return ResponseEntity.ok(inventoryService.deleteTitle(titleId));
    }

    @GetMapping("/title/{titleId}/drugs")
    @Operation(summary = "타이틀 내의 약기록 리스트 조회")
    public ResponseEntity<?> getDrugRecordListInTitle(@PathVariable Long titleId) throws JsonProcessingException {
        return ResponseEntity.ok(inventoryService.getDrugList(titleId));
    }

    @PostMapping("/title/{titleId}/drugs")
    @Operation(summary = "재고관리 약기록 생성")
    public ResponseEntity<?> createDrugRecord(@PathVariable Long titleId, @RequestBody DrugRecordRequestDTO drugRecordRequestDTO) throws JsonProcessingException {
        return ResponseEntity.ok(inventoryService.createDrugRecord(titleId, drugRecordRequestDTO));
    }

    @GetMapping("/title/{titleId}/drugs/{drugCode}")
    @Operation(summary = "중복 의약품 체크")
    public ResponseEntity<?> IsDuplicateDrugRecord(@PathVariable Long titleId, @PathVariable int drugCode) {
        return ResponseEntity.ok(inventoryService.duplDrugRecord(titleId, drugCode));
    }

    @PostMapping("/title/{titleId}/excel")
    @Operation(summary = "타이틀 엑셀")
    public ResponseEntity<?> exportTitleExcel(@PathVariable Long titleId, @RequestParam String email) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(inventoryService.exportTitle(titleId, email));
    }

    @PostMapping("/narcotic/OTP")
    @Operation(summary = "마약류 의약품 등록 OTP")
    public ResponseEntity<?> getNarcoticOTP() {
        return ResponseEntity.ok(inventoryService.getOTP());
    }

    @PostMapping("/narcotic/title")
    @Operation(summary = "마약류 의약품 타이틀 GET")
    public ResponseEntity<?> getNarcoticTitle() {
        return ResponseEntity.ok(inventoryService.getNarcoticTitle());
    }

    @PostMapping("/narcotic/drugRecord/{titleId}")
    @Operation(summary = "마약류 의약품 기록 GET")
    public ResponseEntity<?> getNarcoticRecord(@PathVariable Long titleId) {
        return ResponseEntity.ok(inventoryService.getNarcoticDrugRecord(titleId));
    }

    @PostMapping("/narcotic/drugRecord/Inspection/{drugRecordId}")
    @Operation(summary = "마약류 의약품 재고검사")
    public ResponseEntity<?> drugRecordInspection(@PathVariable Long drugRecordId, @RequestBody NarcoticInspectionDTO dto) {
        return ResponseEntity.ok(inventoryService.narcoticInspection(drugRecordId, dto));
    }

    @DeleteMapping("/narcotic/title/delete/{titleId}")
    @Operation(summary = "마약류 의약품 타이틀 DELETE")
    public boolean narcoticTitleDelete(@PathVariable Long titleId) {
        return inventoryService.deleteNarcoticTitle(titleId);
    }

    @PostMapping("/narcotic/drugRecord/checkChange/{drugRecordId}")
    @Operation(summary = "마약류 의약품 체크박스 변경")
    public boolean narcoticDrugRecordCheckChange(@PathVariable Long drugRecordId) {
        return inventoryService.checkChange(drugRecordId);
    }
}
