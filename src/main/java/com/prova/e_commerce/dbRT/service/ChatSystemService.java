package com.prova.e_commerce.dbRT.service;

import com.prova.e_commerce.dbRT.model.ChatSystem;
import com.prova.e_commerce.dbRT.model.ChatSystem.Message;
import com.prova.e_commerce.dbRT.repository.interfacce.ChatSystemRep;
import com.prova.e_commerce.storage.OzoneService;
import com.google.api.core.ApiFuture;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ChatSystemService {

    private static final Logger logger = LoggerFactory.getLogger(ChatSystemService.class);

    @Autowired
    private ChatSystemRep chatSystemRep;

    @Autowired
    private OzoneService ozoneService;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    public ChatSystemService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.chat.create.count");
        meterRegistry.counter("service.chat.update.count");
        meterRegistry.counter("service.chat.delete.count");
        meterRegistry.counter("service.message.create.count");
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#chat.chatId")
    public ApiFuture<Void> createChat(ChatSystem chat) {
        logger.info("Creazione chat: chatId={}", chat.getId());
        Span span = tracer.spanBuilder("createChat").startSpan();
        try {
            meterRegistry.counter("service.chat.create.count").increment();
            return chatSystemRep.createChat(chat);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#chat.chatId")
    public ApiFuture<Void> updateChat(ChatSystem chat) {
        logger.info("Aggiornamento chat: chatId={}", chat.getId());
        Span span = tracer.spanBuilder("updateChat").startSpan();
        try {
            meterRegistry.counter("service.chat.update.count").increment();
            return chatSystemRep.updateChat(chat);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#chatId")
    public CompletableFuture<ChatSystem> selectChatById(String chatId) {
        logger.info("Selezione chat per ID: chatId={}", chatId);
        Span span = tracer.spanBuilder("selectChatById").startSpan();
        try {
            meterRegistry.counter("service.chat.select.count").increment();
            return chatSystemRep.selectChatById(chatId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#chatId")
    public ApiFuture<Void> deleteChat(String chatId) {
        logger.info("Eliminazione chat: chatId={}", chatId);
        Span span = tracer.spanBuilder("deleteChat").startSpan();
        try {
            meterRegistry.counter("service.chat.delete.count").increment();
            return chatSystemRep.deleteChat(chatId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#chatId")
    public ApiFuture<Void> createMessage(String chatId, Message message) {
        logger.info("Creazione messaggio: chatId={}, messageId={}", chatId, message.getConversationId());
        Span span = tracer.spanBuilder("createMessage").startSpan();
        try {
            meterRegistry.counter("service.message.create.count").increment();
            return chatSystemRep.createMessage(chatId, message);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#chatId")
    public ApiFuture<Void> updateMessage(String chatId, String messageId, Message updatedMessage) {
        logger.info("Aggiornamento messaggio: chatId={}, messageId={}", chatId, messageId);
        Span span = tracer.spanBuilder("updateMessage").startSpan();
        try {
            meterRegistry.counter("service.message.update.count").increment();
            return chatSystemRep.updateMessage(chatId, messageId, updatedMessage);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#chatId")
    public CompletableFuture<List<Message>> selectMessagesByChat(String chatId) {
        logger.info("Selezione messaggi per chat ID: chatId={}", chatId);
        Span span = tracer.spanBuilder("selectMessagesByChat").startSpan();
        try {
            meterRegistry.counter("service.message.select.count").increment();
            return chatSystemRep.selectMessagesByChat(chatId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#chatId")
    public ApiFuture<Void> deleteMessage(String chatId, String messageId) {
        logger.info("Eliminazione messaggio: chatId={}, messageId={}", chatId, messageId);
        Span span = tracer.spanBuilder("deleteMessage").startSpan();
        try {
            meterRegistry.counter("service.message.delete.count").increment();
            return chatSystemRep.deleteMessage(chatId, messageId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#chatId")
    public ApiFuture<Void> deleteAllMessages(String chatId) {
        logger.info("Eliminazione di tutti i messaggi: chatId={}", chatId);
        Span span = tracer.spanBuilder("deleteAllMessages").startSpan();
        try {
            meterRegistry.counter("service.message.delete.all.count").increment();
            return chatSystemRep.deleteAllMessages(chatId);
        } finally {
            span.end();
        }
    }
}
