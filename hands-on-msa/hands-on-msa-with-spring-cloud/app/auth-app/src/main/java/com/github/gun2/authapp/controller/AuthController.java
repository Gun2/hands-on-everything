package com.github.gun2.authapp.controller;

import com.github.gun2.authapp.service.AuthService;
import com.github.gun2.authservice.dto.PassportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/passport")
    public ResponseEntity<PassportResponse> passport(
            Authentication authentication
    )
    {
        PassportResponse passportResponse = authService.generatePassport(authentication.getName());
        return ResponseEntity.ok(passportResponse);
    }
}
