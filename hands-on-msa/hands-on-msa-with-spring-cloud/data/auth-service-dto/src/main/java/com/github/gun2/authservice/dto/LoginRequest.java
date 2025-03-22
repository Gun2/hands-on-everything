package com.github.gun2.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank
    private final String username;
    @NotBlank
    private final String password;
}
