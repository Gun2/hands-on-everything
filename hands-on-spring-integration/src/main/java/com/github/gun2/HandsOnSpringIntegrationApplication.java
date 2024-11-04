package com.github.gun2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HandsOnSpringIntegrationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(HandsOnSpringIntegrationApplication.class);
    }

}
