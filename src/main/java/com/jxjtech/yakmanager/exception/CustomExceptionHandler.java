package com.jxjtech.yakmanager.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(AppException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(AppException e) {
        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }
}
