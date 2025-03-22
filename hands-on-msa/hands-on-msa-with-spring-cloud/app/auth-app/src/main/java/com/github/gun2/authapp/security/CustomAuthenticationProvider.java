package com.github.gun2.authapp.security;

import com.github.gun2.authservice.dto.LoginRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final LocalValidatorFactoryBean localValidatorFactoryBean;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("authentication", authentication);
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        validate(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(passwordEncoder.matches(password, userDetails.getPassword())){
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        }
        return null;
    }

    /**
     * 유효성 검사
     * @param authentication
     */
    private void validate(Authentication authentication) {
        Validator validator = localValidatorFactoryBean.getValidator();
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        Set<ConstraintViolation<LoginRequest>> validate = validator.validate(LoginRequest.builder()
                .username(username)
                .password(password)
                .build());
        if(!validate.isEmpty()){
            throw new IllegalArgumentException("로그인 요청값 유효성 검증에 실패하였습니다.");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
