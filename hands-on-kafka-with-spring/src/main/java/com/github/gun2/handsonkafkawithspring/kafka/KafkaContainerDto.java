package com.github.gun2.handsonkafkawithspring.kafka;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
public class KafkaContainerDto {
    private String mainListenerId;
    private ContainerProperties containerProperties;
    private boolean containerPaused;
    private List<TopicPartitionSimple> assignedPartitions;
    private String groupId;
    private boolean running;

    public KafkaContainerDto(MessageListenerContainer container) {
        this.mainListenerId = container.getMainListenerId();
        this.containerProperties = container.getContainerProperties();
        this.containerPaused = container.isContainerPaused();
        this.assignedPartitions = container.getAssignedPartitions().stream().map(TopicPartitionSimple::new).toList();
        this.groupId = container.getGroupId();
        this.running = container.isRunning();
    }

    @Getter
    @Setter
    @ToString
    public class TopicPartitionSimple {
        private int partition;
        private String topic;
        public TopicPartitionSimple(TopicPartition topicPartition) {
            this.partition = topicPartition.partition();
            this.topic = topicPartition.topic();
        }
    }
}
