package com.github.gun2.authapp.exception;

import com.github.gun2.restmodel.dto.response.ErrorResponse;
import com.github.gun2.restmodel.exception.handler.GlobalExceptionHandler;
import com.github.gun2.restmodel.status.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AppControllerAdvice extends GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationFailureException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationFailureException(AuthenticationFailureException e) {
        log.error("handleAuthenticationFailureException", e);
        return ErrorResponse.ofEmpty().toResponseEntity(ErrorCode.AUTHORIZATION_FAILED);
    }
}
