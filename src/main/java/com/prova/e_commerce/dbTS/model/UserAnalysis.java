package com.prova.e_commerce.dbTS.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import java.time.Instant;

@Measurement(name = "UserAnalysis")
public class UserAnalysis {

    // Tag: Utente
    @Column(tag = true)
    private String utente;

    // Tag: Tipo di dispositivo
    @Column(tag = true)
    private String tipoDiDispositivo;

    // Tag: Azione
    @Column(tag = true)
    private String azione;

    // Field: Durata dell'azione
    @Column
    private Double durataAzione;

    // Timestamp: gestito automaticamente
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
