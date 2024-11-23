package com.prova.e_commerce.dbRT.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class Notifications {

    @NotBlank(message = "L'ID della notifica è obbligatorio")
    private String id; // ID della notifica (obbligatorio)

    @NotBlank(message = "Il destinatario è obbligatorio")
    private String destinatario; // Destinatario della notifica (obbligatorio)

    private String type; // Tipo di notifica (opzionale)

    @Valid
    private List<@NotNull(message = "Il messaggio non può essere null") Message> messages; // Lista dei messaggi associati alla notifica

    @NotNull(message = "Il timestamp è obbligatorio")
    private LocalDateTime timestamp; // Timestamp (data e ora di creazione della notifica)

    private boolean lettura; // Flag che indica se la notifica è stata letta o meno

    // Costruttore
    public Notifications() {}

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

        @NotBlank(message = "Il titolo del messaggio è obbligatorio")
        private String titolo; // Titolo del messaggio (obbligatorio)

        @NotBlank(message = "Il corpo del messaggio è obbligatorio")
        private String corpo; // Corpo del messaggio (obbligatorio)

        // Costruttore
        public Message() {}

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
