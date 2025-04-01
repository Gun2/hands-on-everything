package com.github.gun2.handsonspringactuator;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class HandsOnSpringActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandsOnSpringActuatorApplication.class, args);
    }

}
