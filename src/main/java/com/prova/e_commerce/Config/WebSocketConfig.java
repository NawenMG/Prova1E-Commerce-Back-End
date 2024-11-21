package com.prova.e_commerce.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry config) {
        // Abilita un broker per inoltrare i messaggi a destinazione specifiche
        config.enableSimpleBroker("/topic"); // Sottoscrizioni, per la gestione dei canali
        /*Spiegazione
         * enableSimpleBroker("/topic"): Configura un broker di messaggi semplice (un broker in-memory) che gestisce i messaggi destinati alle destinazioni con il prefisso /topic. In pratica, i client possono sottoscriversi a canali come /topic/someChannel e ricevere messaggi inviati su questi canali.
         */
        config.setApplicationDestinationPrefixes("/app"); // Prefisso per inviare messaggi al server
        /*
         setApplicationDestinationPrefixes("/app"): Imposta il prefisso per i messaggi inviati dal client al server. I messaggi che il client invia al server devono iniziare con il prefisso /app. Ad esempio, un messaggio inviato a /app/sendMessage sarà gestito da un controller configurato per rispondere a quella destinazione.
         */
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Configura il punto di accesso WebSocket
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }
}
