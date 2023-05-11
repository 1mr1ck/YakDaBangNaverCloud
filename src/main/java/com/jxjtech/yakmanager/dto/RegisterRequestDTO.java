package com.jxjtech.yakmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @Schema(example = "test@naver.com")
    private String memberEmail;
    @Schema(example = "test")
    private String memberNickName;

}
