package com.prova.e_commerce.RabbitMQ;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//Implementa questo service negli endpoint come producer 
@Service
public class RabbitProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public RabbitProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        System.out.println("Message sent: " + message);
    }
}
