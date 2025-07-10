package com.github.gun2.handsonkafkawithspring.retry.blocking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class BlockingRetryConsumerConfig {

    public static final String CONFIG_BEAN_NAME = "blockingRetryKafkaListenerContainerFactory";

    @Value("${kafka.backoff.interval:1000}")
    private long interval;

    @Value("${kafka.backoff.max_failure:3}")
    private long maxAttempts;

    @Bean
    public DefaultErrorHandler errorHandler() {
        BackOff backOff = new FixedBackOff(interval, maxAttempts);
        return new DefaultErrorHandler((record, ex) -> {
            System.out.println("⚠️ All retries exhausted for: " + record.value());
        }, backOff);
    }

    @Bean(name = CONFIG_BEAN_NAME)
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return factory;
    }
}
