package com.prova.e_commerce.dbCol.model;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Table("ArchiviazioneResi") // Nome della tabella in Cassandra
public class ArchiviazioneResi {

    @PrimaryKeyColumn(name = "returns_id", type = PrimaryKeyType.PARTITIONED)
    @NotBlank(message = "L'ID del reso è obbligatorio")
    private String returnsID;  // Primary Key (partition key)

    @PrimaryKeyColumn(name = "users_id", type = PrimaryKeyType.CLUSTERED)
    @NotBlank(message = "L'ID dell'utente è obbligatorio")
    private String userID;  // Clustering Key

    @NotNull(message = "Lo stato del reso è obbligatorio")
    private Boolean status;  // Stato del reso

    @NotNull(message = "L'accettazione del reso è obbligatoria")
    private Boolean accettazioneReso;  // Accettazione del reso

    @NotNull(message = "La data di richiesta è obbligatoria")
    @PastOrPresent(message = "La data di richiesta deve essere nel passato o nel presente")
    private LocalDate dataRichiesta;  // Data di richiesta del reso

    // Getters e Setters
    public String getReturnsID() {
        return returnsID;
    }

    public void setReturnsID(String returnsID) {
        this.returnsID = returnsID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String usersID) {
        this.userID = usersID;
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
