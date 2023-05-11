package com.jxjtech.yakmanager.logging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxjtech.yakmanager.exception.AppException;
import com.jxjtech.yakmanager.exception.ErrorCode;
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

@Slf4j
@RequiredArgsConstructor
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    private final TokenProvider tokenProvider;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


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

        // View를 리턴하기 직전에 실행됨
        // 현재 시간을 구한다
        long currentTime = System.currentTimeMillis();

        // 요청이 시작된 시간을 가져온다
        long beginTime = (long) request.getAttribute("bTime");

        // 현재 시간 - 요청이 시작된 시간 = 총 처리시간을 구한다
        long processedTime = currentTime - beginTime;

        String jwt = resolveToken(request);
        String memberId = "";

        String jwtFilterBody = jwtAccessDeniedHandler.getResponseBody();

//        if (!tokenProvider.validateToken(jwt)) {
//            jwt = null;
//        }

        try {
            if(tokenProvider.validateToken(jwt)) {
                memberId = tokenProvider.accessParseClaims(jwt).getSubject();
            }
        } catch (Exception e) {
            jwt = null;
        }
        String logStr = "{path: " + request.getServletPath() + ", requestType: " + request.getMethod() + ", requestTime: " + processedTime + "ms, Client IP: " + request.getRemoteAddr() + ", status: " + response.getStatus();
        if (jwt != null) {
            memberId = tokenProvider.accessParseClaims(jwt).getSubject();
            logStr += ", memberId: " + memberId;
        }

        if (!request.getServletPath().equals("/error") || !request.getServletPath().startsWith("/v3/api-docs")) {
            String responseBody = getResponseBody(getResponseWrapper(response));
            if (jwtFilterBody != null) {
                logStr += ", ResponseBody: " + jwtFilterBody + "}";
            } else if(response.getStatus() != 200){
                logStr += ", ResponseBody: " + responseBody + "}";
            } else {
                logStr += "}";
            }
            logger.info(logStr);
        } else {
            logStr += "}";
            logger.info(logStr);
        }

    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
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
