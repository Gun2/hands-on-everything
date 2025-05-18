package com.github.gun2.websocketapp;


import com.github.gun2.securitymodule.PassportUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * WebSocket 연결 시 JWT 인증 처리
 */
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    private final PassportUtil passportUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String passport = request.getHeaders().getFirst("sec-websocket-protocol");
        if (passport != null && !passportUtil.isTokenExpired(passport)) {
            attributes.put("passport", passport);
            return true;
        }
        response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}