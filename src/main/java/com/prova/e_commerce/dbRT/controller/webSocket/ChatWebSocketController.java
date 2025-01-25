package com.prova.e_commerce.dbRT.controller.webSocket;

import com.prova.e_commerce.dbRT.model.ChatSystem;
import com.prova.e_commerce.dbRT.model.ChatSystem.Message;
import com.prova.e_commerce.dbRT.service.ChatSystemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
@PreAuthorize("hasRole('USER')")
public class ChatWebSocketController {

    @Autowired
    private ChatSystemService chatSystemService;

    // ==============================
    //         Operazioni Chat
    // ==============================

    /**
     * Creazione di una chat.
     * I client inviano un messaggio a "/app/createChat".
     * Riceveranno in broadcast il ChatSystem creato su "/topic/chats".
     */
    @MessageMapping("/createChat")
    @SendTo("/topic/chats")
    public CompletableFuture<ChatSystem> createChat(@Valid ChatSystem chat) {
        // Invoca il service in modo asincrono
        return chatSystemService.createChat(chat)
            .exceptionally(ex -> {
                throw new RuntimeException("Errore durante la creazione della chat", ex);
            });
    }

    /**
     * Recupera i dettagli di una chat per ID.
     * I client inviano un messaggio a "/app/getChat".
     * Riceveranno la chat su "/topic/chat".
     */
    @MessageMapping("/getChat")
    @SendTo("/topic/chat")
    public CompletableFuture<ChatSystem> getChat(String chatId) {
        return chatSystemService.selectChatById(chatId)
            .exceptionally(ex -> {
                throw new RuntimeException("Errore durante il recupero della chat", ex);
            });
    }

    /**
     * Aggiorna una chat esistente.
     * I client inviano a "/app/updateChat".
     * Riceveranno il ChatSystem aggiornato su "/topic/chats".
     */
    @MessageMapping("/updateChat")
    @SendTo("/topic/chats")
    public CompletableFuture<ChatSystem> updateChat(@Valid ChatSystem updatedChat) {
        return chatSystemService.updateChat(updatedChat)
            // `updateChat` restituisce CompletableFuture<Void>, 
            // quindi dobbiamo "consegnare" qualcosa da restituire:
            .thenApply(v -> updatedChat)
            .exceptionally(ex -> {
                throw new RuntimeException("Errore durante l'aggiornamento della chat", ex);
            });
    }

    /**
     * Elimina una chat esistente per ID.
     * I client inviano a "/app/deleteChat".
     * Riceveranno un messaggio di conferma su "/topic/chats".
     */
    @MessageMapping("/deleteChat")
    @SendTo("/topic/chats")
    public CompletableFuture<String> deleteChat(String chatId) {
        return chatSystemService.deleteChat(chatId)
            .thenApply(v -> "Chat eliminata con successo (ID: " + chatId + ")")
            .exceptionally(ex -> {
                throw new RuntimeException("Errore durante l'eliminazione della chat", ex);
            });
    }

    // ==============================
    //      Operazioni Messaggi
    // ==============================

    /**
     * Invio di un nuovo messaggio in una chat.
     * I client inviano a "/app/sendMessage".
     * Riceveranno in broadcast il Messaggio creato su "/topic/messages".
     */
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public CompletableFuture<Message> sendMessage(String chatId, @Valid Message message) {
        return chatSystemService.createMessage(chatId, message)
            .exceptionally(ex -> {
                throw new RuntimeException("Errore durante l'invio del messaggio", ex);
            });
    }

    /**
     * Aggiorna un messaggio esistente.
     * I client inviano a "/app/updateMessage".
     * Riceveranno su "/topic/messages" il Messaggio aggiornato.
     */
    @MessageMapping("/updateMessage")
    @SendTo("/topic/messages")
    public CompletableFuture<Message> updateMessage(String chatId, String messageId, @Valid Message updatedMessage) {
        return chatSystemService.updateMessage(chatId, messageId, updatedMessage.getText())
            .thenApply(v -> updatedMessage)
            .exceptionally(ex -> {
                throw new RuntimeException("Errore durante l'aggiornamento del messaggio", ex);
            });
    }

    /**
     * Elimina un messaggio da una chat.
     * I client inviano a "/app/deleteMessage".
     * Riceveranno un messaggio di conferma su "/topic/messages".
     */
    @MessageMapping("/deleteMessage")
    @SendTo("/topic/messages")
    public CompletableFuture<String> deleteMessage(String chatId, String messageId) {
        return chatSystemService.deleteMessage(chatId, messageId)
            .thenApply(v -> "Messaggio eliminato con successo (ID: " + messageId + ")")
            .exceptionally(ex -> {
                throw new RuntimeException("Errore durante l'eliminazione del messaggio", ex);
            });
    }

    /**
     * Recupera tutti i messaggi di una chat.
     * I client inviano a "/app/getMessages".
     * Riceveranno la lista dei messaggi su "/topic/messages".
     */
    @MessageMapping("/getMessages")
    @SendTo("/topic/messages")
    public CompletableFuture<List<Message>> getMessages(String chatId) {
        return chatSystemService.selectMessagesByChat(chatId)
            .exceptionally(ex -> {
                throw new RuntimeException("Errore durante il recupero dei messaggi", ex);
            });
    }
}
