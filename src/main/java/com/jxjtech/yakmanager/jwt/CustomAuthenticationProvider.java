package com.jxjtech.yakmanager.jwt;

import com.jxjtech.yakmanager.entity.MemberEntity;
import com.jxjtech.yakmanager.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final MemberRepository memberRepository;

    public CustomAuthenticationProvider(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String memberId = authentication.getName();
        Optional<MemberEntity> optionalMember = memberRepository.findById(Long.valueOf(memberId));
        if (optionalMember.isPresent()) {
            MemberEntity member = optionalMember.get();
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getMemberRole().toString()));
            return new UsernamePasswordAuthenticationToken(memberId, null, authorities);
        } else {
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
