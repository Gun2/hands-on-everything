package com.github.gun2.orderserviceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderServiceAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceAppApplication.class, args);
    }
}
