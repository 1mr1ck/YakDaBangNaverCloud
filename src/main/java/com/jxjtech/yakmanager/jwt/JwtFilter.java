package com.jxjtech.yakmanager.jwt;

import com.jxjtech.yakmanager.exception.AppException;
import com.jxjtech.yakmanager.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private static final String[] PERMIT_ALL = {
            "/api/auth/check", "/api/auth/login", "/api/auth/register/", "/swagger-ui", "/v3", "/index", "/img", "/favicon", "/api/board/notice",
            "/api/auth/token", "/policy"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean pathStart = false;
        log.info("test");
        try {
            String path = request.getServletPath();
            log.info(request.getRemoteAddr() + " : " + request.getServletPath());
            for (String p : PERMIT_ALL) {
                if (path.startsWith(p)) {
                    pathStart = path.startsWith(p);
                    break;
                }
            }
            if (pathStart) {
                filterChain.doFilter(request, response);
            } else {
                String jwt = resolveToken(request);
                if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                    Authentication authentication = tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    filterChain.doFilter(request, response);
                } else {
                    SecurityContextHolder.clearContext();
                    jwtAccessDeniedHandler.handle(request, response, new AppException(ErrorCode.INVALID_JWT_TOKEN)); // 403
                }
            }
        } catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            jwtAccessDeniedHandler.handle(request, response, new AppException(ErrorCode.EXPIRED_JWT_TOKEN)); // 401
        }
    }

    private String resolveToken(HttpServletRequest request) {
        log.info("resolveToken");
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
