package com.github.gun2.apigatewayapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = {AppProperties.class})
@EnableDiscoveryClient
public class ApiGatewayAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayAppApplication.class, args);
	}

}
