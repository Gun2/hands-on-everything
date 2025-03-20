package com.github.gun2.authapp.service;

import com.github.gun2.authapp.entity.RefreshToken;
import com.github.gun2.authapp.exception.TokenValidationFailureException;
import com.github.gun2.authapp.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
            throw new TokenValidationFailureException();
        }
        return refreshTokenOptional.get();

    }

    /**
     * 10초 뒤 만료 시키기
     * @param refreshToken
     */
    public void reserveExpireRefreshTokenAfter10Seconds(String refreshToken) {
        //10초 후 만료 예약
        final int reserveAfterExpiredTime = 10;
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findById(refreshToken);
        refreshTokenOptional.ifPresent(data -> {

            //10초 간격보다 만료일자가 넘어갈 경우에만 업데이트
            Duration duration = Duration.between(Instant.now(), data.getExpiredAt());
            long diffSecond = duration.getSeconds();
            if (diffSecond > reserveAfterExpiredTime){
                data.updateExpiredAt(Instant.now().plus(reserveAfterExpiredTime, ChronoUnit.SECONDS));
            }
            refreshTokenRepository.save(data);
        });
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
