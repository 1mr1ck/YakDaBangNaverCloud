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

    @GetMapping("/token/refresh")
    @Operation(summary = "토큰 재발급")
    public ResponseEntity<?> tokenReIssue(@RequestHeader(name = "refreshToken") String refreshToken) throws Exception {
        return ResponseEntity.ok(jwtService.reIssue(refreshToken));
    }

    @PostMapping("/check/recentLogin")
    @Operation(summary = "최근 로그인 체크")
    public ResponseEntity<?> checkRecentLogin(@RequestHeader(name = "phoneValue") String phoneValue) {
        return ResponseEntity.ok(authService.recentLogin(phoneValue));
    }

    @PostMapping("/check/token")
    @Operation(summary = "자동 로그인 토큰체크")
    public ResponseEntity<?> rememberMeTokenCheck(@RequestHeader(name = "refreshToken") String refreshToken, @RequestHeader(name = "snsType") String snsType, @RequestHeader(name = "phoneValue") String phoneValue, @RequestHeader(name = "buildVersion") String buildVersion) throws Exception {
        return ResponseEntity.ok(jwtService.rememberMe(refreshToken, phoneValue, buildVersion, snsType));
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
    public boolean checkNickName(@RequestBody IsDuplicateNickNameDTO dto) {
        return authService.isDuplicateNickName(dto);
    }

    @PostMapping("/login/{snsType}")
    @Operation(description = "로그인 API", summary = "로그인")
    public ResponseEntity<?> memberLogin(@PathVariable String snsType, @RequestBody LoginRequestDTO loginRequestDTO, @RequestHeader(name = "osType") String osType, @RequestHeader(name = "phoneValue") String phoneValue, @RequestHeader(name = "buildVersion") String buildVersion) {
        return ResponseEntity.ok(authService.login(snsType, loginRequestDTO, osType, phoneValue, buildVersion));
    }

    @GetMapping("/check/policy")
    @Operation(summary = "개인정보정책 확인 API")
    public ResponseEntity<?> policyCheck(@RequestHeader(name = "refreshToken") String refreshToken) throws Exception {
        return ResponseEntity.ok(authService.policyCheck(refreshToken));
    }

    @PostMapping("/register/policy")
    @Operation(summary = "개인정보정책 등록 API")
    public ResponseEntity<?> registerPolicy(@RequestHeader(name = "refreshToken") String refreshToken, @RequestBody PolicyRegisterDTO policyRegisterDTO) {
        return ResponseEntity.ok(authService.policyRegister(policyRegisterDTO, refreshToken));
    }
}
