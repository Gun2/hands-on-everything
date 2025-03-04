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
    private final Long accessTokenExpiresIn;
    private final Long refreshTokenExpiresIn;
    private final String tokenType;
    private final String refreshToken;

    public static TokenResponse ofBearer(String accessToken, Long accessTokenExpiresIn, String refreshToken, Long refreshTokenExpiresIn){
        return TokenResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn)
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn)
                .tokenType("Bearer")
                .build();
    }
}
