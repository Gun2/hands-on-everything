package com.github.gun2.eurekaserver.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ApiAuthProcessingFilter apiAuthProcessingFilter) throws Exception {
        http.authorizeHttpRequests(registry -> {
            registry.requestMatchers("/").permitAll()
                    .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                    .anyRequest().authenticated();
        });
        http.addFilterBefore(apiAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class);
        http.csrf(AbstractHttpConfigurer::disable).sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/eureka/css/**",
                        "/eureka/js/**",
                        "/eureka/fonts/**"
                );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
