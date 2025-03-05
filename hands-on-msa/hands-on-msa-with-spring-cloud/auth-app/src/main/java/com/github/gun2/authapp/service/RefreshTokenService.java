package com.github.gun2.authapp.service;

import com.github.gun2.authapp.entity.RefreshToken;
import com.github.gun2.authapp.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MessageDigest sha265MessageDigest;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        try {
            this.sha265MessageDigest =  MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(String username, String accessToken, String refreshToken, Long refreshTokenExpiresIn) {
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .username(username)
                        .accessTokenHash(hash(accessToken))
                        .refreshToken(refreshToken)
                        .expiredAt(Instant.now().plusMillis(refreshTokenExpiresIn))
                        .build()
        );
    }

    public String hash(String str){
        byte[] digest = sha265MessageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : digest) {
            stringBuffer.append(String.format("%02x", b));
        }
        return stringBuffer.toString();
    }

    /**
     * 유효성 검증에 성공하면 token 정보 반환
     * @param accessToken
     * @param refreshToken
     * @return
     */
    public RefreshToken validate(String accessToken, String refreshToken) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByRefreshTokenAndAccessTokenHashAndExpiredAtGreaterThan(refreshToken, hash(accessToken), Instant.now());
        if (refreshTokenOptional.isEmpty()){
            log.error("failed refresh validation");
            throw new BadCredentialsException("""
                    refresh token : %s is invalid value
                    """.formatted(refreshToken));
        }
        return refreshTokenOptional.get();

    }

    public void removeRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }

    /**
     * access token에 해당하는 refresh token 제거
     * @param token
     */
    @Transactional
    public void removeByAccessToken(String token) {
        refreshTokenRepository.deleteByAccessTokenHash(hash(token));
    }
}
