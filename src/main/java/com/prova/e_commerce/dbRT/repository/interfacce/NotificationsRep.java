package com.prova.e_commerce.dbRT.repository.interfacce;

import com.prova.e_commerce.dbRT.model.Notifications;
import com.google.api.core.ApiFuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NotificationsRep {

    // Operazioni sulle Notifiche

    // Crea una nuova notifica
    ApiFuture<Void> createNotification(Notifications notification);

    // Modifica una notifica esistente
    ApiFuture<Void> updateNotification(Notifications notification);

    // Seleziona una notifica per ID
    CompletableFuture<Notifications> selectNotificationById(String notificationId);

    // Elimina una notifica
    ApiFuture<Void> deleteNotification(String notificationId);

    // Operazioni sui Messaggi della Notifica

    // Crea un nuovo messaggio per una notifica
    ApiFuture<Void> createMessageForNotification(String notificationId, Notifications.Message message);

    // Seleziona tutti i messaggi di una notifica
    CompletableFuture<List<Notifications.Message>> selectMessagesByNotification(String notificationId);

    // Elimina un messaggio da una notifica
    ApiFuture<Void> deleteMessageFromNotification(String notificationId, String messageId);
}
