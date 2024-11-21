package com.prova.e_commerce.dbRT.model;

import java.util.List;

public class ChatSystem {

    private String id; // ID della chat (Obbligatorio)
    private List<String> participants; // Elenco dei partecipanti (Obbligatorio)
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
        private String conversationId; // ID della conversazione (Obbligatorio)
        private String sender; // Mittente del messaggio (Obbligatorio)
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
