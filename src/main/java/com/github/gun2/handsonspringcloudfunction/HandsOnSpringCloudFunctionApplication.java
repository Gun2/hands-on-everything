package com.github.gun2.handsonspringcloudfunction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SpringBootApplication
public class HandsOnSpringCloudFunctionApplication {

    private final AtomicLong count = new AtomicLong();
    public static void main(String[] args) {
        SpringApplication.run(HandsOnSpringCloudFunctionApplication.class, args);
    }

    /**
     * 문자열 뒤집어 반환
     * @return
     */
    @Bean
    public Function<String, String> reverseString() {
        return value -> new StringBuilder(value).reverse().toString();
    }

    /**
     * data 값 만큼 count 값 증가
     * @return
     */
    @Bean
    public Consumer<Integer> increaseCount(){
        return count::getAndAdd;
    }

    /**
     * 현재 count값 반환
     * @return
     */
    @Bean
    public Supplier<String> getCount(){
        return count::toString;
    }


}
