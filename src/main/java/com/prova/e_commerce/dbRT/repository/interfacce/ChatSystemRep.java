package com.prova.e_commerce.dbRT.repository.interfacce;

import com.prova.e_commerce.dbRT.model.ChatSystem;
import com.prova.e_commerce.dbRT.model.ChatSystem.Message;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ChatSystemRep {

    CompletableFuture<ChatSystem> createChat(ChatSystem chat);

    CompletableFuture<Void> updateChat(ChatSystem updatedChat);

    CompletableFuture<ChatSystem> selectChatById(String chatId);

    CompletableFuture<Void> deleteChat(String chatId);

    CompletableFuture<Message> createMessage(String chatId, Message message);

    CompletableFuture<Void> updateMessage(String chatId, String messageId, String updatedText);

    CompletableFuture<List<Message>> selectMessagesByChat(String chatId);

    CompletableFuture<Void> deleteMessage(String chatId, String messageId);

    CompletableFuture<Void> deleteAllMessages(String chatId);
}
