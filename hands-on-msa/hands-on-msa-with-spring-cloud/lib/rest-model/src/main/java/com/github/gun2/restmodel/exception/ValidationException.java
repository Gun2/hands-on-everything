package com.github.gun2.restmodel.exception;


import com.github.gun2.restmodel.dto.response.ValidationErrorResponse;
import lombok.Getter;

import java.util.List;

/**
 * 유효성 검증 실패시 예외
 */
public class ValidationException extends RuntimeException {
    @Getter
    private final List<ValidationErrorResponse.ClientFieldError> fieldErrorList;

    public ValidationException(List<ValidationErrorResponse.ClientFieldError> fieldErrorList) {
        this.fieldErrorList = fieldErrorList;
    }

    public ValidationException(List<ValidationErrorResponse.ClientFieldError> fieldErrorList, String message) {
        super(message);
        this.fieldErrorList = fieldErrorList;
    }
}
