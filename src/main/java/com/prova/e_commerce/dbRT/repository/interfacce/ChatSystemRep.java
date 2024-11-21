package com.prova.e_commerce.dbRT.repository.interfacce;

import com.prova.e_commerce.dbRT.model.ChatSystem;
import com.prova.e_commerce.dbRT.model.ChatSystem.Message;
import com.google.api.core.ApiFuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ChatSystemRep {

    // ==============================
    // Operazioni sulle Chat
    // ==============================

    /**
     * Crea una nuova chat.
     *
     * @param chat l'oggetto ChatSystem da creare.
     * @return ApiFuture<Void> che rappresenta l'operazione asincrona.
     */
    ApiFuture<Void> createChat(ChatSystem chat);

    /**
     * Modifica una chat esistente.
     *
     * @param chat l'oggetto ChatSystem aggiornato.
     * @return ApiFuture<Void> che rappresenta l'operazione asincrona.
     */
    ApiFuture<Void> updateChat(ChatSystem chat);

    /**
     * Recupera una chat per ID.
     *
     * @param chatId l'ID della chat da recuperare.
     * @return CompletableFuture<ChatSystem> che rappresenta l'operazione asincrona.
     */
    CompletableFuture<ChatSystem> selectChatById(String chatId);

    /**
     * Elimina una chat esistente.
     *
     * @param chatId l'ID della chat da eliminare.
     * @return ApiFuture<Void> che rappresenta l'operazione asincrona.
     */
    ApiFuture<Void> deleteChat(String chatId);

    // ==============================
    // Operazioni sui Messaggi
    // ==============================

    /**
     * Crea un nuovo messaggio in una chat.
     *
     * @param chatId  l'ID della chat in cui creare il messaggio.
     * @param message l'oggetto Message da creare.
     * @return ApiFuture<Void> che rappresenta l'operazione asincrona.
     */
    ApiFuture<Void> createMessage(String chatId, Message message);

    /**
     * Modifica un messaggio esistente in una chat.
     *
     * @param chatId       l'ID della chat.
     * @param messageId    l'ID del messaggio da modificare.
     * @param updatedMessage l'oggetto Message aggiornato.
     * @return ApiFuture<Void> che rappresenta l'operazione asincrona.
     */
    ApiFuture<Void> updateMessage(String chatId, String messageId, Message updatedMessage);

    /**
     * Recupera tutti i messaggi di una chat.
     *
     * @param chatId l'ID della chat di cui recuperare i messaggi.
     * @return CompletableFuture<List<Message>> che rappresenta l'operazione asincrona.
     */
    CompletableFuture<List<Message>> selectMessagesByChat(String chatId);

    /**
     * Elimina un singolo messaggio in una chat.
     *
     * @param chatId    l'ID della chat.
     * @param messageId l'ID del messaggio da eliminare.
     * @return ApiFuture<Void> che rappresenta l'operazione asincrona.
     */
    ApiFuture<Void> deleteMessage(String chatId, String messageId);

    /**
     * Elimina tutti i messaggi in una chat.
     *
     * @param chatId l'ID della chat di cui eliminare tutti i messaggi.
     * @return ApiFuture<Void> che rappresenta l'operazione asincrona.
     */
    ApiFuture<Void> deleteAllMessages(String chatId);
}
