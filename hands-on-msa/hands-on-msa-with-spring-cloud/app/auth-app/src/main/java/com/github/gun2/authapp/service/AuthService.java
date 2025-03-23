package com.github.gun2.authapp.service;


import com.github.gun2.authapp.entity.RefreshToken;
import com.github.gun2.authapp.entity.User;
import com.github.gun2.authapp.exception.AuthenticationFailureException;
import com.github.gun2.authapp.repository.UserRepository;
import com.github.gun2.authapp.security.AccessTokenUtil;
import com.github.gun2.authservice.dto.PassportResponse;
import com.github.gun2.authservice.dto.TokenResponse;
import com.github.gun2.securitymodule.PassportUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AccessTokenBlackListService accessTokenBlackListService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenUtil accessTokenUtil;
    private final RefreshTokenService refreshTokenService;
    private final PassportUtil passportUtil;


    public TokenResponse login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()){
            throw new AuthenticationFailureException();
        }
        if (!passwordEncoder.matches(password, userOptional.get().getPassword())){
            throw new AuthenticationFailureException();
        }
        TokenResponse tokenResponse = creteAccessTokenAndRefreshToken(username);
        return tokenResponse;
    }

    private TokenResponse creteAccessTokenAndRefreshToken(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
        TokenResponse tokenResponse = createTokenResponse(user);
        saveRefreshToken(tokenResponse);
        return tokenResponse;
    }

    private void saveRefreshToken(TokenResponse tokenResponse) {
        refreshTokenService.save(
                accessTokenUtil.extractUsername(tokenResponse.getAccessToken()),
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                accessTokenUtil.getRefreshTokenExpire()
        );
    }

    /**
     * token 응답값 반환
     * @param user
     * @return
     */
    private TokenResponse createTokenResponse(User user) {
        String token = accessTokenUtil.generateToken(user);
        Claims claims = accessTokenUtil.extractClaims(token);
        return TokenResponse.ofBearer(
                token,
                accessTokenUtil.getAccessTokenExpire() / 1000,
                claims.getExpiration().getTime(),
                accessTokenUtil.generateRefreshToken(),
                accessTokenUtil.getRefreshTokenExpire() / 1000
        );
    }

    public void logout(String token) {
        accessTokenBlackListService.logout(token);
        refreshTokenService.removeByAccessToken(token);
    }

    @Transactional
    public TokenResponse refresh(String accessToken, String refreshToken) {
        RefreshToken validate = refreshTokenService.validate(accessToken, refreshToken);
        TokenResponse tokenResponse = creteAccessTokenAndRefreshToken(validate.getUsername());
        expirePreviousToken(accessToken, refreshToken);
        return tokenResponse;
    }

    /**
     * 이전 토큰 정보 만료
     * @param accessToken
     * @param refreshToken
     */
    private void expirePreviousToken(String accessToken, String refreshToken) {
        refreshTokenService.reserveExpireRefreshTokenAfter10Seconds(refreshToken);
        accessTokenBlackListService.isBlackListToken(accessToken);
    }

    public PassportResponse generatePassport(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);

        String passport = passportUtil.generateToken(user.getUsername(), user.getRole());
        return PassportResponse.of(passport, passportUtil.getExpire() / 1000, "passport");
    }
}
