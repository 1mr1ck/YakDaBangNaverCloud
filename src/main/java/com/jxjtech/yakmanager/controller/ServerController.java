package com.jxjtech.yakmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server")
@Tag(name = "Server API")
public class ServerController {

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
}
