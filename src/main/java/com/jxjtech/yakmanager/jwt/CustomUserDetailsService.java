package com.jxjtech.yakmanager.jwt;

import com.jxjtech.yakmanager.entity.MemberEntity;
import com.jxjtech.yakmanager.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    /** UserDetailsService implement,
     * @Service 어노테이션을 사용해서 자동으로 설정 Config 작성해야 하는 것은 없다.
    */
    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String memberEmail) throws UsernameNotFoundException {
        return memberRepository.findByMemberEmail(memberEmail)
                .map(this::createUserDetails).orElseThrow(() -> new UsernameNotFoundException(memberEmail + " 을 DB 찾을 수 없습니다"));
    }
    private UserDetails createUserDetails(MemberEntity member) {
        log.info("CustomUserDetailsService");
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getMemberRole().toString());

        return new User(
                String.valueOf(member.getMemberId()),
                "",
                Collections.singleton(grantedAuthority)
        );
    }

}
