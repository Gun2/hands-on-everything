package com.github.gun2.authapp.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 계정 저장소
 */
@Repository
public class UserRepository {
    private final Map<String, UserDetails> users = new HashMap<>();

    public UserRepository() {
        // 사용자 3명 추가 (테스트 용도)
        users.put("user1", User.withUsername("user1")
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                .roles("USER")
                .build());
        users.put("user2", User.withUsername("user2")
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                .password("{noop}password2")
                .roles("USER")
                .build());
        users.put("user3", User.withUsername("user3")
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                .password("{noop}password3")
                .roles("USER")
                .build());
    }

    public UserDetails findByUsername(String username) {
        return users.get(username);
    }
}
