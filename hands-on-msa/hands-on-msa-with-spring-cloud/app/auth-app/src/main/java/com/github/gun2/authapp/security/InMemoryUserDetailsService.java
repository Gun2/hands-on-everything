package com.github.gun2.authapp.security;

import com.github.gun2.authapp.entity.User;
import com.github.gun2.authapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.github.gun2.authapp.converter.UserDataConverter.entityToUserDetails;

@Service
@RequiredArgsConstructor
public class InMemoryUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return entityToUserDetails(userOptional.get());
    }
}
