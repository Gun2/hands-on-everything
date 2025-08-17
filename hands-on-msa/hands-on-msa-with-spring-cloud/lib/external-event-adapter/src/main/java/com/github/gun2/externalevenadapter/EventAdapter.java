package com.github.gun2.externalevenadapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gun2.event.external.ExternalEvent;
import com.github.gun2.event.external.Topics;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;

@Slf4j
public class EventAdapter {
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher publisher;
    private final ExternalEventAdapterProperties properties;
    private final String serviceName;

    public EventAdapter(ObjectMapper objectMapper, ApplicationEventPublisher publisher, ExternalEventAdapterProperties properties, String serviceName) {
        this.objectMapper = objectMapper;
        this.publisher = publisher;
        this.properties = properties;
        this.serviceName = serviceName;
    }

    @RetryableTopic(
            backoff = @Backoff(value = 1000L),
            attempts = "5",
            autoCreateTopics = "true")
    @KafkaListener(topics = Topics.GLOBAL, groupId = "${spring.application.name}")
    public void listen(ExternalEvent externalEvent) {
        if (!properties.isEnabled()){
            return;
        }
        if (this.serviceName.equals(externalEvent.getSourceService())) {
            return;
        }
        try {
            // 2. eventType 정보를 활용하여 클래스 로드
            Class<?> eventClass = Class.forName(externalEvent.getEventType());

            // 3. payload를 해당 클래스 타입으로 역직렬화
            Object internalEvent = objectMapper.readValue(externalEvent.getPayload(), eventClass);

            // 4. 역직렬화된 객체(internalEvent)를 Spring 이벤트로 발행
            publisher.publishEvent(internalEvent);

        } catch (ClassNotFoundException e) {
            // eventType에 해당하는 클래스를 찾지 못했을 때의 예외 처리
            log.error("Failed to find event class for type: {}", externalEvent.getEventType(), e);
        } catch (Exception e) {
            // ObjectMapper 역직렬화 실패 시의 예외 처리
            log.error("Failed to deserialize event payload: {}", externalEvent.getPayload(), e);
        }
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<?, ?> record, Exception e) {
        log.error("DLT received: {} cause: {}", record.value(), e.getMessage());
    }

}
