package com.prova.e_commerce.dbKey.model.SottoClassi;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class Prodotto {

    @NotNull(message = "L'ID del prodotto è obbligatorio")
    private String _key;  // ID del prodotto, diventa la chiave in ArangoDB

    @NotNull(message = "Il nome del prodotto è obbligatorio")
    private String nome;       // Nome del prodotto

    @Positive(message = "La quantità del prodotto deve essere maggiore di zero")
    private int quantita;      // Quantità del prodotto

    @Positive(message = "Il prezzo unitario deve essere maggiore di zero")
    private double prezzoUnitario; // Prezzo per unità del prodotto

    @Positive(message = "Il prezzo totale deve essere maggiore di zero")
    private double prezzoTotale;  // Prezzo totale del prodotto (quantita * prezzoUnitario)

    // Modifica del tipo immagine: da String (URL) a byte[] (BLOB)
    private byte[] immagine;    // Immagine del prodotto in formato binario (BLOB)

    // Getter e setter per la chiave _key (equivalente a productId in ArangoDB)
    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        this._key = key;
    }

    // Getter e setter per gli altri attributi
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public double getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(double prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public byte[] getImmagine() {
        return immagine;
    }

    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }
}