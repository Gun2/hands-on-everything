package com.github.gun2.handsonspringaiwithreference.chatclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;

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

    @Test
    void getChatResponseTest() {
        /** given */
        String userInput = "Tell me a joke";

        /** when */
        ChatResponse chatResponse = chatClient.prompt()
                .user(userInput)
                .call()
                .chatResponse();

        /** then */
        System.out.println(chatResponse);
    }



    @Test
    void getReturningEntityTest() {
        /** given */
        record ActorFilms(String actor, List<String> movies) {}

        /** when */
        ActorFilms actorFilms = chatClient.prompt()
                .user("Generate the filmography for a random actor.")
                .call()
                .entity(ActorFilms.class);

        /** then */
        System.out.println(actorFilms);
    }

    @Test
    void streamingResponseTest() {
        /** given */
        int questionSize = 10;
        CountDownLatch countDownLatch = new CountDownLatch(questionSize);
        String questionTemplate = """
                    What is the number less than {number}?
                """;
        /** when */
        for (int i = 1; i <= questionSize; i++) {
            String number = String.valueOf(i);
            this.chatClient.prompt()
                    .user(u -> u.text(questionTemplate).param("number", number))
                    .stream()
                    .content().collectList().subscribe(content -> {
                        System.out.println(String.join("", content));
                        countDownLatch.countDown();
                    });
        }

        /** then */
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void promptTemplateTest() {
        /** given */

        /** when */
        String answer = this.chatClient.prompt()
                .user(u -> u
                        .text("Tell me the names of 5 movies whose soundtrack was composed by <composer>")
                        .param("composer", "John Williams"))
                .templateRenderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .call()
                .content();

        /** then */
        System.out.println(answer);
    }

    @Test
    void harryPottersJokeTest() {
        /** given */
        String voice = "Harry Potter";
        String message = "Tell me a joke";
        ChatClient customChatClient = clientBuilder.defaultSystem("You are a friendly chat bot that answers question in the voice of a {voice}").build();
        /** when */
        String content = customChatClient.prompt()
                .system(sp -> sp.param("voice", voice))
                .user(message)
                .call()
                .content();

        /** then */
        System.out.println(content);
    }
}
