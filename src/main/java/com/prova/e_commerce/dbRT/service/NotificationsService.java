package com.prova.e_commerce.dbRT.service;

import com.prova.e_commerce.dbRT.model.Notifications;
import com.prova.e_commerce.dbRT.repository.interfacce.NotificationsRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificationsService {

    @Autowired
    private NotificationsRep notificationsRep;

    // Crea una nuova notifica
    public CompletableFuture<Void> createNotification(Notifications notification) {
        return CompletableFuture.runAsync(() -> {
            try {
                notificationsRep.createNotification(notification).get();
            } catch (Exception e) {
                throw new RuntimeException("Errore durante la creazione della notifica", e);
            }
        });
    }

    // Modifica una notifica esistente
    public CompletableFuture<Void> updateNotification(Notifications notification) {
        return CompletableFuture.runAsync(() -> {
            try {
                notificationsRep.updateNotification(notification).get();
            } catch (Exception e) {
                throw new RuntimeException("Errore durante l'aggiornamento della notifica", e);
            }
        });
    }

    // Recupera una notifica per ID
    public CompletableFuture<Notifications> getNotificationById(String notificationId) {
        return notificationsRep.selectNotificationById(notificationId);
    }

    // Elimina una notifica
    public CompletableFuture<Void> deleteNotification(String notificationId) {
        return CompletableFuture.runAsync(() -> {
            try {
                notificationsRep.deleteNotification(notificationId).get();
            } catch (Exception e) {
                throw new RuntimeException("Errore durante l'eliminazione della notifica", e);
            }
        });
    }

    // Aggiungi un messaggio a una notifica
    public CompletableFuture<Void> addMessageToNotification(String notificationId, Notifications.Message message) {
        return CompletableFuture.runAsync(() -> {
            try {
                notificationsRep.createMessageForNotification(notificationId, message).get();
            } catch (Exception e) {
                throw new RuntimeException("Errore durante l'aggiunta del messaggio alla notifica", e);
            }
        });
    }

    // Recupera tutti i messaggi di una notifica
    public CompletableFuture<List<Notifications.Message>> getMessagesForNotification(String notificationId) {
        return notificationsRep.selectMessagesByNotification(notificationId);
    }

    // Elimina un messaggio da una notifica
    public CompletableFuture<Void> deleteMessageFromNotification(String notificationId, String messageId) {
        return CompletableFuture.runAsync(() -> {
            try {
                notificationsRep.deleteMessageFromNotification(notificationId, messageId).get();
            } catch (Exception e) {
                throw new RuntimeException("Errore durante l'eliminazione del messaggio dalla notifica", e);
            }
        });
    }
}
