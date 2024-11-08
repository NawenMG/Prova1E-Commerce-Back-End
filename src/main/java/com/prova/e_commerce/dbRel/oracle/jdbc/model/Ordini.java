package com.prova.e_commerce.dbRel.oracle.jdbc.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/* import jakarta.validation.constraints.NotBlank;
 */
public class Ordini {

    /* @NotBlank(message = "Obbligatorio") */
    private String orderID;  // Cambiato a String per corrispondere a VARCHAR2(50)

    /* @NotBlank(message = "Obbligatorio") */
    private String usersID;  // Cambiato a String per User_id che è VARCHAR2(50)

    /* @NotBlank(message = "Obbligatorio") */
    private LocalDateTime statoDiSpedizione; // Cambiato a LocalDateTime per corrispondere a TIMESTAMP

    /* @NotBlank(message = "Obbligatorio") */
    private LocalDate dataDiConsegna;   // Data di consegna (corretto)

    /* @NotBlank(message = "Obbligatorio") */
    private LocalDate dataDiRichiesta;   // Data di richiesta (corretto)

    /* @NotBlank(message = "Obbligatorio") */
    private boolean accettazioneOrdine; // Accettazione ordine (corretto)

    /* @NotBlank(message = "Obbligatorio") */
    private String status;              // Stato dell'ordine, cambiato a String per riferirsi alla colonna 'Status' in Pagamenti

    /* @NotBlank(message = "Obbligatorio") */
    private String corriere;             // Nome del corriere (corretto)

    /* @NotBlank(message = "Obbligatorio") */
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
