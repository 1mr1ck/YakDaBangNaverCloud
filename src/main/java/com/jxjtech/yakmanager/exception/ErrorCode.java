package com.jxjtech.yakmanager.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."), // 토큰 재발급을 위한 에러코드
    INVALID_JWT_TOKEN(HttpStatus.FORBIDDEN, "권한 정보가 없는 토큰입니다."), // 잘못된 토큰을 들고왓을 때
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 정보의 사용자를 찾을 수 없습니다."),
    NOT_EQUAL_SNS_TYPE(HttpStatus.NOT_FOUND, "다른 소셜로 가입하신 기록이 있습니다."),
    NOT_EXIST_DATA(HttpStatus.NOT_FOUND, "데이터가 존재하지 않습니다."),
    NOT_BELONG_TO_PHARMACY(HttpStatus.NOT_FOUND, "약국 소속이 아닙니다."),
    NOT_ADMIN(HttpStatus.NOT_FOUND, "약국의 주인이 아닙니다."),
    NOT_CONTENT(HttpStatus.NOT_FOUND, "내용을 입력해 주세요.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
