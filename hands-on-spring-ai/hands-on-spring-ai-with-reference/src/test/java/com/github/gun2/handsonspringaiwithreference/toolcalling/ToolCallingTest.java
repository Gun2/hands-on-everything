package com.github.gun2.handsonspringaiwithreference.toolcalling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("local")
public class ToolCallingTest {
    @Autowired
    ChatModel chatModel;
    @Autowired
    ChatMemory chatMemory;

    class DateTimeTools {
        @Tool(description = "Get the current date and time in the user's timezone")
        String getCurrentDateTime() {
            return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
        }

        @Tool(description = "Set a user alarm for the given time")
        void setAlarm(@ToolParam(description = "Time in ISO-8601 format") String time) {
            LocalDateTime alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
            System.out.println("Alarm set for " + alarmTime);
        }
    }

    @Test
    @DisplayName("선언적 방식 Tool calling")
    void DeclarativeToolCallingTest() {
        /** given */
        String conversationId = UUID.randomUUID().toString();
        String content = "What time is it now?";
        String nextContent = "What day is tomorrow?";
        String finalContent = "Can you set an alarm 10 minutes from tomorrow?";

        /** when */
        chatMemory.add(conversationId, new UserMessage(content));
        String answer = ChatClient.create(chatModel)
                .prompt(new Prompt(chatMemory.get(conversationId)))
                .tools(new DateTimeTools())
                .call()
                .content();

        chatMemory.add(conversationId, new UserMessage(nextContent));
        String nextAnswer = ChatClient.create(chatModel)
                .prompt(new Prompt(chatMemory.get(conversationId)))
                .tools(new DateTimeTools())
                .call()
                .content();

        chatMemory.add(conversationId, new UserMessage(finalContent));
        String finalAnswer = ChatClient.create(chatModel)
                .prompt(new Prompt(chatMemory.get(conversationId)))
                .tools(new DateTimeTools())
                .call()
                .content();


        /** then */
        System.out.println(answer);
        System.out.println("nextAnswer: ");
        System.out.println(nextAnswer);
        System.out.println("finalAnswer: ");
        System.out.println(finalAnswer);

    }


    @Test
    @DisplayName("동적 방식 Tool calling")
    void ProgrammaticToolCallingTest() {
        /** given */
        String conversationId = UUID.randomUUID().toString();
        String content = "What time is it now?";
        String nextContent = "What day is tomorrow?";
        String finalContent = "Can you set an alarm 10 minutes from tomorrow?";

        Method getCurrentDateTimeMethod = ReflectionUtils.findMethod(DateTimeTools.class, "getCurrentDateTime").get();
        ToolCallback getCurrentDateToolCallback = MethodToolCallback.builder()
                .toolDefinition(ToolDefinitions.builder(getCurrentDateTimeMethod)
                        .description("Get the current date and time in the user's timezone")
                        .build())
                .toolMethod(getCurrentDateTimeMethod)
                .toolObject(new DateTimeTools())
                .build();

        Method setAlarmMethod = ReflectionUtils.findMethod(DateTimeTools.class, "setAlarm", String.class).get();
        ToolCallback setAlarmToolCallback = MethodToolCallback.builder()
                .toolDefinition(ToolDefinitions.builder(setAlarmMethod)
                        .description("Set a user alarm for the given time")
                        .build())
                .toolMethod(setAlarmMethod)
                .toolObject(new DateTimeTools())
                .build();

        /** when */
        chatMemory.add(conversationId, new UserMessage(content));

        String answer = ChatClient.create(chatModel)
                .prompt(new Prompt(chatMemory.get(conversationId)))
                .toolCallbacks(getCurrentDateToolCallback, setAlarmToolCallback)
                .call()
                .content();

        chatMemory.add(conversationId, new UserMessage(nextContent));

        String nextAnswer = ChatClient.create(chatModel)
                .prompt(new Prompt(chatMemory.get(conversationId)))
                .toolCallbacks(getCurrentDateToolCallback, setAlarmToolCallback)
                .call()
                .content();

        chatMemory.add(conversationId, new UserMessage(finalContent));
        String finalAnswer = ChatClient.create(chatModel)
                .prompt(new Prompt(chatMemory.get(conversationId)))
                .tools(new DateTimeTools())
                .call()
                .content();

        /** then */
        System.out.println(answer);
        System.out.println("nextAnswer: ");
        System.out.println(nextAnswer);
        System.out.println("finalAnswer: ");
        System.out.println(finalAnswer);
    }
}
