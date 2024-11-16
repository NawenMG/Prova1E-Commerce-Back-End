package com.prova.e_commerce.dbCol.model;

import java.math.BigDecimal;
import java.time.Instant;

public class ArchiviazioneTransizioni {

    private String id; // ID (Primary Key)
    private String orderId; // Order_ID
    private Instant transizioneDate; // Transizione_date
    private BigDecimal importoTotale; // Importo_totale
    private String metodoDiPagamento; // Metodo_di_pagamento
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
