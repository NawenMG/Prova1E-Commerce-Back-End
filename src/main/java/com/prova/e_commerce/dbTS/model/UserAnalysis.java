package com.prova.e_commerce.dbTS.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Instant;

@Measurement(name = "UserAnalysis")
public class UserAnalysis {

    @NotNull(message = "L'ID utente non può essere null")
    @Column(tag = true)
    private String userId;


    // Tag: Utente
    @NotBlank(message = "Il campo 'utente' non può essere vuoto o null")
    @Column(tag = true)
    private String utente;

    // Tag: Tipo di dispositivo
    @NotBlank(message = "Il campo 'tipoDiDispositivo' non può essere vuoto o null")
    @Column(tag = true)
    private String tipoDiDispositivo;

    // Tag: Azione
    @NotBlank(message = "Il campo 'azione' non può essere vuoto o null")
    @Column(tag = true)
    private String azione;

    // Field: Durata dell'azione
    @NotNull(message = "La durata dell'azione non può essere null")
    @Positive(message = "La durata dell'azione deve essere maggiore di 0")
    @Column
    private Double durataAzione;

    // Timestamp: gestito automaticamente
    @NotNull(message = "Il timestamp non può essere null")
    @Column(timestamp = true)
    private Instant time;

    // Costruttore
    public UserAnalysis(String utente, String tipoDiDispositivo, String azione, Double durataAzione, Instant time) {
        this.utente = utente;
        this.tipoDiDispositivo = tipoDiDispositivo;
        this.azione = azione;
        this.durataAzione = durataAzione;
        this.time = time;
    }

    // Getters e Setters

    // Getters e Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public String getTipoDiDispositivo() {
        return tipoDiDispositivo;
    }

    public void setTipoDiDispositivo(String tipoDiDispositivo) {
        this.tipoDiDispositivo = tipoDiDispositivo;
    }

    public String getAzione() {
        return azione;
    }

    public void setAzione(String azione) {
        this.azione = azione;
    }

    public Double getDurataAzione() {
        return durataAzione;
    }

    public void setDurataAzione(Double durataAzione) {
        this.durataAzione = durataAzione;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
