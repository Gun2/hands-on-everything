package com.github.gun2.authservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginSuccessResponse {
    private final String session;
}
