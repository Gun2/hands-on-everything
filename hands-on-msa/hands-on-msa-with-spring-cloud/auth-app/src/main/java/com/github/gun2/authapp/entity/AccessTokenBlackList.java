package com.github.gun2.authapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AccessTokenBlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * accessToken hash값
     */
    private String accessTokenHash;
    /**
     * 만료 시간 (해당 시간 이후 row 삭제 가능)
     */
    private Instant expiresAt;
    /**
     * 등록시간
     */
    private Instant createdAt;

    /**
     * 등록 사유
     */
    private Reason reason;

    public enum Reason {
        LOGOUT
    }
}
