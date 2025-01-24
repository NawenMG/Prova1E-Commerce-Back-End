package com.prova.e_commerce.dbRT.service;

import com.prova.e_commerce.dbRT.model.ChatSystem;
import com.prova.e_commerce.dbRT.model.ChatSystem.Message;
import com.prova.e_commerce.dbRT.repository.interfacce.ChatSystemRep;
import com.prova.e_commerce.storage.OzoneService;
import com.prova.e_commerce.twilioSMS.ChatService; // <--- Import del tuo servizio Twilio Chat
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ChatSystemService {

    private static final Logger logger = LoggerFactory.getLogger(ChatSystemService.class);

    @Autowired
    private ChatSystemRep chatSystemRep;

    @Autowired
    private OzoneService ozoneService;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    // == Aggiunta del tuo servizio Twilio Chat ==
    @Autowired
    private ChatService twilioChatService;

    public ChatSystemService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.chat.create.count");
        meterRegistry.counter("service.chat.update.count");
        meterRegistry.counter("service.chat.delete.count");
        meterRegistry.counter("service.message.create.count");
        meterRegistry.counter("service.message.update.count");
        meterRegistry.counter("service.message.delete.count");
        meterRegistry.counter("service.message.delete.all.count");
        meterRegistry.counter("service.chat.select.count");
        meterRegistry.counter("service.message.select.count");
    }

    // ===========================================================
    //                   METODI SULLA CHAT
    // ===========================================================

    /**
     * Crea una chat sia su Firebase (ChatSystem) che su Twilio Chat.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#chat.id")
    public CompletableFuture<ChatSystem> createChat(ChatSystem chat) {
        logger.info("Creazione chat: chatId={}", chat.getId());
        Span span = tracer.spanBuilder("createChat").startSpan();
        meterRegistry.counter("service.chat.create.count").increment();
        try {
            // 1) Crea la chat localmente (Firebase)
            CompletableFuture<ChatSystem> futureChat = chatSystemRep.createChat(chat);

            // 2) Al completamento, crea il canale su Twilio e aggiungi i partecipanti
            return futureChat.thenApply(createdChat -> {
                String channelName = "Chat_" + createdChat.getId(); // un friendly name a piacere
                String channelSid = twilioChatService.createChannel(channelName);
                logger.info("Canale Twilio creato con SID={}", channelSid);

                // Aggiungi ogni partecipante come utente nel canale Twilio
                for (String participant : createdChat.getParticipants()) {
                    String result = twilioChatService.addUserToChannel(channelSid, participant);
                    logger.info("Twilio: {}", result);
                }

                // In un'app reale, potresti salvare channelSid nella chat su Firebase
                // (ad es. in createdChat.setTwilioChannelSid(channelSid))
                // e poi fare un updateChat(...) se vuoi mantenerne traccia.
                // Oppure, se preferisci, potresti ignorare.

                return createdChat;
            });
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#updatedChat.id")
    public CompletableFuture<Void> updateChat(ChatSystem updatedChat) {
        logger.info("Aggiornamento chat: chatId={}", updatedChat.getId());
        Span span = tracer.spanBuilder("updateChat").startSpan();
        meterRegistry.counter("service.chat.update.count").increment();
        try {
            // Qui aggiorni solo su Firebase. Se vuoi aggiornare anche su Twilio (es. friendlyName),
            // potresti memorizzare channelSid in updatedChat e usare un metodo Twilio per rename.
            return chatSystemRep.updateChat(updatedChat);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#chatId")
    public CompletableFuture<ChatSystem> selectChatById(String chatId) {
        logger.info("Selezione chat per ID: chatId={}", chatId);
        Span span = tracer.spanBuilder("selectChatById").startSpan();
        meterRegistry.counter("service.chat.select.count").increment();
        try {
            return chatSystemRep.selectChatById(chatId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#chatId")
    public CompletableFuture<Void> deleteChat(String chatId) {
        logger.info("Eliminazione chat: chatId={}", chatId);
        Span span = tracer.spanBuilder("deleteChat").startSpan();
        meterRegistry.counter("service.chat.delete.count").increment();
        try {
            // Se volessi eliminare anche il canale su Twilio,
            // Twilio ChatService dovrebbe avere un "deleteChannel(...)"
            // e dovresti conoscere il channelSid associato.
            return chatSystemRep.deleteChat(chatId);
        } finally {
            span.end();
        }
    }

    // ===========================================================
    //                 METODI SUI MESSAGGI
    // ===========================================================

    @CacheEvict(value = {"caffeine", "redis"}, key = "#chatId")
    public CompletableFuture<Message> createMessage(String chatId, Message message) {
        logger.info("Creazione messaggio: chatId={}, messageId={}", chatId, message.getConversationId());
        Span span = tracer.spanBuilder("createMessage").startSpan();
        meterRegistry.counter("service.message.create.count").increment();
        try {
            // 1) Crea messaggio localmente su Firebase
            CompletableFuture<Message> futureMsg = chatSystemRep.createMessage(chatId, message);

            // 2) Al completamento, invia il messaggio anche su Twilio Chat
            return futureMsg.thenApply(createdMessage -> {
                // In un’app “matura”, dovresti avere un channelSid associato a chatId
                // Esempio: "Chat_<chatId>" se lo hai salvato.
                String channelSid = "ricava_il_channelSid_associato_al_chatId"; 
                // Oppure lo ottieni se l'hai memorizzato in ChatSystem.
                // Se non memorizzi, non potrai inviare su Twilio a canale esatto.

                // Esempio semplificato: supponiamo di generare un channelSid costante o di non farlo
                // In un caso reale, devi collegare chatId <-> channelSid in un db, altrimenti non sai dove inviare su Twilio.

                String author = createdMessage.getSender();  // Lo username di chi invia
                String body = createdMessage.getText();      // Il testo del messaggio

                // Esegui l'invio su Twilio
                // ATTENZIONE: se non hai un channelSid reale, non funzionerà.
                // Se vuoi vedere l'effetto, devi davvero mappare "chatId -> channelSid"
                // commentiamo questa parte come esempio:
                // twilioChatService.sendMessageToChannel(channelSid, author, body);

                logger.info("Messaggio inviato su Twilio Chat fittiziamente. (Manca mappatura channelSid.)");
                return createdMessage;
            });
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#chatId")
    public CompletableFuture<Void> updateMessage(String chatId, String messageId, String updatedText) {
        logger.info("Aggiornamento messaggio: chatId={}, messageId={}", chatId, messageId);
        Span span = tracer.spanBuilder("updateMessage").startSpan();
        meterRegistry.counter("service.message.update.count").increment();
        try {
            // Se vuoi riflettere la modifica su Twilio Chat,
            // Twilio Chat non consente (di default) l'update del corpo di un messaggio già inviato
            // potresti gestire questa logica altrove.
            return chatSystemRep.updateMessage(chatId, messageId, updatedText);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#chatId")
    public CompletableFuture<List<Message>> selectMessagesByChat(String chatId) {
        logger.info("Selezione messaggi per chat ID: chatId={}", chatId);
        Span span = tracer.spanBuilder("selectMessagesByChat").startSpan();
        meterRegistry.counter("service.message.select.count").increment();
        try {
            return chatSystemRep.selectMessagesByChat(chatId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#chatId")
    public CompletableFuture<Void> deleteMessage(String chatId, String messageId) {
        logger.info("Eliminazione messaggio: chatId={}, messageId={}", chatId, messageId);
        Span span = tracer.spanBuilder("deleteMessage").startSpan();
        meterRegistry.counter("service.message.delete.count").increment();
        try {
            // Se vuoi eliminare anche il messaggio su Twilio Chat, devi avere un metodo in ChatService
            return chatSystemRep.deleteMessage(chatId, messageId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#chatId")
    public CompletableFuture<Void> deleteAllMessages(String chatId) {
        logger.info("Eliminazione di tutti i messaggi: chatId={}", chatId);
        Span span = tracer.spanBuilder("deleteAllMessages").startSpan();
        meterRegistry.counter("service.message.delete.all.count").increment();
        try {
            // Se vuoi eliminare anche i messaggi su Twilio Chat, dovrai scorrerli e cancellarli
            // o eliminare l'intero canale se preferisci.
            return chatSystemRep.deleteAllMessages(chatId);
        } finally {
            span.end();
        }
    }
}
