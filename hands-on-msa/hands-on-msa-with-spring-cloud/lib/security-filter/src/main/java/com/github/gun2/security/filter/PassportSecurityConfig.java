package com.github.gun2.security.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class PassportSecurityConfig {

    @Value("${app.security.permitted-matcher:}")
    private String permittedMatcher;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            PassportAuthenticationFilter passportAuthenticationFilter
    ) throws Exception {
        if (this.permittedMatcher != null && !this.permittedMatcher.isEmpty()){
            http.authorizeHttpRequests(
                    registry -> registry.requestMatchers(permittedMatcher).permitAll().anyRequest().authenticated()
            );
        }else {
            http.authorizeHttpRequests(
                    registry -> registry.anyRequest().authenticated()
            );
        }
        http.addFilterBefore(passportAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
