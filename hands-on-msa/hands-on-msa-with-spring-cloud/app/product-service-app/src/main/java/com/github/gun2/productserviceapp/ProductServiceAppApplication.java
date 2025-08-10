package com.github.gun2.productserviceapp;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@RequiredArgsConstructor
public class ProductServiceAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceAppApplication.class, args);
    }

}
