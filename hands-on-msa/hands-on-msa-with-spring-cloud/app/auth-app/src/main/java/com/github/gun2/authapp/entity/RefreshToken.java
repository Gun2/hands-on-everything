package com.github.gun2.authapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String refreshToken;
    @Column(nullable = false)
    private String accessTokenHash;
    @Column(nullable = false)
    private Instant expiredAt;
    private String username;

    public void updateExpiredAt(Instant expiredAt){
        this.expiredAt = expiredAt;
    }
}
