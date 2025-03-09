package com.github.gun2.orderserviceapp;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderServiceAppApplication {
    private final RestClient restClient;
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceAppApplication.class, args);
    }


    @GetMapping("/{orderId}")
    public String getOrder(@PathVariable String orderId) {

        String paymentResponse = restClient.get().uri("http://PAYMENT-SERVICE/payments/" + orderId).retrieve().body(String.class);//restTemplate.getForObject("http://PAYMENT-SERVICE/payments/" + orderId, String.class);
        return "Order ID: " + orderId + ", Payment: " + paymentResponse;
    }

}
