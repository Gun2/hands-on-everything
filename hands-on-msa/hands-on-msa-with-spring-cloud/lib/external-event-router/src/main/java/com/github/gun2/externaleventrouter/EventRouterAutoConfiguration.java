package com.github.gun2.externaleventrouter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableConfigurationProperties(EventRouterProperties.class)
@RequiredArgsConstructor
public class EventRouterAutoConfiguration {

    private final ObjectMapper objectMapper;
    private final EventRouterProperties properties;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final Environment environment;

    @Bean
    @ConditionalOnProperty(prefix = "event-router", name = "enabled", havingValue = "true", matchIfMissing = true)
    public EventRouter eventRouter() {
        return new EventRouter(
                objectMapper,
                eventPublisher,
                kafkaTemplate,
                properties,
                environment.getProperty("spring.application.name").toString()
        );
    }
}