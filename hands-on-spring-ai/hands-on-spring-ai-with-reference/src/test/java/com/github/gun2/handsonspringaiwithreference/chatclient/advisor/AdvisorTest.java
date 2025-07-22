package com.github.gun2.handsonspringaiwithreference.chatclient.advisor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.util.Map;

@SpringBootTest
public class AdvisorTest {

    @Autowired
    ChatClient.Builder  clientBuilder;

    ChatClient chatClient;
    @BeforeEach
    public void setup() {
        this.chatClient = clientBuilder.build();
    }

    @Test
    void loggingAdvisorTest() {
        /** given */
        SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        /** when */
        String content = chatClient.prompt()
                .advisors(
                        simpleLoggerAdvisor
                ).user("Tell me a joke")
                .call()
                .content();

        /** then */
        System.out.println(content);
    }

    public class SimpleLoggerAdvisor implements CallAdvisor, StreamAdvisor {

        private static final Logger logger = LoggerFactory.getLogger(SimpleLoggerAdvisor.class);

        @Override
        public String getName() {
            /** advisor의 고유 이름 정의 */
            return this.getClass().getSimpleName();
        }

        @Override
        public int getOrder() {
            /** advisor 실행 순서 설정 */
            return 0;
        }

        @Override
        public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
            logger.info("BEFORE: {}", chatClientRequest);
            ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
            logger.info("AFTER: {}", chatClientResponse);
            return chatClientResponse;
        }

        @Override
        public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
            logger.info("BEFORE: {}", chatClientRequest);
            Flux<ChatClientResponse> chatClientResponseFlux = streamAdvisorChain.nextStream(chatClientRequest);
            /** ChatClientMessageAggregator Flux 응답을 하나의 AdvisedResponse로 모아주는 유틸리티 클래스. */
            return new ChatClientMessageAggregator().aggregateChatClientResponse(chatClientResponseFlux,
                    advisedResponse -> logger.debug("AFTER: {}", advisedResponse));
        }
    }

    @Test
    void reReadingAdvisorTest() {
        /** given */
        ReReadingAdvisor reReadingAdvisor = new ReReadingAdvisor();
        String prompt = "Tell me a joke";

        /** when */
        String context = this.chatClient.prompt().user(prompt).call().content();
        String reReadingContext = this.chatClient.prompt().advisors(reReadingAdvisor).user(prompt).call().content();

        /** then */
        System.out.println("context : " + context);
        System.out.println("reReadingContext : " + reReadingContext);
    }

    public class ReReadingAdvisor implements BaseAdvisor {

        private static final String DEFAULT_RE2_ADVISE_TEMPLATE = """
            {re2_input_query}
            Read the question again: {re2_input_query}
            """;

        private final String re2AdviseTemplate;

        private int order = 0;

        public ReReadingAdvisor() {
            this(DEFAULT_RE2_ADVISE_TEMPLATE);
        }

        public ReReadingAdvisor(String re2AdviseTemplate) {
            this.re2AdviseTemplate = re2AdviseTemplate;
        }
        /** 사용자의 input query에 Re2를 적용하는 메서드 */
        @Override
        public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
            String augmentedUserText = PromptTemplate.builder()
                    .template(this.re2AdviseTemplate)
                    .variables(Map.of("re2_input_query", chatClientRequest.prompt().getUserMessage().getText()))
                    .build()
                    .render();

            return chatClientRequest.mutate()
                    .prompt(chatClientRequest.prompt().augmentUserMessage(augmentedUserText))
                    .build();
        }

        @Override
        public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
            return chatClientResponse;
        }

        @Override
        public int getOrder() {
            return this.order;
        }

        public ReReadingAdvisor withOrder(int order) {
            this.order = order;
            return this;
        }
    }

    @Test
    void advisorContextTest() {
        /** given */
        String key = "CONTEXT_KEY";
        //context의 값을 1씩 증가 시키는 advisor
        CallAdvisor contextAdvisor = new CallAdvisor() {
            @Override
            public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
                chatClientRequest.context().put(key, Integer.valueOf(chatClientRequest.context().getOrDefault(key, 0).toString()) + 1);
                return callAdvisorChain.nextCall(chatClientRequest);
            }

            @Override
            public String getName() {
                return "contextAdvisor";
            }

            @Override
            public int getOrder() {
                return 0;
            }
        };
        //context의 값으로 질문하는 advisor
        CallAdvisor finishAdvisor = new CallAdvisor() {
            @Override
            public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
                String contextValue = chatClientRequest.context().getOrDefault(key, 0).toString();
                ChatClientRequest mutatedChatClientRequest = chatClientRequest.mutate().prompt(chatClientRequest.prompt().augmentUserMessage("just say the number " + contextValue)).build();
                return callAdvisorChain.nextCall(mutatedChatClientRequest);
            }

            @Override
            public String getName() {
                return "finishAdvisor";
            }

            @Override
            public int getOrder() {
                return 1000;
            }
        };
        /** when */
        String content = chatClient.prompt().advisors(
                contextAdvisor, contextAdvisor, contextAdvisor, contextAdvisor, contextAdvisor,
                finishAdvisor
        ).user("질문은 마지막의 finishAdvisor에서 처리합니다.").call().content();

        /** then */
        System.out.println(content);
    }

    @Test
    void messageChatMemoryAdvisorTest() {
        /** given */
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
        MessageChatMemoryAdvisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        ChatClient muatatedChatClient = chatClient.mutate().defaultAdvisors(chatMemoryAdvisor).build();


        /** when */
        String content1 = muatatedChatClient.prompt().user("what is the number after number 1?").call().content();
        String content2 = muatatedChatClient.prompt().user("what is the next number?").call().content();
        String content3 = muatatedChatClient.prompt().user("what is the next number?").call().content();
        String content4 = muatatedChatClient.prompt().user("what is the next number?").call().content();
        String content5 = muatatedChatClient.prompt().user("what is the next number?").call().content();

        /** then */
        System.out.println(content1);
        System.out.println(content2);
        System.out.println(content3);
        System.out.println(content4);
        System.out.println(content5);
    }


    @Test
    void promptChatMemoryAdvisorTest() {
        /** given */
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
        PromptChatMemoryAdvisor promptAdvisor = PromptChatMemoryAdvisor.builder(chatMemory).systemPromptTemplate(
                new PromptTemplate("""
                        Use the conversation memory from the MEMORY section to provide accurate answers.
                        ---------------------
                        MEMORY:
                        {memory}
                        ---------------------
                        """)
        ).build();
        ChatClient muatatedChatClient = chatClient.mutate().defaultAdvisors(promptAdvisor).build();


        /** when */
        String content1 = muatatedChatClient.prompt().user("Add this name to a list and return all the values: Bob").call().content();
        String content2 = muatatedChatClient.prompt().user("Add this name to a list and return all the values: John").call().content();
        String content3 = muatatedChatClient.prompt().user("Add this name to a list and return all the values: Anna").call().content();
        /** then */
        System.out.println(content1);
        System.out.println(content2);
        System.out.println(content3);
    }

}
