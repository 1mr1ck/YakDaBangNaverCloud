package com.jxjtech.yakmanager.config;

import com.jxjtech.yakmanager.jwt.JwtAccessDeniedHandler;
import com.jxjtech.yakmanager.jwt.JwtFilter;
import com.jxjtech.yakmanager.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Override
    public void configure(HttpSecurity httpSecurity) {
        log.info("JwtConfigure");
        JwtFilter customFilter = new JwtFilter(tokenProvider, jwtAccessDeniedHandler);

        httpSecurity.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
