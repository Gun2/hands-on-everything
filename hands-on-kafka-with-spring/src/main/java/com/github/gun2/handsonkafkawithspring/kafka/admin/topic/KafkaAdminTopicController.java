package com.github.gun2.handsonkafkawithspring.kafka.admin.topic;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class KafkaAdminTopicController {
    private final KafkaAdminTopicService kafkaAdminTopicService;


    @GetMapping("/kafka/admin/topics")
    public List<KafkaTopic> findAll() throws ExecutionException, InterruptedException {
        return kafkaAdminTopicService.findAll();
    }
    @PostMapping("/kafka/admin/topics")
    public void create(
            @RequestBody KafkaTopicCreateRequest createRequest
    ) throws ExecutionException, InterruptedException {
        kafkaAdminTopicService.create(createRequest.getTopicName(), createRequest.getNumPartitions(), createRequest.getReplicationFactor());
    }
}
