package com.prova.e_commerce.dbRT.repository.classi;

import com.prova.e_commerce.dbRT.model.ChatSystem;
import com.prova.e_commerce.dbRT.model.ChatSystem.Message;
import com.prova.e_commerce.dbRT.repository.interfacce.ChatSystemRep;
import com.prova.e_commerce.security.security1.SecurityUtils;
import com.google.api.core.ApiFuture;
import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class ChatSystemRepImp implements ChatSystemRep {

    @Autowired
    private DatabaseReference databaseReference;

    // ===========================================================
    //                     OPERAZIONI SULLA CHAT
    // ===========================================================

    /**
     * Crea una nuova chat. Parte dall'utente corrente (ricavato da SecurityUtils)
     * e da un oggetto ChatSystem che contiene la lista dei partecipanti.
     *
     * @param chat L'oggetto ChatSystem da salvare (con eventuali partecipanti).
     * @return CompletableFuture<ChatSystem> con la chat salvata.
     */
    public CompletableFuture<ChatSystem> createChat(ChatSystem chat) {
        CompletableFuture<ChatSystem> future = new CompletableFuture<>();

        // Recupera l'utente corrente da Spring Security
        String currentUserId = SecurityUtils.getCurrentUsername();

        // Se il creator non è già nella lista dei partecipanti, lo aggiungiamo
        if (!chat.getParticipants().contains(currentUserId)) {
            chat.getParticipants().add(currentUserId);
        }

        // Genera un nuovo ID per la chat se non esiste
        if (chat.getId() == null || chat.getId().isEmpty()) {
            chat.setId(databaseReference.push().getKey());
        }

        // Salva la chat su Firebase Realtime Database
        ApiFuture<Void> apiFuture = databaseReference.child(chat.getId()).setValueAsync(chat);
        apiFuture.addListener(() -> {
            try {
                apiFuture.get();
                future.complete(chat);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, Runnable::run);

        return future;
    }

    /**
     * Aggiorna una chat esistente (es. modificare i partecipanti o il nome).
     * In questo esempio, chiunque sia tra i partecipanti può effettuare l'update.
     */
    public CompletableFuture<Void> updateChat(ChatSystem updatedChat) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Recupera l'utente corrente
        String currentUserId = SecurityUtils.getCurrentUsername();

        // Recupera prima la chat corrente per verificare l'esistenza e i permessi
        databaseReference.child(updatedChat.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ChatSystem existingChat = snapshot.getValue(ChatSystem.class);
                if (existingChat == null) {
                    future.completeExceptionally(new RuntimeException("Chat non trovata"));
                    return;
                }

                // Controllo di base: l'utente deve far parte dei partecipanti
                if (!existingChat.getParticipants().contains(currentUserId)) {
                    future.completeExceptionally(new SecurityException("Non sei un partecipante di questa chat."));
                    return;
                }

                // Procede con l'aggiornamento su Firebase
                ApiFuture<Void> apiFuture = databaseReference
                        .child(updatedChat.getId())
                        .setValueAsync(updatedChat);

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
     * Seleziona una chat per ID, verificando che l'utente corrente
     * sia tra i partecipanti (logica di visualizzazione).
     */
    public CompletableFuture<ChatSystem> selectChatById(String chatId) {
        CompletableFuture<ChatSystem> future = new CompletableFuture<>();

        // Utente corrente
        String currentUserId = SecurityUtils.getCurrentUsername();

        databaseReference.child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ChatSystem chat = snapshot.getValue(ChatSystem.class);
                if (chat == null) {
                    future.completeExceptionally(new RuntimeException("Chat non trovata"));
                    return;
                }

                // Controllo: l'utente deve essere un partecipante
                if (!chat.getParticipants().contains(currentUserId)) {
                    future.completeExceptionally(new SecurityException("Non puoi visualizzare questa chat."));
                    return;
                }

                future.complete(chat);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });

        return future;
    }

    /**
     * Elimina una chat. In questo esempio, chiunque sia partecipante può eliminarla.
     */
    public CompletableFuture<Void> deleteChat(String chatId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Utente corrente
        String currentUserId = SecurityUtils.getCurrentUsername();

        databaseReference.child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ChatSystem chat = snapshot.getValue(ChatSystem.class);
                if (chat == null) {
                    future.completeExceptionally(new RuntimeException("Chat non trovata"));
                    return;
                }

                // Controllo: deve essere un partecipante
                if (!chat.getParticipants().contains(currentUserId)) {
                    future.completeExceptionally(new SecurityException("Non hai i permessi per eliminare questa chat."));
                    return;
                }

                ApiFuture<Void> deleteFuture = databaseReference.child(chatId).removeValueAsync();
                deleteFuture.addListener(() -> {
                    try {
                        deleteFuture.get();
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
    //                   OPERAZIONI SUI MESSAGGI
    // ===========================================================

    /**
     * Crea un nuovo messaggio in una chat. 
     * In questo esempio, solo i partecipanti possono inviare messaggi.
     */
    public CompletableFuture<Message> createMessage(String chatId, Message message) {
        CompletableFuture<Message> future = new CompletableFuture<>();

        // Utente corrente
        String currentUserId = SecurityUtils.getCurrentUsername();

        // Verifica che la chat esista e che l'utente corrente sia un partecipante
        databaseReference.child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ChatSystem chat = snapshot.getValue(ChatSystem.class);
                if (chat == null) {
                    future.completeExceptionally(new RuntimeException("Chat non trovata"));
                    return;
                }

                if (!chat.getParticipants().contains(currentUserId)) {
                    future.completeExceptionally(new SecurityException("Non sei un partecipante di questa chat."));
                    return;
                }

                // Prepara il riferimento ai messaggi
                DatabaseReference messagesRef = databaseReference.child(chatId).child("messages");

                // Genera un nuovo ID per il messaggio
                String messageId = messagesRef.push().getKey();

                // Imposta campi del messaggio
                message.setConversationId(messageId);
                message.setSender(currentUserId); // Mittente = utente corrente

                // Salvataggio del messaggio
                ApiFuture<Void> apiFuture = messagesRef.child(messageId).setValueAsync(message);
                apiFuture.addListener(() -> {
                    try {
                        apiFuture.get();
                        future.complete(message);
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
     * Modifica un messaggio esistente. Solo il mittente (sender) può modificarlo.
     */
    public CompletableFuture<Void> updateMessage(String chatId, String messageId, String updatedText) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Utente corrente
        String currentUserId = SecurityUtils.getCurrentUsername();

        DatabaseReference messageRef = databaseReference.child(chatId).child("messages").child(messageId);
        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Message existingMessage = snapshot.getValue(Message.class);
                if (existingMessage == null) {
                    future.completeExceptionally(new RuntimeException("Messaggio non trovato."));
                    return;
                }

                // Verifica che chi vuole modificare sia il mittente
                if (!existingMessage.getSender().equals(currentUserId)) {
                    future.completeExceptionally(new SecurityException("Non puoi modificare un messaggio che non hai inviato."));
                    return;
                }

                // Aggiorna il testo del messaggio
                existingMessage.setText(updatedText);

                ApiFuture<Void> apiFuture = messageRef.setValueAsync(existingMessage);
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
     * Seleziona tutti i messaggi di una chat, ammesso che l'utente
     * sia tra i partecipanti.
     */
    public CompletableFuture<List<Message>> selectMessagesByChat(String chatId) {
        CompletableFuture<List<Message>> future = new CompletableFuture<>();

        // Utente corrente
        String currentUserId = SecurityUtils.getCurrentUsername();

        // Verifichiamo che la chat esista e che l'utente corrente sia un partecipante
        databaseReference.child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot chatSnapshot) {
                ChatSystem chat = chatSnapshot.getValue(ChatSystem.class);
                if (chat == null) {
                    future.completeExceptionally(new RuntimeException("Chat non trovata"));
                    return;
                }

                if (!chat.getParticipants().contains(currentUserId)) {
                    future.completeExceptionally(new SecurityException("Non sei un partecipante di questa chat."));
                    return;
                }

                // Recuperiamo i messaggi della chat
                DatabaseReference messagesRef = chatSnapshot.getRef().child("messages");
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
     * Elimina un singolo messaggio, se e solo se l'utente corrente è il mittente.
     */
    public CompletableFuture<Void> deleteMessage(String chatId, String messageId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Utente corrente
        String currentUserId = SecurityUtils.getCurrentUsername();

        DatabaseReference messageRef = databaseReference.child(chatId).child("messages").child(messageId);
        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Message existingMessage = snapshot.getValue(Message.class);
                if (existingMessage == null) {
                    future.completeExceptionally(new RuntimeException("Messaggio non trovato"));
                    return;
                }

                // Solo il sender può eliminarlo
                if (!existingMessage.getSender().equals(currentUserId)) {
                    future.completeExceptionally(new SecurityException("Non puoi eliminare un messaggio non tuo."));
                    return;
                }

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

    /**
     * Elimina tutti i messaggi di una chat.
     * In questo esempio, chiunque dei partecipanti può farlo.
     */
    public CompletableFuture<Void> deleteAllMessages(String chatId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Utente corrente
        String currentUserId = SecurityUtils.getCurrentUsername();

        databaseReference.child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ChatSystem chat = snapshot.getValue(ChatSystem.class);
                if (chat == null) {
                    future.completeExceptionally(new RuntimeException("Chat non trovata"));
                    return;
                }

                // Controllo di base: deve essere un partecipante
                if (!chat.getParticipants().contains(currentUserId)) {
                    future.completeExceptionally(
                            new SecurityException("Non hai i permessi per eliminare i messaggi di questa chat."));
                    return;
                }

                ApiFuture<Void> apiFuture = databaseReference.child(chatId).child("messages").removeValueAsync();
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
