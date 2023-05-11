package com.jxjtech.yakmanager.controller;

import com.jxjtech.yakmanager.dto.GetPackageInfoDTO;
import com.jxjtech.yakmanager.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class CSVController {

    private final FileService fileService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("otp") String otp) throws IOException {
        fileService.saveFile(file, otp);
        return "redirect:/csvUpload";
    }
}
