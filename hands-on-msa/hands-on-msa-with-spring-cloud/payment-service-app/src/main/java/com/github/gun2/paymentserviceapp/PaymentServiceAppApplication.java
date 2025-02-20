package com.github.gun2.paymentserviceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RequestMapping("/payments")
public class PaymentServiceAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceAppApplication.class, args);
    }



    @GetMapping("/{orderId}")
    public String processPayment(@PathVariable String orderId) {
        return "Payment processed for Order ID: " + orderId;
    }
}
