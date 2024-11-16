package com.prova.e_commerce.dbCol.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public class ArchiviazioneOrdini {

    private String id; // Primary Key
    private String userId;
    private Instant orderDate;
    private String status;
    private BigDecimal importoTotale;
    private String indirizzoDellaSpedizione;
    private Set<String> listaArticoliDellOrdine;
    private Instant consegna;
    private String corriere;

    public ArchiviazioneOrdini() {
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getImportoTotale() {
        return importoTotale;
    }

    public void setImportoTotale(BigDecimal importoTotale) {
        this.importoTotale = importoTotale;
    }

    public String getIndirizzoDellaSpedizione() {
        return indirizzoDellaSpedizione;
    }

    public void setIndirizzoDellaSpedizione(String indirizzoDellaSpedizione) {
        this.indirizzoDellaSpedizione = indirizzoDellaSpedizione;
    }

    public Set<String> getListaArticoliDellOrdine() {
        return listaArticoliDellOrdine;
    }

    public void setListaArticoliDellOrdine(Set<String> listaArticoliDellOrdine) {
        this.listaArticoliDellOrdine = listaArticoliDellOrdine;
    }

    public Instant getConsegna() {
        return consegna;
    }

    public void setConsegna(Instant consegna) {
        this.consegna = consegna;
    }

    public String getCorriere() {
        return corriere;
    }

    public void setCorriere(String corriere) {
        this.corriere = corriere;
    }
}
