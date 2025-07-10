package com.github.gun2.handsonkafkawithspring.retry.nonblocking;

import lombok.Getter;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class NonBlockingRetryListener {
    public static final String TOPIC_NAME = "greeting-non-blocking";

    @Getter
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private int failCount = 0;

    @RetryableTopic(
            backoff = @Backoff(value = 1000L),
            attempts = "1",
            autoCreateTopics = "true")
    @KafkaListener(topics = TOPIC_NAME, groupId = "test-group")
    public void listen(String message) {
        System.out.println("📥 Received: " + message);

        //실패 유도
        if (failCount < 4) {
            failCount++;
            throw new RuntimeException("Simulated failure");
        }
        System.out.println("✅ Successfully processed after retry: " + message);
        this.countDownLatch.countDown();
    }

    @DltHandler
    public void handleDlt(String message) {
        System.out.println("dlt : " + message);
    }

    public void reset() {
        failCount = 0;
        countDownLatch = new CountDownLatch(1);
    }
}
