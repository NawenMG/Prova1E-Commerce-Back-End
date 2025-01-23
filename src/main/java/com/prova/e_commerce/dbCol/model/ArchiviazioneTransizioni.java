package com.prova.e_commerce.dbCol.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;

public class ArchiviazioneTransizioni {

    @NotNull(message = "L'ID della transizione è obbligatorio")
    private String id; // ID (Primary Key)

    @NotNull(message = "L'Order_ID è obbligatorio")
    @Size(min = 1, max = 255, message = "L'Order_ID deve essere tra 1 e 255 caratteri")
    private String orderId; // Order_ID

    @NotNull(message = "UserId non può essere nullo")
    @Size(min = 1, max = 50, message = "UserId deve essere compreso tra 1 e 50 caratteri")
    private String userId;

    @NotNull(message = "La data della transizione è obbligatoria")
    private Instant transizioneDate; // Transizione_date

    @NotNull(message = "L'importo totale è obbligatorio")
    @Positive(message = "L'importo totale deve essere positivo")
    private BigDecimal importoTotale; // Importo_totale

    @NotNull(message = "Il metodo di pagamento è obbligatorio")
    @Size(min = 1, max = 50, message = "Il metodo di pagamento deve essere tra 1 e 50 caratteri")
    private String metodoDiPagamento; // Metodo_di_pagamento

    @NotNull(message = "Lo stato è obbligatorio")
    @Size(min = 1, max = 50, message = "Lo stato deve essere tra 1 e 50 caratteri")
    private String status; // Status

    // Costruttore vuoto
    public ArchiviazioneTransizioni() {}

    // Getter e Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getTransizioneDate() {
        return transizioneDate;
    }

    public void setTransizioneDate(Instant transizioneDate) {
        this.transizioneDate = transizioneDate;
    }

    public BigDecimal getImportoTotale() {
        return importoTotale;
    }

    public void setImportoTotale(BigDecimal importoTotale) {
        this.importoTotale = importoTotale;
    }

    public String getMetodoDiPagamento() {
        return metodoDiPagamento;
    }

    public void setMetodoDiPagamento(String metodoDiPagamento) {
        this.metodoDiPagamento = metodoDiPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
