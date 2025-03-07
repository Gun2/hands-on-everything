package com.github.gun2.securitymodule;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@EnablePassportUtil
@Import({PassportAuthenticationFilter.class, PassportSecurityConfig.class})
public @interface EnablePassportSecurityConfig {
}
