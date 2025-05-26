package com.github.gun2.handsonkafkawithspring;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Slf4j
public class KafkaConfig {
    @Bean
    NewTopic newTopic(){
        return TopicBuilder.name(Topics.CHAT_TOPIC)
                .partitions(2)
                .replicas(1)
                .build();
    }
}
