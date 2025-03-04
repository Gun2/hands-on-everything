package com.github.gun2.authapp.repository;

import com.github.gun2.authapp.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByRefreshTokenAndAccessTokenHashAndExpiredAtGreaterThan(String refreshToken, String hash, Instant instant);
}
