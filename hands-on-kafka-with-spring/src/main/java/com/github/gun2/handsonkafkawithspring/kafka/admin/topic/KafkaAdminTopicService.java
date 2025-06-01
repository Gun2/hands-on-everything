package com.github.gun2.handsonkafkawithspring.kafka.admin.topic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaAdminTopicService {
    private final KafkaAdmin kafkaAdmin;

    public List<KafkaTopic> findAll() throws ExecutionException, InterruptedException {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            Set<String> topicNames = adminClient.listTopics(new ListTopicsOptions().listInternal(false)).names().get();
            DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(topicNames);

            KafkaFuture<Map<String, TopicDescription>> mapKafkaFuture = describeTopicsResult.allTopicNames();
            Map<String, TopicDescription> stringTopicDescriptionMap = mapKafkaFuture.get();
            return stringTopicDescriptionMap.values().stream().map(KafkaTopic::new).toList();
        }
    }



    public void create(String topicName, int numPartitions, short replicationFactor) throws ExecutionException, InterruptedException {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {

            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);

            CreateTopicsResult result = adminClient.createTopics(Collections.singletonList(newTopic));
            KafkaFuture<Void> future = result.values().get(topicName);

            future.get();
        }
    }
}
