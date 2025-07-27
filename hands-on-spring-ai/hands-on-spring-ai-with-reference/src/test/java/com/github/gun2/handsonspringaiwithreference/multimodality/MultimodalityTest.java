package com.github.gun2.handsonspringaiwithreference.multimodality;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;

@SpringBootTest
public class MultimodalityTest {

    @Autowired
    ChatClient.Builder  clientBuilder;

    ChatClient chatClient;
    @BeforeEach
    public void setup() {
        this.chatClient = clientBuilder.build();
    }


    @Test
    void imageExplainTest() {
        /** given */
        ClassPathResource resource = new ClassPathResource("tests/multimodal.test.png");

        /** when */
        String content = chatClient.prompt().user(
                user -> user.text("Explain what do you see on this picture?")
                        .media(MimeTypeUtils.IMAGE_PNG, resource)
        ).call().content();

        /** then */
        System.out.println(content);
        // The image shows a metal wire basket containing three bananas and two red apples. The bananas are yellow with visible dark spots, indicating they might be ripe. The apples appear to be fresh and have a bright red color. In the background, there is what looks like a part of a room or interior decor, possibly a wall with a blue hue, but not enough detail is visible to provide more information about the setting.
    }
}
