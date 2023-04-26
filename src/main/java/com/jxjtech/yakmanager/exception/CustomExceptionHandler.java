package com.jxjtech.yakmanager.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(AppException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(AppException e) {
        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }
}