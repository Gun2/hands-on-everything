package com.github.gun2.authapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonToFormUrlEncodedFilter implements Filter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String LOGIN_URL;

    public JsonToFormUrlEncodedFilter(String LOGIN_URL) {
        this.LOGIN_URL = LOGIN_URL;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            // 1. Content-Type이 application/json인지 확인
            boolean isJson = ContentType.APPLICATION_JSON.getMimeType().equalsIgnoreCase(httpRequest.getContentType());

            // 2. 요청 URL이 로그인 URL인지 확인
            boolean isLoginRequest = LOGIN_URL.equalsIgnoreCase(httpRequest.getServletPath());

            // 3. 두 조건을 만족할 때만 실행
            if (isJson && isLoginRequest) {
                try (ServletInputStream inputStream = httpRequest.getInputStream()) {
                    Map<String, String> credentials = objectMapper.readValue(inputStream, Map.class);
                    String username = credentials.getOrDefault("username", "");
                    String password = credentials.getOrDefault("password", "");

                    // 기존 UsernamePasswordAuthenticationFilter가 읽을 수 있도록 request attribute에 저장
                    httpRequest.setAttribute("username", username);
                    httpRequest.setAttribute("password", password);

                    // HttpServletRequestWrapper 사용하여 getParameter() 오버라이드
                    HttpServletRequest wrappedRequest = new CustomHttpServletRequestWrapper(httpRequest, username, password);
                    chain.doFilter(wrappedRequest, response);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    // 내부 클래스로 HttpServletRequestWrapper 정의
    private static class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final Map<String, String> params = new HashMap<>();

        public CustomHttpServletRequestWrapper(HttpServletRequest request, String username, String password) {
            super(request);
            params.put("username", username);
            params.put("password", password);
        }

        @Override
        public String getParameter(String name) {
            return params.getOrDefault(name, super.getParameter(name));
        }
    }
}
