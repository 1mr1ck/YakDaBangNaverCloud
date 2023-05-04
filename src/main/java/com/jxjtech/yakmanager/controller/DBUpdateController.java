package com.jxjtech.yakmanager.controller;

import com.jxjtech.yakmanager.service.DBUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/DBUpdate")
@RequiredArgsConstructor
public class DBUpdateController {

    private final DBUpdateService dbUpdateService;

    @PostMapping("/drugPrice")
    @Operation(summary = "약가격테이블 최신으로 업데이트")
    public void drugPriceUpdate() {
        dbUpdateService.drugPrice();
    }


}
