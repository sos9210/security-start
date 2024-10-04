package com.ssh.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록
@EnableMethodSecurity(securedEnabled = true,    // @Secured 활성화
                        prePostEnabled = true //   @PreAuthorize, @PostAuthorize 활성화
                        )
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
        .authorizeRequests(auth ->
                auth
                .requestMatchers("/user/**").authenticated()
                .requestMatchers("/manager/**").access("hasRole('ROLE_MANAGER' or hasRole('ROLE_ADMIN'))")
                .requestMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
        );
        http.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/"));
        return http.build();

    }
}
