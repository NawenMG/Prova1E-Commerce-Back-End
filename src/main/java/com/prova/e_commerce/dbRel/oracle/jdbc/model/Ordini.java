package com.prova.e_commerce.dbRel.oracle.jdbc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Ordini {

    @NotBlank(message = "L'ID dell'ordine è obbligatorio")
    @Size(max = 50, message = "L'ID dell'ordine non può superare i 50 caratteri")
    private String orderID;  // Cambiato a String per corrispondere a VARCHAR2(50)

    @NotBlank(message = "L'ID dell'utente è obbligatorio")
    @Size(max = 50, message = "L'ID dell'utente non può superare i 50 caratteri")
    private String usersID;  // Cambiato a String per User_id che è VARCHAR2(50)

    @NotNull(message = "Lo stato di spedizione è obbligatorio")
    @PastOrPresent(message = "La data dello stato di spedizione deve essere nel passato o nel presente")
    private LocalDateTime statoDiSpedizione; // Cambiato a LocalDateTime per corrispondere a TIMESTAMP

    @NotNull(message = "La data di consegna è obbligatoria")
    @PastOrPresent(message = "La data di consegna deve essere nel passato o nel presente")
    private LocalDate dataDiConsegna;   // Data di consegna (corretto)

    @NotNull(message = "La data di richiesta è obbligatoria")
    @PastOrPresent(message = "La data di richiesta deve essere nel passato o nel presente")
    private LocalDate dataDiRichiesta;   // Data di richiesta (corretto)

    @NotNull(message = "L'accettazione dell'ordine è obbligatoria")
    private boolean accettazioneOrdine; // Accettazione ordine (corretto)

    @NotBlank(message = "Lo stato dell'ordine è obbligatorio")
    @Size(max = 50, message = "Lo stato dell'ordine non può superare i 50 caratteri")
    private String status;              // Stato dell'ordine, cambiato a String per riferirsi alla colonna 'Status' in Pagamenti

    @NotBlank(message = "Il nome del corriere è obbligatorio")
    @Size(max = 50, message = "Il nome del corriere non può superare i 50 caratteri")
    private String corriere;             // Nome del corriere (corretto)

    @NotBlank(message = "La posizione dell'ordine è obbligatoria")
    @Size(max = 100, message = "La posizione dell'ordine non può superare i 100 caratteri")
    private String posizione;            // Posizione dell'ordine (corretto)

    // Costruttore
    public Ordini() {
    }

    // Getters e Setters
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUsersID() {
        return usersID;
    }

    public void setUsersID(String usersID) {
        this.usersID = usersID;
    }

    public LocalDateTime getStatoDiSpedizione() {
        return statoDiSpedizione;
    }

    public void setStatoDiSpedizione(LocalDateTime statoDiSpedizione) {
        this.statoDiSpedizione = statoDiSpedizione;
    }

    public LocalDate getDataDiConsegna() {
        return dataDiConsegna;
    }

    public void setDataDiConsegna(LocalDate dataDiConsegna) {
        this.dataDiConsegna = dataDiConsegna;
    }

    public LocalDate getDataDiRichiesta() {
        return dataDiRichiesta;
    }

    public void setDataDiRichiesta(LocalDate dataDiRichiesta) {
        this.dataDiRichiesta = dataDiRichiesta;
    }

    public boolean isAccettazioneOrdine() {
        return accettazioneOrdine;
    }

    public void setAccettazioneOrdine(boolean accettazioneOrdine) {
        this.accettazioneOrdine = accettazioneOrdine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCorriere() {
        return corriere;
    }

    public void setCorriere(String corriere) {
        this.corriere = corriere;
    }

    public String getPosizione() {
        return posizione;
    }

    public void setPosizione(String posizione) {
        this.posizione = posizione;
    }
}
