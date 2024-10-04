package com.ssh.security1.config.auth;

import com.ssh.security1.model.User;
import com.ssh.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 시큐리티 설정(SecurityConfig) 에서 loginProcessingUrl 등록한 url요청이 들어오면
 UserDetailsService 타입으로 Bean 등록되어있는 loadUserByUsername 메서드 실행
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // username 파라미터명과 변수명이 일치하거나
    // SecurityConfig 에서 usernameParameter 설정을 해줘야함.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new PrincipalDetails(user);
        }

        return null;
    }
}
