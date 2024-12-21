package com.prova.e_commerce.dbRT.service;

import com.prova.e_commerce.dbRT.model.Notifications;
import com.prova.e_commerce.dbRT.repository.interfacce.NotificationsRep;
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
public class NotificationsService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationsService.class);

    @Autowired
    private NotificationsRep notificationsRep;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    public NotificationsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.notifications.create.count");
        meterRegistry.counter("service.notifications.update.count");
        meterRegistry.counter("service.notifications.delete.count");
        meterRegistry.counter("service.notifications.messages.count");
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public CompletableFuture<Void> createNotification(Notifications notification) {
        logger.info("Creazione nuova notifica: {}", notification);
        Span span = tracer.spanBuilder("createNotification").startSpan();
        try {
            meterRegistry.counter("service.notifications.create.count").increment();
            return CompletableFuture.runAsync(() -> {
                try {
                    notificationsRep.createNotification(notification).get();
                } catch (Exception e) {
                    throw new RuntimeException("Errore durante la creazione della notifica", e);
                }
            });
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#notification.id")
    public CompletableFuture<Void> updateNotification(Notifications notification) {
        logger.info("Aggiornamento notifica: {}", notification);
        Span span = tracer.spanBuilder("updateNotification").startSpan();
        try {
            meterRegistry.counter("service.notifications.update.count").increment();
            return CompletableFuture.runAsync(() -> {
                try {
                    notificationsRep.updateNotification(notification).get();
                } catch (Exception e) {
                    throw new RuntimeException("Errore durante l'aggiornamento della notifica", e);
                }
            });
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#notificationId")
    public CompletableFuture<Notifications> getNotificationById(String notificationId) {
        logger.info("Recupero notifica per ID: {}", notificationId);
        Span span = tracer.spanBuilder("getNotificationById").startSpan();
        try {
            meterRegistry.counter("service.notifications.get.count").increment();
            return notificationsRep.selectNotificationById(notificationId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#notificationId")
    public CompletableFuture<Void> deleteNotification(String notificationId) {
        logger.info("Eliminazione notifica per ID: {}", notificationId);
        Span span = tracer.spanBuilder("deleteNotification").startSpan();
        try {
            meterRegistry.counter("service.notifications.delete.count").increment();
            return CompletableFuture.runAsync(() -> {
                try {
                    notificationsRep.deleteNotification(notificationId).get();
                } catch (Exception e) {
                    throw new RuntimeException("Errore durante l'eliminazione della notifica", e);
                }
            });
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#notificationId")
    public CompletableFuture<Void> addMessageToNotification(String notificationId, Notifications.Message message) {
        logger.info("Aggiunta messaggio alla notifica: notificationId={}, message={}", notificationId, message);
        Span span = tracer.spanBuilder("addMessageToNotification").startSpan();
        try {
            meterRegistry.counter("service.notifications.messages.count").increment();
            return CompletableFuture.runAsync(() -> {
                try {
                    notificationsRep.createMessageForNotification(notificationId, message).get();
                } catch (Exception e) {
                    throw new RuntimeException("Errore durante l'aggiunta del messaggio alla notifica", e);
                }
            });
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#notificationId")
    public CompletableFuture<List<Notifications.Message>> getMessagesForNotification(String notificationId) {
        logger.info("Recupero messaggi per notifica: {}", notificationId);
        Span span = tracer.spanBuilder("getMessagesForNotification").startSpan();
        try {
            meterRegistry.counter("service.notifications.messages.get.count").increment();
            return notificationsRep.selectMessagesByNotification(notificationId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#notificationId")
    public CompletableFuture<Void> deleteMessageFromNotification(String notificationId, String messageId) {
        logger.info("Eliminazione messaggio da notifica: notificationId={}, messageId={}", notificationId, messageId);
        Span span = tracer.spanBuilder("deleteMessageFromNotification").startSpan();
        try {
            meterRegistry.counter("service.notifications.messages.delete.count").increment();
            return CompletableFuture.runAsync(() -> {
                try {
                    notificationsRep.deleteMessageFromNotification(notificationId, messageId).get();
                } catch (Exception e) {
                    throw new RuntimeException("Errore durante l'eliminazione del messaggio dalla notifica", e);
                }
            });
        } finally {
            span.end();
        }
    }
}
