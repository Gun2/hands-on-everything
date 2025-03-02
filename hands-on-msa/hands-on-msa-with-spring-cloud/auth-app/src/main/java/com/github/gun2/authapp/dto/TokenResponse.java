package com.github.gun2.authapp.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 인증 성공 시 반환하는 응답값
 */
@Getter
@Builder
public class TokenResponse {
    private final String accessToken;
    private final Long expiresIn;
    private final String tokenType;

    public static TokenResponse ofBearer(String accessToken, Long expiresIn){
        return TokenResponse.builder()
                .accessToken(accessToken)
                .expiresIn(expiresIn)
                .tokenType("Bearer")
                .build();
    }
}
