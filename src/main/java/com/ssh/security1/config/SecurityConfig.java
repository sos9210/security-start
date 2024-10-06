package com.ssh.security1.config;

import com.ssh.security1.config.auth.PrincipalDetailsService;
import com.ssh.security1.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록
@EnableMethodSecurity(securedEnabled = true,    // @Secured 활성화
                        prePostEnabled = true //   @PreAuthorize, @PostAuthorize 활성화
                        )
public class SecurityConfig {
    private final PrincipalOauth2UserService principalOauth2UserService;
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PrincipalDetailsService principalDetailsService) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
        .authorizeRequests(auth ->
                auth
                .requestMatchers("/user/**").authenticated()
                .requestMatchers("/manager/**").access("hasRole('ROLE_MANAGER' or hasRole('ROLE_ADMIN'))")
                .requestMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
        );
        http.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/"));

        http.oauth2Login(oauth ->
                oauth.loginPage("/login")
                //구글 로그인 후 후처리
                //1. 코드받기(인증) 2. 액세스토큰(권한), 3.사용자프로필정보
                //4-1. 자동 회원가입진행
                        .userInfoEndpoint(c -> c.userService(principalOauth2UserService))
        );
        return http.build();

    }
}
