package com.prova.e_commerce.dbRel.oracle.jdbc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public class Resi {

    @NotBlank(message = "L'ID del reso è obbligatorio")
    private String returnsID;  // Cambiato a String per corrispondere a VARCHAR2(50)

    @NotBlank(message = "L'ID dell'utente è obbligatorio")
    private String usersID;  // Cambiato a String per User_id che è VARCHAR2(50)

    @NotNull(message = "Lo stato del reso è obbligatorio")
    private Boolean status;  // Stato del reso (Boolean per consentire il null)

    @NotNull(message = "L'accettazione del reso è obbligatoria")
    private Boolean accettazioneReso;  // Accettazione del reso (Boolean per consentire il null)

    @NotNull(message = "La data di richiesta è obbligatoria")
    @PastOrPresent(message = "La data di richiesta deve essere nel passato o nel presente")
    private LocalDate dataRichiesta;  // Data di richiesta del reso

    // Costruttore
    public Resi() {
    }

    // Getters e Setters
    public String getReturnsID() {
        return returnsID;
    }

    public void setReturnsID(String returnsID) {
        this.returnsID = returnsID;
    }

    public String getUsersID() {
        return usersID;
    }

    public void setUsersID(String usersID) {
        this.usersID = usersID;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getAccettazioneReso() {
        return accettazioneReso;
    }

    public void setAccettazioneReso(Boolean accettazioneReso) {
        this.accettazioneReso = accettazioneReso;
    }

    public LocalDate getDataRichiesta() {
        return dataRichiesta;
    }

    public void setDataRichiesta(LocalDate dataRichiesta) {
        this.dataRichiesta = dataRichiesta;
    }
}
