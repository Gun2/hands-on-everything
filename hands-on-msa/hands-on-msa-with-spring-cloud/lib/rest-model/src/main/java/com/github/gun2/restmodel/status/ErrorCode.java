package com.github.gun2.restmodel.status;

import lombok.Getter;

/**
 * 에러 코드 정의
 */
@Getter
public enum ErrorCode implements ResponseCode {
    INVALID_INPUT_VALUE(400, "INVALID_INPUT_VALUE", "Invalid Input Value"),
    //인증 실패
    AUTHORIZATION_FAILED(401, "AUTHORIZATION_FAILED", "Authorization failed"),
    ACCESS_DENIED(403 ,"ACCESS_DENIED", "Access is Denied"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "Server Error");
    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }


    }
