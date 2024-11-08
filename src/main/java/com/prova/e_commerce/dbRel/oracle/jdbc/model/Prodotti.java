package com.prova.e_commerce.dbRel.oracle.jdbc.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/* import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size; */

public class Prodotti {

    /* @NotBlank(message = "Obbligatorio")
    @Size(max = 50, message = "L'ID del prodotto non può superare i 50 caratteri") */
    private String productId;  // chiave primaria (modificato in String per corrispondere a VARCHAR2(50))

    /* @NotBlank(message = "Obbligatorio")
    @Size(max = 100, message = "Il nome del prodotto non può superare i 100 caratteri") */
    private String nome;             // nome del prodotto

    /* @NotNull(message = "Obbligatorio") */
    private BigDecimal prezzo;      // prezzo del prodotto (BigDecimal per precisione decimale)

    private String descrizione; // descrizione del prodotto (CLOB, trattato come Stringa)

    private byte[] immagine;  // immagine del prodotto (BLOB trattato come byte[])

    /* @NotNull(message = "Obbligatorio") */
    private int amountAvailable;  // quantità disponibile

    /* @NotNull(message = "Obbligatorio")
    @Size(max = 50, message = "Il nome della categoria non può superare i 50 caratteri") */
    private String categoria; // categoria del prodotto

    /* @NotNull(message = "Obbligatorio") */
    private LocalDate dataDiInserimento; // data di inserimento del prodotto

    // Costruttore
    public Prodotti() {
    }

    // Getter e Setter
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public byte[] getImmagine() {
        return immagine;
    }

    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    public int getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(int amountAvailable) {
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
