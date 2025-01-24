package com.prova.e_commerce.dbRT.service;

import com.prova.e_commerce.dbRT.model.Notifications;
import com.prova.e_commerce.dbRT.model.Notifications.Message;
import com.prova.e_commerce.dbRT.repository.interfacce.NotificationsRep;
import com.prova.e_commerce.sendGridEmail.EmailService;  // <--- import del tuo EmailService
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
    private NotificationsRep notificationsRep; // Il repository per Firebase

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    // == Aggiunta del servizio per l'invio email con SendGrid ==
    @Autowired
    private EmailService emailService;

    // Costruttore
    public NotificationsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.notifications.create.count");
        meterRegistry.counter("service.notifications.update.count");
        meterRegistry.counter("service.notifications.delete.count");
        meterRegistry.counter("service.notifications.messages.count");
        meterRegistry.counter("service.notifications.messages.delete.count");
        meterRegistry.counter("service.notifications.get.count");
        meterRegistry.counter("service.notifications.messages.get.count");
    }

    // ===========================================================
    //              METODI PRINCIPALI SULLE NOTIFICHE
    // ===========================================================

    /**
     * Crea una nuova notifica. Consentito solo all’admin (verifica in repository).
     * Qui, come esempio, inviamo anche un'email al destinatario della notifica
     * (se la logica di business lo prevede).
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public CompletableFuture<Void> createNotification(Notifications notification) {
        logger.info("Creazione nuova notifica: {}", notification);
        Span span = tracer.spanBuilder("createNotification").startSpan();
        meterRegistry.counter("service.notifications.create.count").increment();

        // Eseguiamo la logica in modo asincrono, e attendiamo il risultato dal repository
        return CompletableFuture.runAsync(() -> {
            try {
                // 1) Creiamo la notifica su Firebase (in modo asincrono)
                notificationsRep.createNotification(notification).get();

                // 2) Se vuoi inviare un'email automatica al destinatario, puoi farlo qui
                //    Esempio: l'oggetto Notifications ha un campo "destinatario" = email
                //    Se in realtà "destinatario" è uno username, serve una mappatura a email.
                String toEmail = notification.getDestinatario(); // supponiamo sia un email
                if (toEmail != null && !toEmail.isEmpty()) {
                    String subject = "Nuova notifica per te!";
                    String body = buildEmailBody(notification); 
                    // isHtml = false => invia come testo semplice, o true se vuoi HTML
                    emailService.sendEmail(toEmail, subject, body, false);
                }

            } catch (Exception e) {
                throw new RuntimeException("Errore durante la creazione della notifica", e);
            } finally {
                span.end();
            }
        });
    }

    /**
     * Esempio di metodo (privato) che costruisce un corpo email
     * partendo dall'oggetto Notifications
     */
    private String buildEmailBody(Notifications notification) {
        // Puoi personalizzare il testo come preferisci
        StringBuilder sb = new StringBuilder();
        sb.append("Ciao, hai ricevuto una nuova notifica!\n\n");
        sb.append("ID Notifica: ").append(notification.getId()).append("\n");
        sb.append("Tipo: ").append(notification.getType()).append("\n");
        sb.append("Data/Ora: ").append(notification.getTimestamp()).append("\n\n");
        sb.append("Messaggi:\n");
        if (notification.getMessages() != null) {
            for (Notifications.Message msg : notification.getMessages()) {
                sb.append("- ").append(msg.getTitolo())
                  .append(": ").append(msg.getCorpo())
                  .append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Aggiorna una notifica esistente. Solo l’admin.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#notification.id")
    public CompletableFuture<Void> updateNotification(Notifications notification) {
        logger.info("Aggiornamento notifica: {}", notification);
        Span span = tracer.spanBuilder("updateNotification").startSpan();
        meterRegistry.counter("service.notifications.update.count").increment();

        return CompletableFuture.runAsync(() -> {
            try {
                notificationsRep.updateNotification(notification).get();
            } catch (Exception e) {
                throw new RuntimeException("Errore durante l'aggiornamento della notifica", e);
            } finally {
                span.end();
            }
        });
    }

    /**
     * Recupera una notifica per ID. 
     * Visualizzazione consentita all’admin o al destinatario.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#notificationId")
    public CompletableFuture<Notifications> getNotificationById(String notificationId) {
        logger.info("Recupero notifica per ID: {}", notificationId);
        Span span = tracer.spanBuilder("getNotificationById").startSpan();
        meterRegistry.counter("service.notifications.get.count").increment();

        try {
            return notificationsRep.selectNotificationById(notificationId)
                    .whenComplete((res, ex) -> span.end());
        } catch (Exception e) {
            span.end();
            throw new RuntimeException("Errore durante il recupero della notifica", e);
        }
    }

    /**
     * Elimina una notifica esistente. Solo l’admin.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#notificationId")
    public CompletableFuture<Void> deleteNotification(String notificationId) {
        logger.info("Eliminazione notifica per ID: {}", notificationId);
        Span span = tracer.spanBuilder("deleteNotification").startSpan();
        meterRegistry.counter("service.notifications.delete.count").increment();

        return CompletableFuture.runAsync(() -> {
            try {
                notificationsRep.deleteNotification(notificationId).get();
            } catch (Exception e) {
                throw new RuntimeException("Errore durante l'eliminazione della notifica", e);
            } finally {
                span.end();
            }
        });
    }

    // ===========================================================
    //         OPERAZIONI SUI MESSAGGI DELLA NOTIFICA
    // ===========================================================

    /**
     * Aggiunge un messaggio a una notifica esistente. Solo l’admin.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#notificationId")
    public CompletableFuture<Void> addMessageToNotification(String notificationId, Message message) {
        logger.info("Aggiunta messaggio alla notifica: notificationId={}, message={}", notificationId, message);
        Span span = tracer.spanBuilder("addMessageToNotification").startSpan();
        meterRegistry.counter("service.notifications.messages.count").increment();

        return CompletableFuture.runAsync(() -> {
            try {
                notificationsRep.createMessageForNotification(notificationId, message).get();
            } catch (Exception e) {
                throw new RuntimeException("Errore durante l'aggiunta del messaggio alla notifica", e);
            } finally {
                span.end();
            }
        });
    }

    /**
     * Restituisce la lista di messaggi di una notifica,
     * se l’utente è admin o destinatario.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#notificationId")
    public CompletableFuture<List<Message>> getMessagesForNotification(String notificationId) {
        logger.info("Recupero messaggi per notifica: {}", notificationId);
        Span span = tracer.spanBuilder("getMessagesForNotification").startSpan();
        meterRegistry.counter("service.notifications.messages.get.count").increment();

        try {
            return notificationsRep.selectMessagesByNotification(notificationId)
                    .whenComplete((res, ex) -> span.end());
        } catch (Exception e) {
            span.end();
            throw new RuntimeException("Errore durante il recupero dei messaggi della notifica", e);
        }
    }

    /**
     * Elimina un singolo messaggio da una notifica, se l’utente è admin.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#notificationId")
    public CompletableFuture<Void> deleteMessageFromNotification(String notificationId, String messageId) {
        logger.info("Eliminazione messaggio da notifica: notificationId={}, messageId={}", notificationId, messageId);
        Span span = tracer.spanBuilder("deleteMessageFromNotification").startSpan();
        meterRegistry.counter("service.notifications.messages.delete.count").increment();

        return CompletableFuture.runAsync(() -> {
            try {
                notificationsRep.deleteMessageFromNotification(notificationId, messageId).get();
            } catch (Exception e) {
                throw new RuntimeException("Errore durante l'eliminazione del messaggio dalla notifica", e);
            } finally {
                span.end();
            }
        });
    }

    // ===========================================================
    //    ESEMPIO DI METODO DEDICATO PER INVIARE EMAIL MANUALMENTE
    // ===========================================================

    /**
     * Esempio di metodo per inviare un'email personalizzata
     * riguardo una notifica esistente.
     *
     * @param notificationId L'ID della notifica da cui prendere informazioni
     * @param toEmail        Email del destinatario
     * @param subject        Oggetto dell'email
     * @param content        Contenuto dell'email
     * @param isHtml         Se true, invia l'email in formato HTML
     * @return CompletableFuture<Void> per gestire l'asincronia
     */
    public CompletableFuture<Void> sendEmailForNotification(
            String notificationId,
            String toEmail,
            String subject,
            String content,
            boolean isHtml) {

        logger.info("Invio email per notifica: notificationId={}, toEmail={}", notificationId, toEmail);
        Span span = tracer.spanBuilder("sendEmailForNotification").startSpan();

        return notificationsRep.selectNotificationById(notificationId)
                .thenAccept(notification -> {
                    try {
                        meterRegistry.counter("service.notifications.email.count").increment();

                        // Esempio: potresti arricchire "content" con info dal notification
                        // Oppure mandare l'email "così com'è"
                        emailService.sendEmail(toEmail, subject, content, isHtml);

                    } catch (Exception e) {
                        logger.error("Errore durante l'invio email per la notifica {}: {}", notificationId, e.getMessage());
                        throw new RuntimeException("Errore invio email per la notifica", e);
                    } finally {
                        span.end();
                    }
                });
    }
}
