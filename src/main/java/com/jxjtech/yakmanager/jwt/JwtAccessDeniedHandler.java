package com.jxjtech.yakmanager.jwt;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jxjtech.yakmanager.exception.AppException;
import com.jxjtech.yakmanager.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ThreadLocal<String> responseBody = new ThreadLocal<>();
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.info("handle");

        String body = "token error";

        response.sendError(HttpServletResponse.SC_FORBIDDEN, body);
        responseBody.set(body);
    }

    public void handle(HttpServletRequest request, HttpServletResponse response, AppException e) throws IOException {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("status", String.valueOf(e.getErrorCode().getHttpStatus().value()));
        body.put("error", e.getErrorCode().getHttpStatus().name());
        body.put("message", e.getErrorCode().getMessage());
        body.put("path", request.getServletPath());

        response.sendError(e.getErrorCode().getHttpStatus().value(), body.get("message"));
        responseBody.set(body.toString());
    }

    public String getResponseBody() {
        return responseBody.get();
    }
}
