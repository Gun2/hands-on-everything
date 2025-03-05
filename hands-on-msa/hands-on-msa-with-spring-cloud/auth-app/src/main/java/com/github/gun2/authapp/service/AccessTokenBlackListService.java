package com.github.gun2.authapp.service;

import com.github.gun2.authapp.entity.AccessTokenBlackList;
import com.github.gun2.authapp.repository.AccessTokenBlackListRepository;
import com.github.gun2.authapp.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

/**
 * 로그아웃 관리 서비스
 */
@Service
public class AccessTokenBlackListService {
    private final AccessTokenBlackListRepository accessTokenBlackListRepository;
    private final MessageDigest sha265MessageDigest;
    private final JwtUtil jwtUtil;

    public AccessTokenBlackListService(AccessTokenBlackListRepository accessTokenBlackListRepository, JwtUtil jwtUtil) {
        this.accessTokenBlackListRepository = accessTokenBlackListRepository;
        this.jwtUtil = jwtUtil;
        try {
            this.sha265MessageDigest =  MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * token이 blacklist에 등록된 상태인지 확인
     * @param token
     * @return
     */
    public boolean isBlackListToken(String token){
        return accessTokenBlackListRepository.findByAccessTokenHash(hash(token)).isPresent();
    }

    public void logout(String token) {
        Claims claims = jwtUtil.extractClaims(token);
        accessTokenBlackListRepository.save(AccessTokenBlackList.builder()
                        .accessTokenHash(hash(token))
                        .createdAt(Instant.now())
                        .expiresAt(claims.getExpiration().toInstant())
                        .reason(AccessTokenBlackList.Reason.LOGOUT)
                .build());
    }

    public String hash(String str){
        byte[] digest = sha265MessageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : digest) {
            stringBuffer.append(String.format("%02x", b));
        }
        return stringBuffer.toString();
    }
}
