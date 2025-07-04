package com.github.gun2.websocketapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WebsocketAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketAppApplication.class, args);
    }

}
