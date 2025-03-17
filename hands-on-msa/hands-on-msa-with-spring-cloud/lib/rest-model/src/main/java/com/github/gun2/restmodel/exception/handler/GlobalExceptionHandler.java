package com.github.gun2.restmodel.exception.handler;


import com.github.gun2.restmodel.dto.response.ErrorResponse;
import com.github.gun2.restmodel.dto.response.ValidationErrorResponse;
import com.github.gun2.restmodel.exception.ValidationException;
import com.github.gun2.restmodel.status.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@Slf4j
public class GlobalExceptionHandler {

    /**
     * <b>{@link org.springframework.web.bind.annotation.RequestBody}로 받은 값 중 Validation 실패 시 발생</b>
     * @param e {@link MethodArgumentNotValidException}
     * @return {@link ResponseEntity}
     * @see org.springframework.validation.annotation.Validated
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        return ValidationErrorResponse.of(e.getFieldErrors()).toResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
    }

    /**
     * <b>{@link org.springframework.web.bind.annotation.ModelAttribute}로 받은 값 중 validation 실패 시 발생</b>
     * @param e {@link BindException}
     * @return {@link ResponseEntity}
     * @see org.springframework.validation.annotation.Validated
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ValidationErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        return ValidationErrorResponse.of(e.getFieldErrors()).toResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
    }

    /**
     * 비즈니스 로직 처리간 수동으로 발생된 유효성 검증 에러
     * @param e
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<ValidationErrorResponse> handleValidationException(ValidationException e) {
        log.error("handleValidationException", e);
        return ValidationErrorResponse.of(e.getFieldErrorList()).toResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
    }

    /**
     * <b>접근 권한이 없는 경우 발생</b>
     * @param e {@link AccessDeniedException}
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        return ErrorResponse.ofEmpty().toResponseEntity(ErrorCode.ACCESS_DENIED);
    }


    /**
     * <b>요청한 값을 읽을 수 없는 경우 발생 ex => JSON format이 옳바르지 않은경우</b>
     * @param e {@link HttpMessageNotReadableException}
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        log.error("handleHttpMessageNotReadableException", e);
        return ErrorResponse.ofEmpty().toResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
    }


    /**
     * 정의되지 않은 예외가 발생한 경우
     * @param e {@link Exception}
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e){
        log.error("handleException {}", e);
        return ErrorResponse.ofEmpty().toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
