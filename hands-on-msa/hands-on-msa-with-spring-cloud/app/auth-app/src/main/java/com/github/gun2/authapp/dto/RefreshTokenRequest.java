package com.github.gun2.authapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * token 재발급 시 사용되는 요청
 */
@Getter
@ToString
@AllArgsConstructor
public class RefreshTokenRequest {
    /**
     * refresh token
     */
    private final String refreshToken;
}
