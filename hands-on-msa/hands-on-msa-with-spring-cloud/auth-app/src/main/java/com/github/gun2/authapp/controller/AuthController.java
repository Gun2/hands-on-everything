package com.github.gun2.authapp.controller;

import com.github.gun2.authapp.dto.LoginRequest;
import com.github.gun2.authapp.dto.RefreshTokenRequest;
import com.github.gun2.authapp.dto.TokenResponse;
import com.github.gun2.authapp.resolver.JwtToken;
import com.github.gun2.authapp.security.JwtUtil;
import com.github.gun2.authapp.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody LoginRequest loginRequest
            ){
        TokenResponse tokenResponse = authService.login(loginRequest.getUsername(), loginRequest.getPassword());

        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @JwtToken String jwtToken
    ) {
        authService.logout(jwtToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @RequestBody RefreshTokenRequest request,
            @JwtToken String jwtToken
    ){
        TokenResponse tokenResponse = authService.refresh(jwtToken, request.getRefreshToken());

        return ResponseEntity.ok(tokenResponse);

    }
}
