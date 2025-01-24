package com.prova.e_commerce.dbRT.repository.interfacce;


import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.prova.e_commerce.dbRT.model.Notifications;

public interface NotificationsRep {
    CompletableFuture<Notifications> createNotification(Notifications notification);
    CompletableFuture<Void> updateNotification(Notifications notification);
    CompletableFuture<Notifications> selectNotificationById(String notificationId);
    CompletableFuture<Void> deleteNotification(String notificationId);
    CompletableFuture<Void> createMessageForNotification(String notificationId, Notifications.Message message);
    CompletableFuture<List<Notifications.Message>> selectMessagesByNotification(String notificationId);
    CompletableFuture<Void> deleteMessageFromNotification(String notificationId, String messageId);
}
