package com.github.gun2.handsonkafkawithspring.kafka.admin.topic;

import lombok.Getter;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;

import java.util.List;

@Getter
public class KafkaTopic {
    private final String uuid;
    private final String name;
    private final boolean internal;
    private final List<KafkaPartition> partitionList;

    public KafkaTopic(TopicDescription topicDescription) {
        this.uuid = topicDescription.topicId().toString();
        this.name = topicDescription.name();
        this.internal = topicDescription.isInternal();
        this.partitionList = topicDescription.partitions().stream().map(KafkaPartition::new).toList();
    }

    @Getter
    public static class KafkaPartition {
        private final int partition;
        private final List<KafkaNode> replicas;

        public KafkaPartition(TopicPartitionInfo topicPartitionInfo) {
            this.partition = topicPartitionInfo.partition();
            this.replicas = topicPartitionInfo.replicas().stream().map(KafkaNode::new).toList();
        }
    }

    @Getter
    public static class KafkaNode {
        private final int id;
        private final int hashCode;
        private final int port;
        private final String rack;
        private final String host;
        public KafkaNode(Node node) {
            this.id = node.id();
            this.hashCode = node.hashCode();
            this.port = node.port();
            this.rack = node.rack();
            this.host = node.host();
        }
    }
}
