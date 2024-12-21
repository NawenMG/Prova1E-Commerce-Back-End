package com.prova.e_commerce.dbRT.controller.webSocket;

import com.google.api.core.ApiFuture;
import com.prova.e_commerce.dbRT.model.ChatSystem;
import com.prova.e_commerce.dbRT.model.ChatSystem.Message;
import com.prova.e_commerce.dbRT.service.ChatSystemService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
/* import org.springframework.web.multipart.MultipartFile;

import java.io.IOException; */
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

    @MessageMapping("/createChat")
    @SendTo("/topic/chats")
    public CompletableFuture<ChatSystem> createChat(@Valid ChatSystem chat) {
        ApiFuture<Void> future = chatSystemService.createChat(chat);
        return CompletableFuture.supplyAsync(() -> {
            try {
                future.get();
                return chat;
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Errore durante la creazione della chat", e);
            }
        });
    }

    @MessageMapping("/getChat")
    @SendTo("/topic/chat")
    public CompletableFuture<ChatSystem> getChat(String chatId) {
        return chatSystemService.selectChatById(chatId);
    }

    @MessageMapping("/updateChat")
    @SendTo("/topic/chats")
    public CompletableFuture<ChatSystem> updateChat(@Valid ChatSystem chat) {
        ApiFuture<Void> future = chatSystemService.updateChat(chat);
        return CompletableFuture.supplyAsync(() -> {
            try {
                future.get();
                return chat;
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Errore durante l'aggiornamento della chat", e);
            }
        });
    }

    @MessageMapping("/deleteChat")
    @SendTo("/topic/chats")
    public CompletableFuture<String> deleteChat(String chatId) {
        ApiFuture<Void> future = chatSystemService.deleteChat(chatId);
        return CompletableFuture.supplyAsync(() -> {
            try {
                future.get();
                return "Chat eliminata con successo";
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Errore durante l'eliminazione della chat", e);
            }
        });
    }

    // ==============================
    // Operazioni sui Messaggi
    // ==============================

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public CompletableFuture<Message> sendMessage(@Valid Message message, String chatId) {
        ApiFuture<Void> future = chatSystemService.createMessage(chatId, message);
        return CompletableFuture.supplyAsync(() -> {
            try {
                future.get();
                return message;
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Errore durante l'invio del messaggio", e);
            }
        });
    }

    @MessageMapping("/updateMessage")
    @SendTo("/topic/messages")
    public CompletableFuture<Message> updateMessage(String chatId, String messageId, @Valid Message updatedMessage) {
        ApiFuture<Void> future = chatSystemService.updateMessage(chatId, messageId, updatedMessage);
        return CompletableFuture.supplyAsync(() -> {
            try {
                future.get();
                return updatedMessage;
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Errore durante l'aggiornamento del messaggio", e);
            }
        });
    }

    @MessageMapping("/deleteMessage")
    @SendTo("/topic/messages")
    public CompletableFuture<String> deleteMessage(String chatId, String messageId) {
        ApiFuture<Void> future = chatSystemService.deleteMessage(chatId, messageId);
        return CompletableFuture.supplyAsync(() -> {
            try {
                future.get();
                return "Messaggio eliminato con successo";
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Errore durante l'eliminazione del messaggio", e);
            }
        });
    }

    @MessageMapping("/getMessages")
    @SendTo("/topic/messages")
    public CompletableFuture<List<Message>> getMessages(String chatId) {
        return chatSystemService.selectMessagesByChat(chatId);
    }

    // ==============================
    // Operazioni sui File Allegati
    // ==============================

    /**
     * Carica un file allegato ad un messaggio in una chat.
     */
    /* @MessageMapping("/uploadFile")
    @SendTo("/topic/messages")
    public CompletableFuture<String> uploadFile(String chatId, String messageId, MultipartFile file, String fileType) throws IOException {
        // Carica il file tramite il servizio
        String fileUrl = chatSystemService.uploadFileToMessage(chatId, messageId, file, fileType);

        return CompletableFuture.supplyAsync(() -> {
            try {
                return fileUrl;
            } catch (Exception e) {
                throw new RuntimeException("Errore durante il caricamento del file", e);
            }
        });
    } */

    /**
     * Scarica un file allegato ad un messaggio in una chat.
     */
    /* @MessageMapping("/downloadFile")
    @SendTo("/topic/messages")
    public CompletableFuture<byte[]> downloadFile(String chatId, String messageId, String fileType) throws IOException {
        byte[] fileContent = chatSystemService.downloadFileFromMessage(chatId, messageId, fileType).readAllBytes();
        return CompletableFuture.supplyAsync(() -> fileContent);
    } */

    /**
     * Elimina un file allegato da un messaggio di chat.
     */
    /* @MessageMapping("/deleteFile")
    @SendTo("/topic/messages")
    public CompletableFuture<String> deleteFile(String chatId, String messageId, String fileType) throws IOException {
        chatSystemService.deleteFileFromMessage(chatId, messageId, fileType);
        return CompletableFuture.supplyAsync(() -> "File eliminato con successo");
    } */
}
