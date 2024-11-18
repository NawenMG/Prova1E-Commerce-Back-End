package com.prova.e_commerce.dbRT.service;

import com.prova.e_commerce.dbRT.model.ChatSystem;
import com.prova.e_commerce.dbRT.model.ChatSystem.Message;
import com.prova.e_commerce.dbRT.repository.interfacce.ChatSystemRep;
import com.google.api.core.ApiFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ChatSystemService {

    @Autowired
    private ChatSystemRep chatSystemRep;

    // ==============================
    // Operazioni sulle Chat
    // ==============================

    /**
     * Crea una nuova chat.
     *
     * @param chat l'oggetto ChatSystem da creare.
     * @return ApiFuture<Void> rappresenta l'operazione asincrona.
     */
    public ApiFuture<Void> createChat(ChatSystem chat) {
        // Aggiungi logica di validazione o preparazione se necessario
        return chatSystemRep.createChat(chat);
    }

    /**
     * Modifica una chat esistente.
     *
     * @param chat l'oggetto ChatSystem aggiornato.
     * @return ApiFuture<Void> rappresenta l'operazione asincrona.
     */
    public ApiFuture<Void> updateChat(ChatSystem chat) {
        // Aggiungi eventuali controlli o logica di business
        return chatSystemRep.updateChat(chat);
    }

    /**
     * Recupera una chat per ID.
     *
     * @param chatId l'ID della chat da recuperare.
     * @return CompletableFuture<ChatSystem> rappresenta l'operazione asincrona.
     */
    public CompletableFuture<ChatSystem> selectChatById(String chatId) {
        return chatSystemRep.selectChatById(chatId);
    }

    /**
     * Elimina una chat esistente.
     *
     * @param chatId l'ID della chat da eliminare.
     * @return ApiFuture<Void> rappresenta l'operazione asincrona.
     */
    public ApiFuture<Void> deleteChat(String chatId) {
        return chatSystemRep.deleteChat(chatId);
    }

    // ==============================
    // Operazioni sui Messaggi
    // ==============================

    /**
     * Crea un nuovo messaggio in una chat.
     *
     * @param chatId  l'ID della chat in cui creare il messaggio.
     * @param message l'oggetto Message da creare.
     * @return ApiFuture<Void> rappresenta l'operazione asincrona.
     */
    public ApiFuture<Void> createMessage(String chatId, Message message) {
        // Eventuale logica di trasformazione o validazione dei dati
        return chatSystemRep.createMessage(chatId, message);
    }

    /**
     * Modifica un messaggio esistente in una chat.
     *
     * @param chatId        l'ID della chat.
     * @param messageId     l'ID del messaggio da modificare.
     * @param updatedMessage l'oggetto Message aggiornato.
     * @return ApiFuture<Void> rappresenta l'operazione asincrona.
     */
    public ApiFuture<Void> updateMessage(String chatId, String messageId, Message updatedMessage) {
        return chatSystemRep.updateMessage(chatId, messageId, updatedMessage);
    }

    /**
     * Recupera tutti i messaggi di una chat.
     *
     * @param chatId l'ID della chat di cui recuperare i messaggi.
     * @return CompletableFuture<List<Message>> rappresenta l'operazione asincrona.
     */
    public CompletableFuture<List<Message>> selectMessagesByChat(String chatId) {
        return chatSystemRep.selectMessagesByChat(chatId);
    }

    /**
     * Elimina un singolo messaggio in una chat.
     *
     * @param chatId    l'ID della chat.
     * @param messageId l'ID del messaggio da eliminare.
     * @return ApiFuture<Void> rappresenta l'operazione asincrona.
     */
    public ApiFuture<Void> deleteMessage(String chatId, String messageId) {
        return chatSystemRep.deleteMessage(chatId, messageId);
    }

    /**
     * Elimina tutti i messaggi in una chat.
     *
     * @param chatId l'ID della chat di cui eliminare tutti i messaggi.
     * @return ApiFuture<Void> rappresenta l'operazione asincrona.
     */
    public ApiFuture<Void> deleteAllMessages(String chatId) {
        return chatSystemRep.deleteAllMessages(chatId);
    }
}
