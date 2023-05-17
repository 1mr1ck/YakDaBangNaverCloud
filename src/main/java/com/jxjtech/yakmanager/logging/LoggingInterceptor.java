package com.jxjtech.yakmanager.logging;

import com.jxjtech.yakmanager.jwt.JwtAccessDeniedHandler;
import com.jxjtech.yakmanager.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    private final TokenProvider tokenProvider;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        //시간을 가져온다
        long currentTime = System.currentTimeMillis();
        //현재시간을 모델에 넣는다.
        request.setAttribute("bTime", currentTime);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        // 현재 시간을 구한다
        long currentTime = System.currentTimeMillis();

        // 요청이 시작된 시간을 가져온다
        long beginTime = (long) request.getAttribute("bTime");

        // 현재 시간 - 요청이 시작된 시간 = 총 처리시간을 구한다
        long processedTime = currentTime - beginTime;

        String jwt = resolveToken(request);
        String memberId = "";

        if (!tokenProvider.validateTokenLogInterceptor(jwt)) {
            jwt = null;
        }

        String logStr = "{Time: " + ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ", path: " + request.getServletPath() + ", requestType: " + request.getMethod() + ", requestTime: " + processedTime + "ms, Client IP: " + request.getRemoteAddr() + ", status: " + response.getStatus();
        if (jwt != null && tokenProvider.validateToken(jwt)) {
            memberId = tokenProvider.accessParseClaims(jwt).getSubject();
            logStr += ", memberId: " + memberId;
        }
        String responseBody = getResponseBody(getResponseWrapper(response));
        if(responseBody.startsWith("<!DOCTYPE html>")) {
            responseBody = null;
        }
        if(!request.getServletPath().contains("/v3/api-docs") && !request.getServletPath().contains("/swagger-ui")) {
            logStr += ", ResponseBody: " + responseBody + "}";
        }

        logger.info(logStr);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        log.info("로그 : " + bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getResponseBody(ContentCachingResponseWrapper responseWrapper) {
        return new String(responseWrapper.getContentAsByteArray());
    }

    private ContentCachingResponseWrapper getResponseWrapper(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        }
        return new ContentCachingResponseWrapper(response);
    }
}
