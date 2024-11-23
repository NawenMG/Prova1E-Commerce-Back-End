package com.prova.e_commerce.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.beans.factory.annotation.Value;


@Configuration
@EnableKafka
public class KafkaConfig {
    
    @Value("${kafka.topic.name}")
    private String topicName;
    
    @Bean
    public NewTopic kafkaTopic() {
        return TopicBuilder.name(topicName)
                .partitions(1)
                .replicas(1)
                .build();
    }
}