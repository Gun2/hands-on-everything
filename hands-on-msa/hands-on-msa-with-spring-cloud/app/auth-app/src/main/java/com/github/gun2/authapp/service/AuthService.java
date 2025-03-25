package com.github.gun2.authapp.service;


import com.github.gun2.authapp.entity.User;
import com.github.gun2.authapp.repository.UserRepository;
import com.github.gun2.authservice.dto.PassportResponse;
import com.github.gun2.securitymodule.PassportUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PassportUtil passportUtil;

    public PassportResponse generatePassport(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);

        String passport = passportUtil.generateToken(user.getUsername(), user.getRole());
        return PassportResponse.of(passport, passportUtil.getExpire() / 1000, "passport");
    }
}
