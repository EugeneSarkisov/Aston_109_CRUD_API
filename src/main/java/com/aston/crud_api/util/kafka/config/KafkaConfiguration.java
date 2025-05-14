package com.aston.crud_api.util.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.internals.Topic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic successTopic(){
        return TopicBuilder.name("crud-api-events-topic")
                .partitions(1)
                .replicas(1)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic errorTopic(){
        return TopicBuilder.name("crud-api-error-events-topic")
                .partitions(1)
                .replicas(1)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

}
