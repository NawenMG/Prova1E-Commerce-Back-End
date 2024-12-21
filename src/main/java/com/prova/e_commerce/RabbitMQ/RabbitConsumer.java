package com.prova.e_commerce.RabbitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeMessage(String message) {
        System.out.println("Message received: " + message);
    }
}
