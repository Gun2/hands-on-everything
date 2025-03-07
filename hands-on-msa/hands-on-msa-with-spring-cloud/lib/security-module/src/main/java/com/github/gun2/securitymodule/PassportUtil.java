package com.github.gun2.securitymodule;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

/**
 * access token 관련 Util
 */
@Component
public class PassportUtil {

    public static final String HEADER_NAME = "X-Passport-Token";
    private final Key key;
    @Getter
    private final Long expire;

    public PassportUtil(
            PassportProperties passportProperties
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(passportProperties.getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expire = passportProperties.getExpire();
    }
    // JWT 생성
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + this.expire))
                .signWith(key)
                .claim(ClaimNames.ROLE.getValue(), role)
                .compact();
    }

    public String extract(String token, ClaimNames claimName){
        return extractClaims(token).get(claimName.getValue(), String.class);
    }

    // JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // JWT에서 Claims 추출
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT 유효성 검사
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    // 토큰의 유효성 검사
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }


    /**
     * header 내부의 token 반환
     * @param request
     * @return
     */
    public static Optional<String> getTokenFromHeader(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_NAME);
        return Optional.ofNullable(headerValue);
    }
}
