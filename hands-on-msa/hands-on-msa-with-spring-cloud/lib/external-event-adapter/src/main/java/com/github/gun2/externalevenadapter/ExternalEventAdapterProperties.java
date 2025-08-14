package com.github.gun2.externalevenadapter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "external-event-adapter")
@Getter
@Setter
public class ExternalEventAdapterProperties {
    /**
     * Adapter 활성화 여부
     */
    private boolean enabled = true;
}