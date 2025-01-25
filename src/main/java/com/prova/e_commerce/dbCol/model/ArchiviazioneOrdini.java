package com.prova.e_commerce.dbCol.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Table("ArchiviazioneOrdini") // Nome della tabella in Cassandra
public class ArchiviazioneOrdini {

    @PrimaryKeyColumn(name = "orders_id", type = PrimaryKeyType.PARTITIONED)
    @NotNull(message = "ID dell'ordine è obbligatorio")
    private String id; // Primary Key

    @NotNull(message = "UserId non può essere nullo")
    @Size(min = 1, max = 50, message = "UserId deve essere compreso tra 1 e 50 caratteri")
    @PrimaryKeyColumn(name = "users_id", type = PrimaryKeyType.CLUSTERED)
    private String userId;

    @NotNull(message = "La data dell'ordine è obbligatoria")
    private Instant orderDate;

    @NotNull(message = "Lo stato dell'ordine è obbligatorio")
    @Size(min = 1, max = 20, message = "Lo stato deve essere tra 1 e 20 caratteri")
    private String status;

    @NotNull(message = "L'importo totale è obbligatorio")
    @DecimalMin(value = "0.01", message = "L'importo totale deve essere maggiore di 0")
    private BigDecimal importoTotale;

    @NotNull(message = "L'indirizzo di spedizione è obbligatorio")
    @Size(min = 1, max = 255, message = "L'indirizzo di spedizione deve essere tra 1 e 255 caratteri")
    private String indirizzoDellaSpedizione;

    @NotNull(message = "La lista degli articoli è obbligatoria")
    private Set<String> listaArticoliDellOrdine;

    @NotNull(message = "La data di consegna è obbligatoria")
    private Instant consegna;

    @NotNull(message = "Il corriere è obbligatorio")
    @Size(min = 1, max = 50, message = "Il nome del corriere deve essere tra 1 e 50 caratteri")
    private String corriere;

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
