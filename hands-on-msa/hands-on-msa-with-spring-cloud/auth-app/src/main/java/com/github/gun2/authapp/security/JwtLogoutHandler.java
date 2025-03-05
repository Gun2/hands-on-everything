package com.github.gun2.authapp.security;

import com.github.gun2.authapp.service.AccessTokenBlackListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {
    private final AccessTokenBlackListService accessTokenBlackListService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> tokenOptional = JwtUtil.getTokenFromHeader(request);
        tokenOptional.ifPresent(accessTokenBlackListService::logout);
    }
}
