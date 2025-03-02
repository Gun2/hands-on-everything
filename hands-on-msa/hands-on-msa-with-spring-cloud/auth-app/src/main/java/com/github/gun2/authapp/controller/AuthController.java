package com.github.gun2.authapp.controller;

import com.github.gun2.authapp.dto.LoginRequest;
import com.github.gun2.authapp.dto.TokenResponse;
import com.github.gun2.authapp.security.JwtUtil;
import com.github.gun2.authapp.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        Optional<String> tokenOptional = JwtUtil.getTokenFromHeader(request);
        tokenOptional.ifPresent(authService::logout);
        return ResponseEntity.ok().build();
    }
}
