package com.prova.e_commerce.dbRT.controller.webSocket;

import com.prova.e_commerce.dbRT.model.ChatSystem;
import com.prova.e_commerce.dbRT.model.ChatSystem.Message;
import com.prova.e_commerce.dbRT.service.ChatSystemService;
import com.google.api.core.ApiFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
public class ChatWebSocketController {

    @Autowired
    private ChatSystemService chatSystemService;

    // ==============================
    // Operazioni sulle Chat
    // ==============================

    /**
     * Creazione di una nuova chat.
     */
    @MessageMapping("/createChat")
    @SendTo("/topic/chats")
    public CompletableFuture<ChatSystem> createChat(ChatSystem chat) {
        ApiFuture<Void> future = chatSystemService.createChat(chat);

        return CompletableFuture.supplyAsync(() -> {
            try {
                future.get();  // Aspetta il risultato dell'operazione asincrona
                return chat;   // Ritorna la chat appena creata
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Errore durante la creazione della chat", e);
            }
        });
    }

    /**
     * Recupera una chat esistente.
     */
    @MessageMapping("/getChat")
    @SendTo("/topic/chat")
    public CompletableFuture<ChatSystem> getChat(String chatId) {
        return chatSystemService.selectChatById(chatId);
    }

    /**
     * Modifica una chat esistente.
     */
    @MessageMapping("/updateChat")
    @SendTo("/topic/chats")
    public CompletableFuture<ChatSystem> updateChat(ChatSystem chat) {
        ApiFuture<Void> future = chatSystemService.updateChat(chat);

        return CompletableFuture.supplyAsync(() -> {
            try {
                future.get();  // Aspetta che l'update sia completato
                return chat;   // Ritorna la chat aggiornata
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Errore durante l'aggiornamento della chat", e);
            }
        });
    }

    /**
     * Elimina una chat.
     */
    @MessageMapping("/deleteChat")
    @SendTo("/topic/chats")
    public CompletableFuture<String> deleteChat(String chatId) {
        ApiFuture<Void> future = chatSystemService.deleteChat(chatId);

        return CompletableFuture.supplyAsync(() -> {
            try {
                future.get();  // Aspetta che la chat venga eliminata
                return "Chat eliminata con successo";   // Ritorna un messaggio di conferma
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Errore durante l'eliminazione della chat", e);
            }
        });
    }

    // ==============================
    // Operazioni sui Messaggi
    // ==============================

    /**
     * Creazione di un nuovo messaggio in una chat.
     */
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public CompletableFuture<Message> sendMessage(Message message, String chatId) {
        ApiFuture<Void> future = chatSystemService.createMessage(chatId, message);

        return CompletableFuture.supplyAsync(() -> {
            try {
                future.get();  // Aspetta il risultato dell'operazione asincrona
                return message; // Ritorna il messaggio appena creato
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Errore durante l'invio del messaggio", e);
            }
        });
    }

    /**
     * Modifica un messaggio esistente in una chat.
     */
    @MessageMapping("/updateMessage")
    @SendTo("/topic/messages")
    public CompletableFuture<Message> updateMessage(String chatId, String messageId, Message updatedMessage) {
        ApiFuture<Void> future = chatSystemService.updateMessage(chatId, messageId, updatedMessage);

        return CompletableFuture.supplyAsync(() -> {
            try {
                future.get();  // Aspetta che l'update sia completato
                return updatedMessage;   // Ritorna il messaggio aggiornato
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Errore durante l'aggiornamento del messaggio", e);
            }
        });
    }

    /**
     * Elimina un singolo messaggio in una chat.
     */
    @MessageMapping("/deleteMessage")
    @SendTo("/topic/messages")
    public CompletableFuture<String> deleteMessage(String chatId, String messageId) {
        ApiFuture<Void> future = chatSystemService.deleteMessage(chatId, messageId);

        return CompletableFuture.supplyAsync(() -> {
            try {
                future.get();  // Aspetta che il messaggio venga eliminato
                return "Messaggio eliminato con successo";   // Ritorna un messaggio di conferma
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Errore durante l'eliminazione del messaggio", e);
            }
        });
    }

    /**
     * Recupera tutti i messaggi di una chat.
     */
    @MessageMapping("/getMessages")
    @SendTo("/topic/messages")
    public CompletableFuture<List<Message>> getMessages(String chatId) {
        return chatSystemService.selectMessagesByChat(chatId);
    }
}
