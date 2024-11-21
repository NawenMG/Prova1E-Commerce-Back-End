package com.prova.e_commerce.dbRT.repository.classi;

import com.prova.e_commerce.dbRT.model.ChatSystem;
import com.prova.e_commerce.dbRT.model.ChatSystem.Message;
import com.prova.e_commerce.dbRT.repository.interfacce.ChatSystemRep;
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
    private  DatabaseReference databaseReference;

    // ==============================
    // Operazioni sulle Chat
    // ==============================

    // Crea una nuova chat
    public ApiFuture<Void> createChat(ChatSystem chat) { // Le interfacce ApiFuture sono utilizzate per la gestione delle operazioni asincrone
        return databaseReference.child(chat.getId()).setValueAsync(chat);
    }

    // Modifica una chat esistente
    public ApiFuture<Void> updateChat(ChatSystem chat) {
        return databaseReference.child(chat.getId()).setValueAsync(chat);
    }

    // Seleziona una chat per ID
    public CompletableFuture<ChatSystem> selectChatById(String chatId) {
        CompletableFuture<ChatSystem> future = new CompletableFuture<>();
        databaseReference.child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ChatSystem chat = snapshot.getValue(ChatSystem.class);
                future.complete(chat);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });
        return future;
    }

    // Elimina una chat
    public ApiFuture<Void> deleteChat(String chatId) {
        return databaseReference.child(chatId).removeValueAsync();
    }

    // ==============================
    // Operazioni sui Messaggi
    // ==============================

    // Crea un nuovo messaggio
    public ApiFuture<Void> createMessage(String chatId, Message message) {
        DatabaseReference messagesRef = databaseReference.child(chatId).child("messages");
        String messageId = messagesRef.push().getKey(); // Genera ID univoco per il messaggio
        message.setConversationId(messageId); // Imposta l'ID generato
        return messagesRef.child(messageId).setValueAsync(message);
    }

    // Modifica un messaggio esistente
    public ApiFuture<Void> updateMessage(String chatId, String messageId, Message updatedMessage) {
        DatabaseReference messageRef = databaseReference.child(chatId).child("messages").child(messageId);
        return messageRef.setValueAsync(updatedMessage);
    }

    // Seleziona tutti i messaggi di una chat
    public CompletableFuture<List<Message>> selectMessagesByChat(String chatId) {
        CompletableFuture<List<Message>> future = new CompletableFuture<>();
        databaseReference.child(chatId).child("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Message> messages = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Message message = child.getValue(Message.class);
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

    // Elimina un singolo messaggio
    public ApiFuture<Void> deleteMessage(String chatId, String messageId) {
        DatabaseReference messageRef = databaseReference.child(chatId).child("messages").child(messageId);
        return messageRef.removeValueAsync();
    }

    // Elimina tutti i messaggi di una chat
    public ApiFuture<Void> deleteAllMessages(String chatId) {
        return databaseReference.child(chatId).child("messages").removeValueAsync();
    }
}
