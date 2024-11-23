package com.prova.e_commerce.dbDoc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.Map;

@Document(collection = "scheda_prodotti")
public class SchedeProdotti {

    @Id
    private String id;

    @Field("nome")
    @NotNull(message = "Il nome del prodotto è obbligatorio") // Validazione del nome
    private String nome;

    @Field("prezzo")
    @NotNull(message = "Il prezzo del prodotto è obbligatorio") // Validazione del prezzo (non nullo)
    @DecimalMin(value = "0.01", inclusive = true, message = "Il prezzo deve essere maggiore o uguale a 0.01") // Validazione per prezzo maggiore di zero
    private BigDecimal prezzo;

    @Field("parametriDescrittivi")
    private Map<String, String> parametriDescrittivi; // Nessuna validazione specifica

    // Costruttori
    public SchedeProdotti() {}

    public SchedeProdotti(String nome, BigDecimal prezzo, Map<String, String> parametriDescrittivi) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.parametriDescrittivi = parametriDescrittivi;
    }

    // Getter e Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public Map<String, String> getParametriDescrittivi() {
        return parametriDescrittivi;
    }

    public void setParametriDescrittivi(Map<String, String> parametriDescrittivi) {
        this.parametriDescrittivi = parametriDescrittivi;
    }
}
