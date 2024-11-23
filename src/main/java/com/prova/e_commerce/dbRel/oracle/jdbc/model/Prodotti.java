package com.prova.e_commerce.dbRel.oracle.jdbc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Prodotti {

    @NotBlank(message = "L'ID del prodotto è obbligatorio")
    @Size(max = 50, message = "L'ID del prodotto non può superare i 50 caratteri")
    private String productID;  // chiave primaria

    @NotBlank(message = "Il nome del prodotto è obbligatorio")
    @Size(max = 100, message = "Il nome del prodotto non può superare i 100 caratteri")
    private String nome;  // nome del prodotto

    @NotNull(message = "Il prezzo del prodotto è obbligatorio")
    @PositiveOrZero(message = "Il prezzo del prodotto deve essere positivo o zero")
    private BigDecimal prezzo;  // prezzo del prodotto

    @Size(max = 2000, message = "La descrizione non può superare i 2000 caratteri")
    private String descrizione;  // descrizione del prodotto (opzionale)

    @Size(max = 255, message = "Il nome del file immagine non può superare i 255 caratteri")
    private String immagine;  // immagine del prodotto (opzionale)

    @NotNull(message = "La quantità disponibile è obbligatoria")
    @PositiveOrZero(message = "La quantità disponibile deve essere positiva o zero")
    private Integer amountAvailable;  // quantità disponibile

    @NotBlank(message = "La categoria del prodotto è obbligatoria")
    @Size(max = 50, message = "Il nome della categoria non può superare i 50 caratteri")
    private String categoria;  // categoria del prodotto

    @NotNull(message = "La data di inserimento è obbligatoria")
    @PastOrPresent(message = "La data di inserimento deve essere nel passato o nel presente")
    private LocalDate dataDiInserimento;  // data di inserimento

    // Costruttore
    public Prodotti() {
    }

    // Getter e Setter
    public String getProductId() {
        return productID;
    }

    public void setProductId(String productID) {
        this.productID = productID;
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public Integer getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(Integer amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getDataDiInserimento() {
        return dataDiInserimento;
    }

    public void setDataDiInserimento(LocalDate dataDiInserimento) {
        this.dataDiInserimento = dataDiInserimento;
    }
}
