package com.github.gun2.eurekaserver.config;

import com.github.gun2.eurekaserver.domain.User;
import com.github.gun2.eurekaserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApiAuthenticationManager implements AuthenticationManager {

    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String apiKey = authentication.getPrincipal().toString();
        Optional<User> byApiKey = userRepository.findByApiKey(apiKey);
        if (byApiKey.isPresent()){
            return new ApiKeyAuthenticationToken(apiKey, Collections.emptySet());
        }
        return null;
    }


}
