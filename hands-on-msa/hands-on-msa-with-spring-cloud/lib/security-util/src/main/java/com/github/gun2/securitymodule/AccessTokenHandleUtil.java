package com.github.gun2.securitymodule;

import java.util.Optional;

/**
 * access token을 다루는데 필요한 메서드가 정의된 클래스
 */
public class AccessTokenHandleUtil {
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

    /**
     * access token의 헤더에 들어갈 값을 정의하는 메서드
     * @param accessToken
     * @return
     */
    public static String createHeaderValueOfAccessToken(String accessToken){
        return AccessTokenInfo.TOKEN_PREFIX + " " + accessToken;
    }

}
