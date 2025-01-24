package com.prova.e_commerce.dbRT.repository.classi;

import com.prova.e_commerce.dbRT.model.Notifications;
import com.prova.e_commerce.dbRT.model.Notifications.Message;
import com.prova.e_commerce.dbRT.repository.interfacce.NotificationsRep;
import com.prova.e_commerce.security.security1.SecurityUtils;
import com.google.api.core.ApiFuture;
import com.google.firebase.database.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Repository
public class NotificationsRepImp implements NotificationsRep {

    @Autowired
    private DatabaseReference databaseReference;

    /**
     * Metodo di utilità per verificare se l'utente corrente possiede il ruolo "ROLE_ADMIN".
     */
    private boolean isCurrentUserAdmin() {
        Set<String> roles = SecurityUtils.getCurrentUserRoles();
        return roles.contains("ROLE_ADMIN");
    }

    /**
     * Restituisce lo username (di solito l'email o un identificativo univoco)
     * dell'utente attualmente autenticato.
     */
    private String getCurrentUserId() {
        return SecurityUtils.getCurrentUsername();
    }

    // ===========================================================
    //                 METODI PRINCIPALI SULLE NOTIFICHE
    // ===========================================================

    /**
     * Crea una nuova notifica. 
     * Consentito solo all'admin.
     *
     * @param notification  Oggetto Notifications con i dati da salvare.
     * @return CompletableFuture<Notifications> con la notifica creata.
     */
    @Override
    public CompletableFuture<Notifications> createNotification(Notifications notification) {
        CompletableFuture<Notifications> future = new CompletableFuture<>();

        // Controllo se l'utente corrente è admin
        if (!isCurrentUserAdmin()) {
            future.completeExceptionally(new SecurityException("Solo l'admin può creare notifiche."));
            return future;
        }

        // Se l'ID della notifica non è presente, generiamo un nuovo ID
        if (notification.getId() == null || notification.getId().isEmpty()) {
            String newId = databaseReference.push().getKey();
            notification.setId(newId);
        }

        // Salviamo la notifica in Firebase
        ApiFuture<Void> apiFuture = databaseReference.child(notification.getId()).setValueAsync(notification);
        apiFuture.addListener(() -> {
            try {
                apiFuture.get();  // attende l'operazione
                future.complete(notification);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, Runnable::run);

        return future;
    }

    /**
     * Aggiorna una notifica esistente (es. per cambiare il tipo, aggiungere messaggi, ecc.).
     * In questo esempio, solo l'admin può farlo.
     *
     * @param notification  Nuova versione della notifica.
     * @return CompletableFuture<Void>
     */
    @Override
    public CompletableFuture<Void> updateNotification(Notifications notification) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (!isCurrentUserAdmin()) {
            future.completeExceptionally(new SecurityException("Solo l'admin può aggiornare le notifiche."));
            return future;
        }

        // Verifichiamo che la notifica esista prima di aggiornarla
        databaseReference.child(notification.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Notifications existing = snapshot.getValue(Notifications.class);
                        if (existing == null) {
                            future.completeExceptionally(new RuntimeException("Notifica non trovata."));
                            return;
                        }

                        // Procede con l'aggiornamento
                        ApiFuture<Void> apiFuture = databaseReference
                                .child(notification.getId())
                                .setValueAsync(notification);

                        apiFuture.addListener(() -> {
                            try {
                                apiFuture.get();
                                future.complete(null);
                            } catch (Exception e) {
                                future.completeExceptionally(e);
                            }
                        }, Runnable::run);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(new RuntimeException(error.getMessage()));
                    }
                });

        return future;
    }

    /**
     * Restituisce una notifica a partire dal suo ID.
     * Visualizzazione consentita all'admin oppure all'utente destinatario.
     *
     * @param notificationId  ID della notifica da recuperare.
     * @return CompletableFuture<Notifications> con la notifica selezionata.
     */
    @Override
    public CompletableFuture<Notifications> selectNotificationById(String notificationId) {
        CompletableFuture<Notifications> future = new CompletableFuture<>();

        String currentUser = getCurrentUserId();
        boolean isAdmin = isCurrentUserAdmin();

        databaseReference.child(notificationId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Notifications notification = snapshot.getValue(Notifications.class);
                        if (notification == null) {
                            future.completeExceptionally(new RuntimeException("Notifica non trovata."));
                            return;
                        }

                        // Se non è admin, deve essere il destinatario della notifica
                        if (!isAdmin && !notification.getDestinatario().equals(currentUser)) {
                            future.completeExceptionally(new SecurityException(
                                    "Non sei autorizzato a visualizzare questa notifica."));
                            return;
                        }

                        future.complete(notification);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(new RuntimeException(error.getMessage()));
                    }
                });

        return future;
    }

    /**
     * Elimina una notifica. In questo esempio, solo l'admin può farlo.
     *
     * @param notificationId ID della notifica da eliminare.
     * @return CompletableFuture<Void>
     */
    @Override
    public CompletableFuture<Void> deleteNotification(String notificationId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Controllo se l'utente è admin
        if (!isCurrentUserAdmin()) {
            future.completeExceptionally(new SecurityException("Solo l'admin può eliminare le notifiche."));
            return future;
        }

        // Verifichiamo che la notifica esista
        databaseReference.child(notificationId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Notifications notification = snapshot.getValue(Notifications.class);
                        if (notification == null) {
                            future.completeExceptionally(new RuntimeException("Notifica non trovata."));
                            return;
                        }

                        ApiFuture<Void> apiFuture = databaseReference.child(notificationId).removeValueAsync();
                        apiFuture.addListener(() -> {
                            try {
                                apiFuture.get();
                                future.complete(null);
                            } catch (Exception e) {
                                future.completeExceptionally(e);
                            }
                        }, Runnable::run);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(new RuntimeException(error.getMessage()));
                    }
                });

        return future;
    }

    // ===========================================================
    //           OPERAZIONI SUI MESSAGGI DELLA NOTIFICA
    // ===========================================================

    /**
     * Crea un nuovo messaggio all'interno di una notifica esistente.
     * Solo l'admin può inviare messaggi.
     *
     * @param notificationId ID della notifica in cui inserire il messaggio.
     * @param message        Il nuovo messaggio da inserire.
     * @return CompletableFuture<Void>
     */
    @Override
    public CompletableFuture<Void> createMessageForNotification(String notificationId, Message message) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Controllo se l'utente è admin
        if (!isCurrentUserAdmin()) {
            future.completeExceptionally(new SecurityException("Solo l'admin può inviare messaggi nelle notifiche."));
            return future;
        }

        // Verifichiamo che la notifica esista
        databaseReference.child(notificationId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Notifications notification = snapshot.getValue(Notifications.class);
                        if (notification == null) {
                            future.completeExceptionally(new RuntimeException("Notifica non trovata."));
                            return;
                        }

                        // Creiamo un nuovo riferimento per il messaggio
                        DatabaseReference messagesRef = databaseReference.child(notificationId).child("messages");
                        String messageId = messagesRef.push().getKey(); // Generiamo un ID univoco

                        // Salviamo il messaggio
                        ApiFuture<Void> apiFuture = messagesRef.child(messageId).setValueAsync(message);
                        apiFuture.addListener(() -> {
                            try {
                                apiFuture.get();
                                future.complete(null);
                            } catch (Exception e) {
                                future.completeExceptionally(e);
                            }
                        }, Runnable::run);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(new RuntimeException(error.getMessage()));
                    }
                });

        return future;
    }

    /**
     * Restituisce la lista di messaggi di una notifica,
     * ammesso che l'utente richiedente sia admin o destinatario della notifica.
     *
     * @param notificationId  ID della notifica.
     * @return CompletableFuture<List<Notifications.Message>> con la lista dei messaggi.
     */
    @Override
    public CompletableFuture<List<Message>> selectMessagesByNotification(String notificationId) {
        CompletableFuture<List<Message>> future = new CompletableFuture<>();

        String currentUser = getCurrentUserId();
        boolean isAdmin = isCurrentUserAdmin();

        // Prima recuperiamo la notifica per verificare i permessi
        databaseReference.child(notificationId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Notifications notification = snapshot.getValue(Notifications.class);
                        if (notification == null) {
                            future.completeExceptionally(new RuntimeException("Notifica non trovata."));
                            return;
                        }

                        // Se non è admin, deve essere il destinatario
                        if (!isAdmin && !notification.getDestinatario().equals(currentUser)) {
                            future.completeExceptionally(
                                    new SecurityException("Non sei autorizzato a visualizzare questi messaggi."));
                            return;
                        }

                        // Carichiamo i messaggi
                        DatabaseReference messagesRef = snapshot.getRef().child("messages");
                        messagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot messagesSnapshot) {
                                List<Message> messages = new ArrayList<>();
                                for (DataSnapshot child : messagesSnapshot.getChildren()) {
                                    Message msg = child.getValue(Message.class);
                                    if (msg != null) {
                                        messages.add(msg);
                                    }
                                }
                                future.complete(messages);
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                future.completeExceptionally(new RuntimeException(error.getMessage()));
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(new RuntimeException(error.getMessage()));
                    }
                });

        return future;
    }

    /**
     * Elimina un singolo messaggio da una notifica.
     * In questo esempio, solo l'admin può farlo.
     *
     * @param notificationId ID della notifica a cui appartiene il messaggio.
     * @param messageId      Chiave del messaggio da rimuovere.
     * @return CompletableFuture<Void>
     */
    @Override
    public CompletableFuture<Void> deleteMessageFromNotification(String notificationId, String messageId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Verifichiamo se l'utente corrente è admin
        if (!isCurrentUserAdmin()) {
            future.completeExceptionally(new SecurityException("Solo l'admin può eliminare i messaggi nelle notifiche."));
            return future;
        }

        // Verifichiamo che la notifica esista
        databaseReference.child(notificationId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Notifications notification = snapshot.getValue(Notifications.class);
                        if (notification == null) {
                            future.completeExceptionally(new RuntimeException("Notifica non trovata."));
                            return;
                        }

                        DatabaseReference messageRef = snapshot.getRef().child("messages").child(messageId);
                        ApiFuture<Void> apiFuture = messageRef.removeValueAsync();
                        apiFuture.addListener(() -> {
                            try {
                                apiFuture.get();
                                future.complete(null);
                            } catch (Exception e) {
                                future.completeExceptionally(e);
                            }
                        }, Runnable::run);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(new RuntimeException(error.getMessage()));
                    }
                });

        return future;
    }
}
