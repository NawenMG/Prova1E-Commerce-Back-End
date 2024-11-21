package com.prova.e_commerce.dbRT.repository.classi;

import com.prova.e_commerce.dbRT.model.Notifications;
import com.prova.e_commerce.dbRT.repository.interfacce.NotificationsRep;
import com.google.api.core.ApiFuture;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class NotificationsRepImp implements NotificationsRep {

    @Autowired
    private DatabaseReference databaseReference;

    // La parte di inizializzazione Firebase viene gestita tramite il FirebaseConfig e non è necessaria più qui.

    // ==============================
    // Operazioni sulle Notifiche
    // ==============================

    // Crea una nuova notifica
    public ApiFuture<Void> createNotification(Notifications notification) {
        return databaseReference.child(notification.getId()).setValueAsync(notification);
    }

    // Modifica una notifica esistente
    public ApiFuture<Void> updateNotification(Notifications notification) {
        return databaseReference.child(notification.getId()).setValueAsync(notification);
    }

    // Seleziona una notifica per ID
    public CompletableFuture<Notifications> selectNotificationById(String notificationId) {
        CompletableFuture<Notifications> future = new CompletableFuture<>();
        databaseReference.child(notificationId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Notifications notification = snapshot.getValue(Notifications.class);
                future.complete(notification);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });
        return future;
    }

    // Elimina una notifica
    public ApiFuture<Void> deleteNotification(String notificationId) {
        return databaseReference.child(notificationId).removeValueAsync();
    }

    // ==============================
    // Operazioni sui Messaggi della Notifica
    // ==============================

    // Crea un nuovo messaggio per una notifica
    public ApiFuture<Void> createMessageForNotification(String notificationId, Notifications.Message message) {
        DatabaseReference messagesRef = databaseReference.child(notificationId).child("messages");
        String messageId = messagesRef.push().getKey(); // Genera ID univoco per il messaggio
        message.setTitolo("Nuovo messaggio");  // Impostiamo un titolo predefinito
        return messagesRef.child(messageId).setValueAsync(message);
    }

    // Seleziona tutti i messaggi di una notifica
    public CompletableFuture<List<Notifications.Message>> selectMessagesByNotification(String notificationId) {
        CompletableFuture<List<Notifications.Message>> future = new CompletableFuture<>();
        databaseReference.child(notificationId).child("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Notifications.Message> messages = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Notifications.Message message = child.getValue(Notifications.Message.class);
                    if (message != null) {
                        messages.add(message);
                    }
                }
                future.complete(messages);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });
        return future;
    }

    // Elimina un messaggio da una notifica
    public ApiFuture<Void> deleteMessageFromNotification(String notificationId, String messageId) {
        DatabaseReference messageRef = databaseReference.child(notificationId).child("messages").child(messageId);
        return messageRef.removeValueAsync();
    }
}
