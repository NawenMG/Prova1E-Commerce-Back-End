package com.prova.e_commerce.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;

@Configuration
public class SqsConfig {

    @Bean
    public AmazonSQS customAmazonSQS() {
        return AmazonSQSClient.builder()
                              .withRegion("eu-west-1")
                              .build();
    }
}
