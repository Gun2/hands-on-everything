package com.github.gun2.authapp.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class InMemoryUserDetailsService implements UserDetailsService {

    private final Map<String, UserDetails> users = new HashMap<>();

    public InMemoryUserDetailsService(PasswordEncoder passwordEncoder) {
        // 사용자 3명 추가 (테스트 용도)
        users.put("user1", User.withUsername("user1")
                .password(passwordEncoder.encode("password1"))
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return copy(user);
    }

    private static UserDetails copy(UserDetails user) {
        return User.withUsername(user.getUsername()).password(user.getPassword()).authorities(user.getAuthorities()).build();
    }
}
