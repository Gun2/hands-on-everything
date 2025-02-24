package com.github.gun2.authapp.security;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 로그아웃 관리 서비스
 */
@Service
public class LogoutService {
    private final Set<String> logoutTokenSet = new HashSet<>();

    /**
     * token이 로그아웃 된 상태인지 확인
     * @param token
     * @return
     */
    public boolean isLogoutToken(String token){
        return logoutTokenSet.contains(token);
    }

    public void logout(String token) {
        logoutTokenSet.add(token);
    }
}
