package com.github.gun2.handsonspringaiwithreference.chatclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatClientTest {

    @Autowired
    ChatClient.Builder  clientBuilder;

    ChatClient chatClient;
    @BeforeEach
    public void setup() {
        this.chatClient = clientBuilder.build();
    }

    @Test
    void promptTest() {
        /** given */
        String userInput = "Hello AI";

        /** when */
        String content = this.chatClient.prompt()
                .user(userInput)
                // ai 호출
                .call()
                // ai model의 응답 반환
                .content();

        /** then */
        System.out.println(content);
    }
}
