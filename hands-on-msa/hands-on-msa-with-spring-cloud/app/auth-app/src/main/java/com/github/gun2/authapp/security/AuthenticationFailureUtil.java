package com.github.gun2.authapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gun2.restmodel.dto.response.ErrorResponse;
import com.github.gun2.restmodel.status.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 인증 실패 처리 유틸
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFailureUtil {
    private final ObjectMapper objectMapper;

    /**
     * 일증 실패 시 응답값 처리
     * @param response
     * @throws IOException
     */
    public void writeAuthenticationErrorResponse(HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        ResponseEntity responseEntity = ErrorResponse.ofEmpty().toResponseEntity(ErrorCode.AUTHORIZATION_FAILED);
        response.setStatus(responseEntity.getStatusCode().value());
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        responseEntity.getHeaders().forEach((key, value) -> response.setHeader(key, String.join(",", value)));
        writer.write(objectMapper.writeValueAsString(responseEntity.getBody()));
        writer.flush();
    }
}