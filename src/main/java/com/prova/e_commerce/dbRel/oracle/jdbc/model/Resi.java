package com.prova.e_commerce.dbRel.oracle.jdbc.model;

import java.time.LocalDate;

/* import jakarta.validation.constraints.NotBlank;
 */
public class Resi {
    
    /* @NotBlank(message = "Obbligatorio") */
    private String returnsID;         // Cambiato a String per corrispondere a VARCHAR2(50)

    /* @NotBlank(message = "Obbligatorio") */
    private String usersID;           // Cambiato a String per User_id che è VARCHAR2(50)

    /* @NotBlank(message = "Obbligatorio") */
    private boolean status;         // Stato del reso (booleano per NUMBER(1))

    /* @NotBlank(message = "Obbligatorio") */
    private boolean accettazioneReso; // Accettazione del reso (booleano per NUMBER(1))

    /* @NotBlank(message = "Obbligatorio") */
    private LocalDate dataRichiesta; // Data di richiesta del reso (corretto come LocalDate)

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isAccettazioneReso() {
        return accettazioneReso;
    }

    public void setAccettazioneReso(boolean accettazioneReso) {
        this.accettazioneReso = accettazioneReso;
    }

    public LocalDate getDataRichiesta() {
        return dataRichiesta;
    }

    public void setDataRichiesta(LocalDate dataRichiesta) {
        this.dataRichiesta = dataRichiesta;
    }
}
