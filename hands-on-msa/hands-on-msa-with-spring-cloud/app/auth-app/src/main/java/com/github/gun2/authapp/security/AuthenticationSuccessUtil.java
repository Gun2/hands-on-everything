package com.github.gun2.authapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gun2.authservice.dto.LoginSuccessResponse;
import com.github.gun2.restmodel.dto.response.SuccessResponse;
import com.github.gun2.restmodel.status.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 인증 성공 처리 유틸
 */
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessUtil {
    private final ObjectMapper objectMapper;

    /**
     * 일증 성공 시 응답값 처리
     * @param request
     * @param response
     * @throws IOException
     */
    public void writeAuthenticationSuccessResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        ResponseEntity<LoginSuccessResponse> responseEntity = SuccessResponse.of(
                LoginSuccessResponse.builder().session(
                        Base64.getEncoder().encodeToString(request.getSession().getId().getBytes())
                ).build()
        ).toResponseEntity(SuccessCode.OK);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(responseEntity.getStatusCode().value());
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        responseEntity.getHeaders().forEach((key, value) -> response.setHeader(key, String.join(",", value)));
        writer.write(objectMapper.writeValueAsString(responseEntity.getBody()));
        writer.flush();
    }
}