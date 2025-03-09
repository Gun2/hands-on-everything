package com.github.gun2.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class LoginRequest {
    private final String username;
    private final String password;
}
