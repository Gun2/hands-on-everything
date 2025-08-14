package com.github.gun2.externaleventrouter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "event-router")
@Getter
@Setter
public class EventRouterProperties {
    private boolean enabled = true;
}