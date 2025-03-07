package com.github.gun2.securitymodule;

import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PassportProperties {
    private final String secret;
    private final Long expire;

    public PassportProperties(Environment environment) {
        this.secret = environment.getProperty("app.security.passport.secret", String.class);
        this.expire = environment.getProperty("app.security.passport.expire", Long.class);
    }
}
