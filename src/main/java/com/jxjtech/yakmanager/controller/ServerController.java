package com.jxjtech.yakmanager.controller;

import com.jxjtech.yakmanager.service.ServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/server")
@Tag(name = "Server API")
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;

    @GetMapping("/versionCheck/{versionCode}/android")
    @Operation(summary = "안드로이드 어플 버전체크")
    public boolean androidVersionCheck(@PathVariable int versionCode) {
        int version = 0;
        if(versionCode == version) {
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/versionCheck/{versionCode}/iOS")
    @Operation(summary = "안드로이드 어플 버전체크")
    public boolean iOSVersionCheck(@PathVariable int versionCode) {
        int version = 0;
        if(versionCode == version) {
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/updateCheck")
    @Operation(summary = "업데이트 체크")
    public boolean serverUpdateCheck() {
        return true;
    }

    @PostMapping("/serverStatus")
    @Operation(summary = "서버상태")
    public ResponseEntity<?> serverStatus() {
        return ResponseEntity.ok(serverService.getServerStatus());
    }
}
