package com.prova.e_commerce.twilioSMS;

import com.twilio.rest.chat.v2.service.Channel;
import com.twilio.rest.chat.v2.service.User;
import com.twilio.rest.chat.v2.service.channel.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Value("${twilio.chat.service.sid}")
    private String chatServiceSid;

    /**
     * Creazione di un nuovo canale (chat room).
     *
     * @param channelName il nome del canale da creare.
     * @return l'ID (SID) del canale creato.
     */
    public String createChannel(String channelName) {
        Channel channel = Channel.creator(chatServiceSid)
                .setFriendlyName(channelName)
                .create();
        return channel.getSid();
    }

    /**
     * Aggiungere un utente a un canale esistente.
     *
     * @param channelSid l'ID (SID) del canale.
     * @param identity   l'identità (username) dell'utente da aggiungere.
     * @return un messaggio di conferma.
     */
    public String addUserToChannel(String channelSid, String identity) {
        // Creazione dell'utente se non esiste già (Twilio Chat user)
        User.creator(chatServiceSid, identity).create();

        // Aggiunta dell'utente al canale
        com.twilio.rest.chat.v2.service.channel.Member.creator(chatServiceSid, channelSid, identity).create();
        return "User " + identity + " added to channel " + channelSid;
    }

    /**
     * Invio di un messaggio a un canale.
     *
     * @param channelSid l'ID (SID) del canale.
     * @param author     l'autore del messaggio (username).
     * @param message    il contenuto del messaggio.
     * @return l'ID (SID) del messaggio inviato.
     */
    public String sendMessageToChannel(String channelSid, String author, String message) {
        Message msg = Message.creator(chatServiceSid, channelSid)
                .setFrom(author)
                .setBody(message)
                .create();
        return msg.getSid();
    }

    /**
     * Recupera tutti i messaggi da un canale.
     *
     * @param channelSid l'ID (SID) del canale.
     * @return una lista di messaggi (Iterable).
     */
    public Iterable<Message> getMessagesFromChannel(String channelSid) {
        return Message.reader(chatServiceSid, channelSid).read();
    }
}
