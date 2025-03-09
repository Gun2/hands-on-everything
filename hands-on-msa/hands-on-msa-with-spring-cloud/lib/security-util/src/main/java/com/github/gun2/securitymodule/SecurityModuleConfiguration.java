package com.github.gun2.securitymodule;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityModuleConfiguration {
    @Bean
    @ConditionalOnProperty(value = "app.security.passport.util", havingValue = "true", matchIfMissing = true)
    PassportUtil passportUtil(PassportProperties passportProperties){
        return new PassportUtil(passportProperties);
    }
}
