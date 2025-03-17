package com.github.gun2.restmodel.status;

/**
 * 응답 코드 정의
 */
public interface ResponseCode {
    int getStatus();
    String getCode();
    String getMessage();
}
