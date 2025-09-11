package com.example.banto.Configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic applyTopic() {
        return TopicBuilder.name("apply-seller")
            .partitions(1)
            .replicas(1)
            //.compact()                         // key 값 무시
            .build();
    }

    @Bean
    public NewTopic itemTopic() {
        return TopicBuilder.name("search-item-log")
            .partitions(1)
            .replicas(1)
            //.compact()                         // key 값 무시
            .build();
    }

    @Bean
    public NewTopic cartTopic() {
        return TopicBuilder.name("add-cart-log")
            .partitions(1)
            .replicas(1)
            //.compact()                         // key 값 무시
            .build();
    }
}