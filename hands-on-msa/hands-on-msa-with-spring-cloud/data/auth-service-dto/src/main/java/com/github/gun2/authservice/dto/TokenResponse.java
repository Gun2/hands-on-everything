package com.github.gun2.authservice.dto;

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
    //만료되는 시점
    private final Long accessTokenExpiresAt;
    private final Long refreshTokenExpiresIn;
    private final String tokenType;
    private final String refreshToken;

    public static TokenResponse ofBearer(String accessToken, Long accessTokenExpiresIn, Long accessTokenExpiresAt, String refreshToken, Long refreshTokenExpiresIn){
        return TokenResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn)
                .tokenType("Bearer")
                .build();
    }
}
