package com.github.gun2.authapp.resolver;

import com.github.gun2.authapp.security.AccessTokenUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * request header에 있는 AccessToken값을 인자값으로 가져오기 위한 어노테이션
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessToken {
    String headerName() default AccessTokenUtil.HEADER_NAME;
    String tokenType() default AccessTokenUtil.TOKEN_PREFIX;
}
