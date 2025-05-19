package com.github.gun2.websocketapp;


import com.github.gun2.securitymodule.ClaimNames;
import com.github.gun2.securitymodule.PassportUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

/**
 * WebSocket 연결 시 JWT 인증 처리
 */
@Component
@RequiredArgsConstructor
public class PassportHandshakeInterceptor implements HandshakeInterceptor {
    private final PassportUtil passportUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String passport = request.getHeaders().getFirst("sec-websocket-protocol");
        if (passport != null && !passportUtil.isTokenExpired(passport)) {
            String role = passportUtil.extract(passport, ClaimNames.ROLE);
            String username = passportUtil.extractUsername(passport);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, passport, List.of(new SimpleGrantedAuthority("ROLE_"+role)));
            attributes.put("authentication",authentication);
            return true;
        }
        response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}