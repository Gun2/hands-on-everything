package com.github.gun2.authapp.service;


import com.github.gun2.authapp.dto.TokenResponse;
import com.github.gun2.authapp.entity.RefreshToken;
import com.github.gun2.authapp.entity.User;
import com.github.gun2.authapp.repository.UserRepository;
import com.github.gun2.authapp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;


    public TokenResponse login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()){
            throw new UsernameNotFoundException("""
                    username : %s is not found
                    """.formatted(username)
            );
        }
        if (!passwordEncoder.matches(password, userOptional.get().getPassword())){
            throw new BadCredentialsException("""
                    password for username : %s is not matched
                    """.formatted(username));
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
                jwtUtil.extractUsername(tokenResponse.getAccessToken()),
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                jwtUtil.getRefreshTokenExpire()
        );
    }

    /**
     * token 응답값 반환
     * @param user
     * @return
     */
    private TokenResponse createTokenResponse(User user) {
        String token = jwtUtil.generateToken(user);
        return TokenResponse.ofBearer(
                token,
                jwtUtil.getAccessTokenExpire() / 1000,
                jwtUtil.generateRefreshToken(),
                jwtUtil.getRefreshTokenExpire() / 1000
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
        refreshTokenService.removeRefreshToken(refreshToken);
        accessTokenBlackListService.isBlackListToken(accessToken);
    }
}
