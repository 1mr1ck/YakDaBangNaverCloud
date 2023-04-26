package com.jxjtech.yakmanager.controller;

import com.jxjtech.yakmanager.dto.*;
import com.jxjtech.yakmanager.service.AuthService;
import com.jxjtech.yakmanager.service.JWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth API")
public class AuthController {

    private final AuthService authService;
    private final JWTService jwtService;

    @PostMapping("/token/reIssue")
    @Operation(summary = "토큰 재발급")
    public ResponseEntity<?> tokenReIssue(@RequestBody TokenReIssueRequestDTO dto) throws Exception {
        return ResponseEntity.ok(jwtService.reIssue(dto));
    }

    @PostMapping("/token/rememberMe")
    @Operation(summary = "자동 로그인 토큰체크")
    public ResponseEntity<?> rememberMeTokenCheck(@RequestBody RememberMeRequestDTO dto) throws Exception {
        return ResponseEntity.ok(jwtService.rememberMe(dto));
    }

    @PostMapping("/register/{snsType}")
    @Operation(description = "회원가입", summary = "회원가입")
    public ResponseEntity<?> memberRegister(@PathVariable String snsType, @RequestBody RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity.ok(authService.register(snsType, registerRequestDTO));
    }

    @PostMapping("/check/email")
    @Operation(description = "이메일 중복 체크", summary = "이메일 중복 체크")
    public boolean checkEmail(@RequestBody IsDuplicateEmailDTO isDuplicateEmailDTO) {
        return authService.isDuplicateEmail(isDuplicateEmailDTO);
    }

    @PostMapping("/check/nickName")
    @Operation(description = "닉네임 중복 체크", summary = "닉네임 중복 체크")
    public boolean checkNickName(@RequestBody IsDuplicateNickNameDTO IsDuplicateNickNameDTO) {
        return authService.isDuplicateNickName(IsDuplicateNickNameDTO);
    }

    @PostMapping("/login/{snsType}")
    @Operation(description = "로그인 API", summary = "로그인")
    public ResponseEntity<?> memberLogin(@PathVariable String snsType, @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(snsType, loginRequestDTO));
    }

    @PostMapping("/check/policy")
    @Operation(summary = "개인정보정책 확인 API")
    public ResponseEntity<?> policyCheck(@RequestBody JWTRequestDTO dto) throws Exception {
        return ResponseEntity.ok(authService.policyCheck(dto));
    }

    @PostMapping("/register/policy")
    @Operation(summary = "개인정보정책 등록 API")
    public ResponseEntity<?> registerPolicy(@RequestBody PolicyRegisterDTO policyRegisterDTO) {
        return ResponseEntity.ok(authService.policyRegister(policyRegisterDTO));
    }
}
