package com.github.gun2.handsonkafkawithspring.retry.blocking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = {"kafka.backoff.interval=1000", "kafka.backoff.max_failure=10"})
class BlockingRetryListenerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private BlockingRetryListener blockingRetryListener;

    @Test
    void testBlockingRetry() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        blockingRetryListener.reset(); // 실패 카운트 초기화

        kafkaTemplate.send(BlockingRetryListener.TOPIC_NAME, "Hello Retry");
        kafkaTemplate.send(BlockingRetryListener.TOPIC_NAME, "Hello Retry");
        kafkaTemplate.send(BlockingRetryListener.TOPIC_NAME, "Hello Retry");

        // Retry + 처리 시간 대기
        blockingRetryListener.getCountDownLatch().await();
        stopWatch.stop();

        // 로그로 Retry 및 성공 여부 확인
        System.out.println(stopWatch.getTotalTimeMillis() + "ms");
    }
}