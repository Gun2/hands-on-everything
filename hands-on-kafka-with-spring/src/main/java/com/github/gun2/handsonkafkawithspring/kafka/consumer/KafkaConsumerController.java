package com.github.gun2.handsonkafkawithspring.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KafkaConsumerController {
    private final KafkaConsumerService kafkaConsumerService;

    @GetMapping("/kafka/consumers")
    public List<KafkaContainerDto> findAll(){
        return kafkaConsumerService.findAll();
    }

    @GetMapping("/kafka/consumers/{id}")
    public KafkaContainerDto findById(
            @PathVariable("id") String id
    ){
        return kafkaConsumerService.findById(id);
    }

    @PostMapping("/kafka/consumers/{id}/stop")
    public KafkaContainerDto stop(
            @PathVariable("id") String id
    ){
        return kafkaConsumerService.stop(id);
    }

    @PostMapping("/kafka/consumers/{id}/start")
    public KafkaContainerDto start(
            @PathVariable("id") String id
    ){
        return kafkaConsumerService.start(id);
    }
}
