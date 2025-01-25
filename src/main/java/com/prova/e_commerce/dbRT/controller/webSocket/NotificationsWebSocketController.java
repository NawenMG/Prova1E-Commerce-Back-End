package com.prova.e_commerce.dbRT.controller.webSocket;

import com.prova.e_commerce.dbRT.model.Notifications;
import com.prova.e_commerce.dbRT.model.Notifications.Message;
import com.prova.e_commerce.dbRT.service.NotificationsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.concurrent.CompletableFuture;

/**
 * WebSocket Controller per la gestione delle notifiche.
 * I client comunicano su "/app/..." e ricevono aggiornamenti su "/topic/...".
 */
@Controller
@PreAuthorize("hasRole('USER')")
public class NotificationsWebSocketController {

    @Autowired
    private NotificationsService notificationsService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // ===========================================================
    //          GESTIONE NOTIFICHE (CREATE, UPDATE, DELETE)
    // ===========================================================

    /**
     * Crea una nuova notifica (solo admin).
     * Client invia un messaggio a "/app/createNotification".
     * Se la creazione ha successo, notifichiamo tutti i client su "/topic/notifications".
     *
     * @param notification l'oggetto Notifications da creare
     * @return CompletableFuture<Void> per gestire l'asincronia
     */
    @MessageMapping("/createNotification")
    public CompletableFuture<Void> createNotification(@Valid Notifications notification) {
        return notificationsService.createNotification(notification).thenRun(() -> {
            // Invia agli utenti la notifica appena creata
            messagingTemplate.convertAndSend("/topic/notifications", notification);
        });
    }

    /**
     * Aggiorna una notifica esistente (solo admin).
     * Client invia a "/app/updateNotification".
     * Dopo l'aggiornamento, inviamo a "/topic/notifications" la notifica aggiornata.
     *
     * @param notification l'oggetto Notifications aggiornato
     * @return CompletableFuture<Void> 
     */
    @MessageMapping("/updateNotification")
    public CompletableFuture<Void> updateNotification(@Valid Notifications notification) {
        return notificationsService.updateNotification(notification).thenRun(() -> {
            messagingTemplate.convertAndSend("/topic/notifications", notification);
        });
    }

    /**
     * Elimina una notifica (solo admin).
     * Client invia a "/app/deleteNotification".
     * Dopo l'eliminazione, inviamo su "/topic/notifications" l'ID della notifica eliminata.
     *
     * @param notificationId l'ID della notifica da eliminare
     * @return CompletableFuture<Void>
     */
    @MessageMapping("/deleteNotification")
    public CompletableFuture<Void> deleteNotification(String notificationId) {
        return notificationsService.deleteNotification(notificationId).thenRun(() -> {
            // Invio l'ID notifica eliminata (o un messaggio di conferma)
            messagingTemplate.convertAndSend("/topic/notifications", "Notifica eliminata: " + notificationId);
        });
    }

    // ===========================================================
    //               GESTIONE DEI MESSAGGI
    // ===========================================================

    /**
     * Aggiunge un messaggio a una notifica (solo admin).
     * Client invia a "/app/addMessageToNotification".
     * Successivamente, inviamo la lista aggiornata dei messaggi su "/topic/notifications/{notificationId}".
     */
    @MessageMapping("/addMessageToNotification")
    public CompletableFuture<Void> addMessageToNotification(String notificationId, @Valid Message message) {
        return notificationsService.addMessageToNotification(notificationId, message).thenRun(() -> {
            // Recuperiamo i messaggi aggiornati e li inviamo
            notificationsService.getMessagesForNotification(notificationId).thenAccept(messages -> {
                messagingTemplate.convertAndSend("/topic/notifications/" + notificationId, messages);
            });
        });
    }

    /**
     * Recupera i messaggi di una notifica.
     * Client invia a "/app/getMessagesForNotification".
     * Inviamo i messaggi su "/topic/notifications/{notificationId}".
     */
    @MessageMapping("/getMessagesForNotification")
    public CompletableFuture<Void> getMessagesForNotification(String notificationId) {
        return notificationsService.getMessagesForNotification(notificationId).thenAccept(messages -> {
            messagingTemplate.convertAndSend("/topic/notifications/" + notificationId, messages);
        });
    }

    /**
     * Elimina un singolo messaggio da una notifica (solo admin).
     * Client invia a "/app/deleteMessageFromNotification".
     * Poi inviamo la lista aggiornata dei messaggi su "/topic/notifications/{notificationId}".
     */
    @MessageMapping("/deleteMessageFromNotification")
    public CompletableFuture<Void> deleteMessageFromNotification(String notificationId, String messageId) {
        return notificationsService.deleteMessageFromNotification(notificationId, messageId).thenRun(() -> {
            // Recuperiamo i messaggi aggiornati e li inviamo
            notificationsService.getMessagesForNotification(notificationId).thenAccept(messages -> {
                messagingTemplate.convertAndSend("/topic/notifications/" + notificationId, messages);
            });
        });
    }

    // ===========================================================
    //   ESEMPIO: INVIO EMAIL MANUALE PER UNA NOTIFICA VIA WEBSOCKET
    // ===========================================================

    /**
     * Esempio di endpoint WebSocket per inviare un'email riferita a una notifica.
     * Client invia a "/app/sendEmailForNotification".
     * Non pubblichiamo nulla su "/topic" per default, ma potresti avvisare i client se lo desideri.
     */
    @MessageMapping("/sendEmailForNotification")
    public CompletableFuture<Void> sendEmailForNotification(String notificationId, 
                                                            String toEmail, 
                                                            String subject, 
                                                            String content, 
                                                            boolean isHtml) {
        return notificationsService.sendEmailForNotification(notificationId, toEmail, subject, content, isHtml)
            .thenRun(() -> {
                // Se vuoi avvisare i client che l'email è stata inviata, fallo qui:
                messagingTemplate.convertAndSend("/topic/notifications", 
                    "Email inviata per notifica " + notificationId + " a " + toEmail);
            });
    }
}
