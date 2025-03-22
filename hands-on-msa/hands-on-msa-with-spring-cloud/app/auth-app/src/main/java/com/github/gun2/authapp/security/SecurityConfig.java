package com.github.gun2.authapp.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final AppAuthenticationEntryPoint appAuthenticationEntryPoint;
    private final String LOGIN_URL = "/auth/login";
    private final String LOGOUT_URL = "/auth/logout";

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            LoginSuccessHandler loginSuccessHandler
    ) throws Exception {
        http.authorizeHttpRequests(
                        registry -> registry.anyRequest().authenticated()
                ).addFilterBefore(new JsonToFormUrlEncodedFilter(LOGIN_URL), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(config -> config.authenticationEntryPoint(appAuthenticationEntryPoint))
                .formLogin(config -> config.loginProcessingUrl(LOGIN_URL).permitAll().successHandler(loginSuccessHandler))
                .logout(config -> config.logoutUrl(LOGOUT_URL))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션 필요 시 생성
                        .maximumSessions(1) // 한 계정당 한 세션만 유지
                        .expiredSessionStrategy(event -> log.warn("세션 만료됨: " + event.getSessionInformation().getSessionId())));
                http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
