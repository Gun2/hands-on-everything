package com.github.gun2.authapp.repository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 계정 저장소
 */
@Repository
public class UserRepository {
    private final Map<String, UserDetails> users = new HashMap<>();

    public UserRepository(PasswordEncoder passwordEncoder) {
        // 사용자 3명 추가 (테스트 용도)
        users.put("user1", User.withUsername("user1")
                .password(passwordEncoder.encode("password1"))
                .roles("USER")
                .build());
        users.put("user2", User.withUsername("user2")
                .password(passwordEncoder.encode("password2"))
                .roles("USER")
                .build());
        users.put("user3", User.withUsername("user3")
                .password(passwordEncoder.encode("password3"))
                .roles("USER")
                .build());
    }

    public UserDetails findByUsername(String username) {
        return users.get(username);
    }
}
