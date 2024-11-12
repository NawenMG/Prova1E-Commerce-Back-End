package com.prova.e_commerce.dbDoc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/* import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin; */
import java.math.BigDecimal;
import java.util.Map;

@Document(collection = "scheda_prodotti")
public class SchedeProdotti {

    @Id
    private String id;

    @Field("nome")
    /* @NotNull(message = "Il nome del prodotto è obbligatorio") */
    private String nome;

    @Field("prezzo")
    /* @NotNull(message = "Il prezzo del prodotto è obbligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "Il prezzo deve essere maggiore di zero") */
    private BigDecimal prezzo;

    @Field("parametriDescrittivi")
    private Map<String, String> parametriDescrittivi;

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
