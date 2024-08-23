package com.github.gun2.handsonwebflux.board;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class BoardControllerTest {
    private final WebClient client = WebClient.create("http://localhost:8080");

    @Test
    void createTest() throws InterruptedException {
        /** given */
        int times = 100;
        CountDownLatch countDownLatch = new CountDownLatch(times);
        /** when */
        log.info("for 문 시작");
        for (int i = 0; i < times; i++) {
            Mono<BoardDto> mono = client.post()
                    .uri("/boards")
                    .bodyValue(BoardDto.BoardRequest.builder()
                            .title("test" + i)
                            .content("test")
                            .build())
                    .retrieve()
                    .bodyToMono(BoardDto.class);
            mono.subscribe((dto) -> {
                countDownLatch.countDown();
            });
        }
        log.info("for 문 종료");
        /** then */
        countDownLatch.await(10, TimeUnit.SECONDS);
        log.info("request 호출 종료");
    }
}