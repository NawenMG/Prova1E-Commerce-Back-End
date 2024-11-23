package com.prova.e_commerce.dbRT.controller.webSocket;

import com.prova.e_commerce.dbRT.model.Notifications;
import com.prova.e_commerce.dbRT.service.NotificationsService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
public class NotificationsWebSocketController {

    @Autowired
    private NotificationsService notificationsService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Endpoint per creare una nuova notifica
    @MessageMapping("/createNotification")
    public CompletableFuture<Void> createNotification(@Valid Notifications notification) {
        // Chiamata al servizio per creare la notifica
        return notificationsService.createNotification(notification).thenRun(() -> {
            // Dopo aver creato la notifica, inviamo un messaggio a tutti i client
            messagingTemplate.convertAndSend("/topic/notifications", notification);
        });
    }

    // Endpoint per aggiornare una notifica esistente
    @MessageMapping("/updateNotification")
    public CompletableFuture<Void> updateNotification(@Valid Notifications notification) {
        // Chiamata al servizio per aggiornare la notifica
        return notificationsService.updateNotification(notification).thenRun(() -> {
            // Dopo aver aggiornato la notifica, inviamo un messaggio aggiornato a tutti i client
            messagingTemplate.convertAndSend("/topic/notifications", notification);
        });
    }

    // Endpoint per eliminare una notifica
    @MessageMapping("/deleteNotification")
    public CompletableFuture<Void> deleteNotification(String notificationId) {
        // Chiamata al servizio per eliminare la notifica
        return notificationsService.deleteNotification(notificationId).thenRun(() -> {
            // Dopo aver eliminato la notifica, inviamo un messaggio di eliminazione
            messagingTemplate.convertAndSend("/topic/notifications", notificationId);
        });
    }

    // Endpoint per aggiungere un messaggio a una notifica
    @MessageMapping("/addMessageToNotification")
    public CompletableFuture<Void> addMessageToNotification(String notificationId, @Valid Notifications.Message message) {
        // Chiamata al servizio per aggiungere un messaggio alla notifica
        return notificationsService.addMessageToNotification(notificationId, message).thenRun(() -> {
            // Dopo aver aggiunto il messaggio, inviamo la notifica aggiornata a tutti i client
            CompletableFuture<List<Notifications.Message>> messagesFuture = notificationsService.getMessagesForNotification(notificationId);
            messagesFuture.thenAccept(messages -> {
                messagingTemplate.convertAndSend("/topic/notifications/" + notificationId, messages);
            });
        });
    }

    // Endpoint per recuperare i messaggi di una notifica
    @MessageMapping("/getMessagesForNotification")
    public CompletableFuture<Void> getMessagesForNotification(String notificationId) {
        // Chiamata al servizio per ottenere tutti i messaggi di una notifica
        return notificationsService.getMessagesForNotification(notificationId).thenAccept(messages -> {
            // Invia i messaggi a tutti i client
            messagingTemplate.convertAndSend("/topic/notifications/" + notificationId, messages);
        });
    }

    // Endpoint per eliminare un messaggio da una notifica
    @MessageMapping("/deleteMessageFromNotification")
    public CompletableFuture<Void> deleteMessageFromNotification(String notificationId, String messageId) {
        // Chiamata al servizio per eliminare il messaggio dalla notifica
        return notificationsService.deleteMessageFromNotification(notificationId, messageId).thenRun(() -> {
            // Dopo aver eliminato il messaggio, inviamo la lista aggiornata dei messaggi
            CompletableFuture<List<Notifications.Message>> messagesFuture = notificationsService.getMessagesForNotification(notificationId);
            messagesFuture.thenAccept(messages -> {
                messagingTemplate.convertAndSend("/topic/notifications/" + notificationId, messages);
            });
        });
    }
}
