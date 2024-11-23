package com.prova.e_commerce.dbRT.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class ChatSystem {

    @NotBlank(message = "L'ID della chat è obbligatorio")
    private String id;  // ID della chat (Obbligatorio)

    @NotEmpty(message = "La lista dei partecipanti non può essere vuota")
    private List<@NotBlank(message = "Ogni partecipante deve avere un ID valido") String> participants; // Elenco dei partecipanti (Obbligatorio)

    @Valid
    private List<Message> messages; // Elenco dei messaggi nella chat

    // Costruttori
    public ChatSystem() {}

    // Getter e Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    // Classe interna per rappresentare i messaggi
    public static class Message {
        @NotBlank(message = "L'ID della conversazione è obbligatorio")
        private String conversationId; // ID della conversazione (Obbligatorio)

        @NotBlank(message = "Il mittente del messaggio è obbligatorio")
        private String sender; // Mittente del messaggio (Obbligatorio)

        @NotBlank(message = "Il testo del messaggio è obbligatorio")
        private String text; // Testo del messaggio (Obbligatorio)

        private String image; // URL o percorso dell'immagine (Opzionale)
        private String audio; // URL o percorso dell'audio (Opzionale)
        private String video; // URL o percorso del video (Opzionale)

        private long timestamp; // Timestamp del messaggio

        private boolean isRead; // Stato di lettura del messaggio

        // Costruttori
        public Message() {}

        // Getter e Setter
        public String getConversationId() {
            return conversationId;
        }

        public void setConversationId(String conversationId) {
            this.conversationId = conversationId;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public boolean isRead() {
            return isRead;
        }

        public void setRead(boolean read) {
            isRead = read;
        }
    }
}
