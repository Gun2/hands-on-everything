package com.github.gun2.apigatewayapp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@ConfigurationProperties(prefix = "app.gateway")
@AllArgsConstructor
public class AppProperties {
    private final List<String> permittedPaths;
}
