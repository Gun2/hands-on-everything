package com.github.gun2.authserviceclient.exception;

import com.github.gun2.restmodel.dto.response.ErrorResponse;
import com.github.gun2.restmodel.status.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@ControllerAdvice
public class AuthServiceClientExceptionHandler {

    @ExceptionHandler(WebClientResponseException.class)
    protected ResponseEntity<ErrorResponse> handleWebClientResponseException(WebClientResponseException e) {
        log.error("handleWebClientResponseException", e);
        try {
            ErrorResponse errorResponse = e.getResponseBodyAs(ErrorResponse.class);
            //에러 내용 유지하여 전파
            return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
        }catch (Exception exception){
            log.error("handleWebClientResponseException error : {}", exception);
            return ErrorResponse.ofEmpty().toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
