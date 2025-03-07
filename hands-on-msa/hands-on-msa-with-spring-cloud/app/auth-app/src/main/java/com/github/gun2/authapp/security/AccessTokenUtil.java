package com.github.gun2.authapp.security;

import com.github.gun2.authapp.entity.User;
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
import java.util.UUID;

/**
 * access token 관련 Util
 */
@Component
public class AccessTokenUtil {

    public static final String HEADER_NAME = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";

    private final Key key;
    @Getter
    private final Long accessTokenExpire;
    @Getter
    private final Long refreshTokenExpire;

    public AccessTokenUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token.expire}") Long accessTokenExpire,
            @Value("${jwt.refresh-token.expire}") Long refreshTokenExpire
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpire = accessTokenExpire;
        this.refreshTokenExpire = refreshTokenExpire;
    }
    // JWT 생성
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + this.accessTokenExpire))
                .signWith(key)
                .claim("role", user.getRole())
                .compact();
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
        String headerValue = request.getHeader(AccessTokenUtil.HEADER_NAME);
        if (headerValue == null){
            return Optional.empty();
        }
        if (headerValue.startsWith(TOKEN_PREFIX)){
            return Optional.of(headerValue.substring(TOKEN_PREFIX.length() + 1));
        }
        return Optional.empty();
    }

    /**
     * refresh token 생성
     * @return
     */
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();  // 무작위 값 생성
    }

}
