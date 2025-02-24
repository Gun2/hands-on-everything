package com.github.gun2.authapp.security;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LogoutService logoutService;

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil){
        return new JwtAuthenticationFilter(jwtUtil, logoutService);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter, JwtUtil jwtUtil) throws Exception {
        http.authorizeHttpRequests(
                        registry -> registry.requestMatchers("/login").permitAll()
                                .anyRequest().authenticated()
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(
                        config -> config.loginProcessingUrl("/login")
                                .successHandler((request, response, authentication) -> {
                                    String token = jwtUtil.generateToken(authentication.getPrincipal().toString());
                                    Cookie cookie = new Cookie(JwtUtil.COOKIE_NAME, token);
                                    cookie.setHttpOnly(true);
                                    cookie.setSecure(true);
                                    cookie.setPath("/");
                                    cookie.setAttribute("SameSite", "Strict"); // CSRF 방지
                                    response.addCookie(cookie);
                                })
                ).logout(config -> {
                    config.addLogoutHandler((request, response, authentication) -> {
                        Optional<Cookie> cookieOptional = JwtUtil.getTokenFromCookie(request);
                        cookieOptional.ifPresent(cookie -> logoutService.logout(cookie.getValue()));

                    });
                })
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(config -> config.disable());
        return http.build();
    }
}
