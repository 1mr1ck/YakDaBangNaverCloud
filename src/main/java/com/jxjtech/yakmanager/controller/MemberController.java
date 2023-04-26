package com.jxjtech.yakmanager.controller;

import com.jxjtech.yakmanager.dto.MemberUpdateDTO;
import com.jxjtech.yakmanager.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    @Operation(summary = "회원정보 조회 API")
    public ResponseEntity<?> memberInfo() {
        return ResponseEntity.ok(memberService.getInfo());
    }

    @PutMapping("/me")
    @Operation(summary = "회원정보 수정 API")
    public ResponseEntity<?> memberUpdate(@RequestBody MemberUpdateDTO memberUpdateDTO) {
        return ResponseEntity.ok(memberService.update(memberUpdateDTO));
    }

    @DeleteMapping("/me")
    @Operation(summary = "회원탈퇴 API")
    public ResponseEntity<?> memberDelete() {
        return ResponseEntity.ok(memberService.delete());
    }
}
