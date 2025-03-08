package com.github.gun2.securitymodule;

import java.util.Optional;

public class AccessTokeExtractor {
    /**
     * header값 내부의 token 반환
     * @param headerValue 헤더값
     * @return
     */
    public static Optional<String> getAccessTokenFromHeaderValue(String headerValue) {
        if (headerValue.startsWith(AccessTokenInfo.TOKEN_PREFIX)){
            return Optional.of(headerValue.substring(AccessTokenInfo.TOKEN_PREFIX.length() + 1));
        }
        return Optional.empty();
    }

}
