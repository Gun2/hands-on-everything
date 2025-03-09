package com.github.gun2.authservice.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * passport 응답값
 */
@Getter
@Builder
public class PassportResponse {
    private final String passport;
    private final Long expiresIn;
    private final String tokenType;

    public static PassportResponse of(String passport, Long expiresIn, String tokenType){
        return PassportResponse.builder()
                .passport(passport)
                .expiresIn(expiresIn)
                .tokenType(tokenType)
                .build();
    }
}
