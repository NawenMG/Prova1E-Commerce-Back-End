package com.prova.e_commerce.dbCol.model;

import java.time.Instant;

public class ArchiviazioneSegnalazioni {

    private String id;                // Primary Key
    private String utente;            // Utente che ha fatto la segnalazione
    private String riferimento;       // Riferimento della segnalazione
    private Instant data;             // Data della segnalazione
    private String titolo;            // Titolo della segnalazione
    private String descrizione;      // Descrizione della segnalazione
    private String fileMultimediali; // File multimediali associati alla segnalazione

    // Costruttore vuoto
    public ArchiviazioneSegnalazioni() {
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public String getRiferimento() {
        return riferimento;
    }

    public void setRiferimento(String riferimento) {
        this.riferimento = riferimento;
    }

    public Instant getData() {
        return data;
    }

    public void setData(Instant data) {
        this.data = data;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getFileMultimediali() {
        return fileMultimediali;
    }

    public void setFileMultimediali(String fileMultimediali) {
        this.fileMultimediali = fileMultimediali;
    }
}
