package com.github.gun2.handsonkafkawithspring.retry.blocking;

import lombok.Getter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class BlockingRetryListener {
    public static final String TOPIC_NAME = "greeting-blocking";

    private int failCount = 0;
    @Getter
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @KafkaListener(topics = TOPIC_NAME, groupId = "test-group", containerFactory = BlockingRetryConsumerConfig.CONFIG_BEAN_NAME)
    public void listen(String message) {
        System.out.println("📥 Received: " + message);

        //실패 유도
        if (failCount < 10) {
            failCount++;
            throw new RuntimeException("Simulated failure");
        }
        System.out.println("✅ Successfully processed after retry: " + message);
        this.countDownLatch.countDown();
    }

    public void reset() {
        failCount = 0;
        countDownLatch = new CountDownLatch(1);
    }
}
