package com.jxjtech.yakmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequestDTO {

    @Schema(example = "test@naver.com")
    @NotNull(message = "이메일을 입력 해 주세요.")
    private String memberEmail;
}
