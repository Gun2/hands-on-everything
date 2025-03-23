package com.github.gun2.authapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gun2.restmodel.dto.response.SuccessResponse;
import com.github.gun2.restmodel.status.SuccessCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        ResponseEntity<Void> responseEntity = SuccessResponse.of(null).toResponseEntity(SuccessCode.OK);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(responseEntity.getStatusCode().value());
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        responseEntity.getHeaders().forEach((key, value) -> response.setHeader(key, String.join(",", value)));
        writer.write(objectMapper.writeValueAsString(responseEntity.getBody()));
        writer.flush();
    }
}
