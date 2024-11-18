package com.prova.e_commerce.dbRT.model;

import java.time.LocalDateTime;
import java.util.List;

public class Notifications {

    // ID della notifica (obbligatorio)
    private String id;

    // Destinatario della notifica (obbligatorio)
    private String destinatario;

    // Tipo di notifica (opzionale)
    private String type;

    // Lista dei messaggi associati alla notifica
    private List<Message> messages;

    // Timestamp (data e ora di creazione della notifica)
    private LocalDateTime timestamp;

    // Flag che indica se la notifica è stata letta o meno
    private boolean lettura;

    public Notifications() {
    }

    // Getters e Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isLettura() {
        return lettura;
    }

    public void setLettura(boolean lettura) {
        this.lettura = lettura;
    }

    // Classe interna per il messaggio
    public static class Message {
        private String titolo;
        private String corpo;

        // Costruttore
        public Message() {
        }

        // Getters e Setters
        public String getTitolo() {
            return titolo;
        }

        public void setTitolo(String titolo) {
            this.titolo = titolo;
        }

        public String getCorpo() {
            return corpo;
        }

        public void setCorpo(String corpo) {
            this.corpo = corpo;
        }
    }
}
