package com.jxjtech.yakmanager.jwt;

import com.jxjtech.yakmanager.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.info("handle");

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "token error");
    }

    public void handle(HttpServletRequest request, HttpServletResponse response, AppException e) throws IOException {
        response.sendError(e.getErrorCode().getHttpStatus().value(), e.getErrorCode().getMessage());
    }
}
