package com.github.gun2.restmodel.dto.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErrorResponse<T> extends BaseResponse{
    protected T error;

    protected ErrorResponse() {
    }

    protected ErrorResponse(T error){
        this.error = error;
    }

    public static ErrorResponse ofEmpty(){
        return new ErrorResponse();
    }

    public static <T> ErrorResponse<T> of(T error){
        return new ErrorResponse<>(error);
    }
}
