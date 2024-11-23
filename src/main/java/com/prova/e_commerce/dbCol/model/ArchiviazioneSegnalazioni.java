package com.prova.e_commerce.dbCol.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

public class ArchiviazioneSegnalazioni {

    @NotNull(message = "ID della segnalazione è obbligatorio")
    private String id;  // Primary Key

    @NotNull(message = "L'utente è obbligatorio")
    @Size(min = 1, max = 100, message = "Il nome dell'utente deve essere tra 1 e 100 caratteri")
    private String utente;  // Utente che ha fatto la segnalazione

    @NotNull(message = "Il riferimento della segnalazione è obbligatorio")
    @Size(min = 1, max = 100, message = "Il riferimento deve essere tra 1 e 100 caratteri")
    private String riferimento;  // Riferimento della segnalazione

    @NotNull(message = "La data della segnalazione è obbligatoria")
    private Instant data;  // Data della segnalazione

    @NotNull(message = "Il titolo della segnalazione è obbligatorio")
    @Size(min = 1, max = 255, message = "Il titolo deve essere tra 1 e 255 caratteri")
    private String titolo;  // Titolo della segnalazione

    @NotBlank(message = "La descrizione non può essere vuota")
    @Size(min = 1, max = 1000, message = "La descrizione deve essere tra 1 e 1000 caratteri")
    private String descrizione;  // Descrizione della segnalazione

    @Size(max = 255, message = "Il file multimediale può essere al massimo di 255 caratteri")
    private String fileMultimediali;  // File multimediali associati alla segnalazione

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
