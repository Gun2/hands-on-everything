package com.github.gun2.externalevenadapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableConfigurationProperties(ExternalEventAdapterProperties.class)
@RequiredArgsConstructor
public class EventAdapterAutoConfiguration {
    private final ObjectMapper objectMapper;
    private final ExternalEventAdapterProperties properties;
    private final ApplicationEventPublisher eventPublisher;
    private final Environment environment;

    @Bean
    @ConditionalOnProperty(prefix = "external-event-adapter", name = "enabled", havingValue = "true", matchIfMissing = true)
    public EventAdapter eventAdapter() {
        return new EventAdapter(
                objectMapper,
                eventPublisher,
                properties,
                environment.getProperty("spring.application.name").toString()
        );
    }
}