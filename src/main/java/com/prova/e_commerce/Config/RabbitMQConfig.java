package com.prova.e_commerce.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ================================
    // Configurazione per ArchiviazioneTransizioniService
    // ================================
    public static final String TRANSIZIONI_INPUT_QUEUE = "transizioniInputQueue";
    public static final String TRANSIZIONI_OUTPUT_QUEUE = "transizioniOutputQueue";
    public static final String TRANSIZIONI_EXCHANGE = "transizioniExchange";

    @Bean
    public Queue transizioniInputQueue() {
        return new Queue(TRANSIZIONI_INPUT_QUEUE, true);
    }

    @Bean
    public Queue transizioniOutputQueue() {
        return new Queue(TRANSIZIONI_OUTPUT_QUEUE, true);
    }

    @Bean
    public TopicExchange transizioniExchange() {
        return new TopicExchange(TRANSIZIONI_EXCHANGE);
    }

    @Bean
    public Binding bindingTransizioniInputQueue(Queue transizioniInputQueue, TopicExchange transizioniExchange) {
        return BindingBuilder.bind(transizioniInputQueue).to(transizioniExchange).with(TRANSIZIONI_INPUT_QUEUE);
    }

    @Bean
    public Binding bindingTransizioniOutputQueue(Queue transizioniOutputQueue, TopicExchange transizioniExchange) {
        return BindingBuilder.bind(transizioniOutputQueue).to(transizioniExchange).with(TRANSIZIONI_OUTPUT_QUEUE);
    }

    // ================================
    // Configurazione per ShippingStatusService
    // ================================
    public static final String SHIPPING_INPUT_QUEUE = "shippingStatusInputQueue";
    public static final String SHIPPING_OUTPUT_QUEUE = "shippingStatusOutputQueue";
    public static final String SHIPPING_EXCHANGE = "shippingStatusExchange";

    @Bean
    public Queue shippingInputQueue() {
        return new Queue(SHIPPING_INPUT_QUEUE, true);
    }

    @Bean
    public Queue shippingOutputQueue() {
        return new Queue(SHIPPING_OUTPUT_QUEUE, true);
    }

    @Bean
    public TopicExchange shippingExchange() {
        return new TopicExchange(SHIPPING_EXCHANGE);
    }

    @Bean
    public Binding bindingShippingInputQueue(Queue shippingInputQueue, TopicExchange shippingExchange) {
        return BindingBuilder.bind(shippingInputQueue).to(shippingExchange).with(SHIPPING_INPUT_QUEUE);
    }

    @Bean
    public Binding bindingShippingOutputQueue(Queue shippingOutputQueue, TopicExchange shippingExchange) {
        return BindingBuilder.bind(shippingOutputQueue).to(shippingExchange).with(SHIPPING_OUTPUT_QUEUE);
    }
}
