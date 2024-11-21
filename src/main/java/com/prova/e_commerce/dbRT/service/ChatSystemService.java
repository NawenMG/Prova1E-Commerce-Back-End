package com.prova.e_commerce.dbRT.service;

import com.prova.e_commerce.dbRT.model.ChatSystem;
import com.prova.e_commerce.dbRT.model.ChatSystem.Message;
import com.prova.e_commerce.dbRT.repository.interfacce.ChatSystemRep;
import com.prova.e_commerce.storage.S3Service;
import com.google.api.core.ApiFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ChatSystemService {

    @Autowired
    private ChatSystemRep chatSystemRep;

    @Autowired
    private S3Service s3Service;

    // ==============================
    // Operazioni sulle Chat
    // ==============================

    public ApiFuture<Void> createChat(ChatSystem chat) {
        return chatSystemRep.createChat(chat);
    }

    public ApiFuture<Void> updateChat(ChatSystem chat) {
        return chatSystemRep.updateChat(chat);
    }

    public CompletableFuture<ChatSystem> selectChatById(String chatId) {
        return chatSystemRep.selectChatById(chatId);
    }

    public ApiFuture<Void> deleteChat(String chatId) {
        return chatSystemRep.deleteChat(chatId);
    }

    // ==============================
    // Operazioni sui Messaggi
    // ==============================

    public ApiFuture<Void> createMessage(String chatId, Message message) {
        return chatSystemRep.createMessage(chatId, message);
    }

    public ApiFuture<Void> updateMessage(String chatId, String messageId, Message updatedMessage) {
        return chatSystemRep.updateMessage(chatId, messageId, updatedMessage);
    }

    public CompletableFuture<List<Message>> selectMessagesByChat(String chatId) {
        return chatSystemRep.selectMessagesByChat(chatId);
    }

    public ApiFuture<Void> deleteMessage(String chatId, String messageId) {
        return chatSystemRep.deleteMessage(chatId, messageId);
    }

    public ApiFuture<Void> deleteAllMessages(String chatId) {
        return chatSystemRep.deleteAllMessages(chatId);
    }

    // ==============================
    // Gestione degli Allegati (File)
    // ==============================

    /**
     * Carica un file allegato a un messaggio di chat.
     *
     * @param chatId l'ID della chat
     * @param messageId l'ID del messaggio
     * @param file il file da caricare
     * @param fileType il tipo di file: "image", "audio", o "video"
     * @return l'URL del file caricato
     * @throws IOException se si verifica un errore durante il caricamento
     */
    public String uploadFileToMessage(String chatId, String messageId, MultipartFile file, String fileType) throws IOException {
        // Verifica che il file non sia nullo o vuoto
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Il file non può essere vuoto.");
        }

        // Carica il file su S3 e ottieni l'URL
        String fileUrl = s3Service.uploadFile("chat/" + chatId + "/" + messageId, file);

        // Trova il messaggio da aggiornare
        Message message = chatSystemRep.selectChatById(chatId)
            .thenApply(chat -> chat.getMessages().stream()
                .filter(m -> m.getConversationId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Messaggio non trovato")))
            .join();

        // Imposta l'URL del file a seconda del tipo
        switch (fileType.toLowerCase()) {
            case "image":
                message.setImage(fileUrl);
                break;
            case "audio":
                message.setAudio(fileUrl);
                break;
            case "video":
                message.setVideo(fileUrl);
                break;
            default:
                throw new IllegalArgumentException("Tipo di file non supportato.");
        }

        // Salva il messaggio aggiornato
        chatSystemRep.updateMessage(chatId, messageId, message);

        return fileUrl;
    }

    /**
     * Scarica un file allegato a un messaggio di chat.
     *
     * @param chatId l'ID della chat
     * @param messageId l'ID del messaggio
     * @param fileType il tipo di file: "image", "audio", o "video"
     * @return InputStream del file allegato
     * @throws IOException se si verifica un errore durante il download
     */
    public InputStream downloadFileFromMessage(String chatId, String messageId, String fileType) throws IOException {
        // Trova il messaggio da cui scaricare il file
        Message message = chatSystemRep.selectChatById(chatId)
            .thenApply(chat -> chat.getMessages().stream()
                .filter(m -> m.getConversationId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Messaggio non trovato")))
            .join();

        String fileUrl = null;

        // Ottieni l'URL del file in base al tipo
        switch (fileType.toLowerCase()) {
            case "image":
                fileUrl = message.getImage();
                break;
            case "audio":
                fileUrl = message.getAudio();
                break;
            case "video":
                fileUrl = message.getVideo();
                break;
            default:
                throw new IllegalArgumentException("Tipo di file non supportato.");
        }

        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new RuntimeException("File non trovato per il messaggio: " + messageId);
        }

        // Estrai la chiave del file dall'URL
        String key = fileUrl.substring(fileUrl.indexOf("amazonaws.com/") + 14);

        return s3Service.downloadFile(key);
    }

    /**
     * Elimina un file allegato a un messaggio di chat.
     *
     * @param chatId l'ID della chat
     * @param messageId l'ID del messaggio
     * @param fileType il tipo di file: "image", "audio", o "video"
     * @throws IOException se si verifica un errore durante l'eliminazione
     */
    public void deleteFileFromMessage(String chatId, String messageId, String fileType) throws IOException {
        // Trova il messaggio da cui eliminare il file
        Message message = chatSystemRep.selectChatById(chatId)
            .thenApply(chat -> chat.getMessages().stream()
                .filter(m -> m.getConversationId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Messaggio non trovato")))
            .join();

        String fileUrl = null;

        // Ottieni l'URL del file in base al tipo
        switch (fileType.toLowerCase()) {
            case "image":
                fileUrl = message.getImage();
                message.setImage(null);
                break;
            case "audio":
                fileUrl = message.getAudio();
                message.setAudio(null);
                break;
            case "video":
                fileUrl = message.getVideo();
                message.setVideo(null);
                break;
            default:
                throw new IllegalArgumentException("Tipo di file non supportato.");
        }

        if (fileUrl != null && !fileUrl.isEmpty()) {
            // Estrai la chiave del file dall'URL
            String key = fileUrl.substring(fileUrl.indexOf("amazonaws.com/") + 14);

            // Elimina il file da S3
            s3Service.deleteFile(key);

            // Salva il messaggio aggiornato senza il file
            chatSystemRep.updateMessage(chatId, messageId, message);
        } else {
            throw new RuntimeException("Nessun file associato al messaggio: " + messageId);
        }
    }
}
