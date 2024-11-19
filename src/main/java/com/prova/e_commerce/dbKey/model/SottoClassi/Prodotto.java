package com.prova.e_commerce.dbKey.model.SottoClassi;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@DynamoDbBean
public class Prodotto {

    private String productId;  // ID del prodotto
    private String nome;       // Nome del prodotto
    private int quantita;      // Quantità del prodotto
    private double prezzoUnitario; // Prezzo per unità del prodotto
    private double prezzoTotale;  // Prezzo totale del prodotto (quantita * prezzoUnitario)
    private String immagine;    // URL dell'immagine del prodotto

    @DynamoDbAttribute("productId")
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @DynamoDbAttribute("nome")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @DynamoDbAttribute("quantita")
    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    @DynamoDbAttribute("prezzoUnitario")
    public double getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(double prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    @DynamoDbAttribute("prezzoTotale")
    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    @DynamoDbAttribute("immagine")
    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }
}
