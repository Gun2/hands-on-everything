package com.github.gun2.handsonkafkawithspring.retry.nonblocking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NonBlockingRetryListenerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private NonBlockingRetryListener nonBlockingRetryListener;

    @Test
    void testBlockingRetry() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        nonBlockingRetryListener.reset(); // 실패 카운트 초기화

        kafkaTemplate.send(NonBlockingRetryListener.TOPIC_NAME, "Hello Retry");
        kafkaTemplate.send(NonBlockingRetryListener.TOPIC_NAME, "Hello Retry");
        kafkaTemplate.send(NonBlockingRetryListener.TOPIC_NAME, "Hello Retry");

        // Retry + 처리 시간 대기
        nonBlockingRetryListener.getCountDownLatch().await();
        stopWatch.stop();

        // 로그로 Retry 및 성공 여부 확인
        System.out.println(stopWatch.getTotalTimeMillis() + "ms");

    }
}