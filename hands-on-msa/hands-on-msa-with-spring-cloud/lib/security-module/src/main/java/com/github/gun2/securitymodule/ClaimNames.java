package com.github.gun2.securitymodule;

import lombok.Getter;

public enum ClaimNames {
    ROLE("role");
    @Getter
    private final String value;

    ClaimNames(String value) {
        this.value = value;
    }
}
