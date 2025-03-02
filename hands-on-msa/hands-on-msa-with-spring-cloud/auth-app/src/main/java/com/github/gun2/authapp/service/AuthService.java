package com.github.gun2.authapp.service;


import com.github.gun2.authapp.dto.TokenResponse;
import com.github.gun2.authapp.repository.UserRepository;
import com.github.gun2.authapp.security.JwtUtil;
import com.github.gun2.authapp.security.LogoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final LogoutService logoutService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public TokenResponse login(String username, String password) {
        UserDetails userDetails = userRepository.findByUsername(username);
        if (userDetails == null){
            throw new UsernameNotFoundException("""
                    username : %s is not found
                    """.formatted(username)
            );
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("""
                    password for username : %s is not matched
                    """.formatted(username));
        }
        String token = jwtUtil.generateToken(username);
        return TokenResponse.ofBearer(
                token,
                jwtUtil.getAccessTokenExpire() / 1000
        );
    }

    public void logout(String token) {
        logoutService.logout(token);
    }
}
