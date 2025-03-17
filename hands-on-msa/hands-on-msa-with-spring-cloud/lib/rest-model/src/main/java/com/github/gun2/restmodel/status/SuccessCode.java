package com.github.gun2.restmodel.status;

import lombok.Getter;

/**
 * 성공 코드 정의
 */
@Getter
public enum SuccessCode implements ResponseCode{
    OK(200, "OK", "Success"),
    CREATED(201, "CREATED", "Created");

    private final int status;
    private final String code;
    private final String message;

    SuccessCode(int status, String code, String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
