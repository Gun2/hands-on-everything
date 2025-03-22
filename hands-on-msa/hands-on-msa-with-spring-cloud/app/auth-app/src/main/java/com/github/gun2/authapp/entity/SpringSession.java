package com.github.gun2.authapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SPRING_SESSION")
public class SpringSession {
    @Id
    @Size(max = 36)
    @Column(name = "PRIMARY_ID", nullable = false, length = 36)
    private String primaryId;

    @Size(max = 36)
    @NotNull
    @Column(name = "SESSION_ID", nullable = false, length = 36)
    private String sessionId;

    @NotNull
    @Column(name = "CREATION_TIME", nullable = false)
    private Long creationTime;

    @NotNull
    @Column(name = "LAST_ACCESS_TIME", nullable = false)
    private Long lastAccessTime;

    @NotNull
    @Column(name = "MAX_INACTIVE_INTERVAL", nullable = false)
    private Integer maxInactiveInterval;

    @NotNull
    @Column(name = "EXPIRY_TIME", nullable = false)
    private Long expiryTime;

    @Size(max = 100)
    @Column(name = "PRINCIPAL_NAME", length = 100)
    private String principalName;
}