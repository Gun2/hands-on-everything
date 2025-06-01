package com.github.gun2.handsonkafkawithspring.kafka.admin.topic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KafkaTopicCreateRequest {
    private final String topicName;
    private final int numPartitions;
    private final short replicationFactor;
}
