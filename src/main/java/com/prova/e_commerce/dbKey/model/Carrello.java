package com.prova.e_commerce.dbKey.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;

@DynamoDbBean
public class Carrello {

    @NotNull(message = "L'ID utente è obbligatorio")
    @Size(min = 3, max = 100, message = "L'ID utente deve avere una lunghezza compresa tra 3 e 100 caratteri")
    private String userId;  // Chiave primaria (partition key)

    @NotEmpty(message = "La lista dei prodotti non può essere vuota")
    private List<Prodotto> prodotti;  // Lista di prodotti

    @Min(value = 0, message = "La quantità totale deve essere maggiore o uguale a zero")
    private int quantitaTotale;

    @Min(value = 0, message = "Il prezzo totale deve essere maggiore o uguale a zero")
    private double prezzoTotale;

    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDbAttribute("prodotti")
    public List<Prodotto> getProdotti() {
        return prodotti;
    }

    public void setProdotti(List<Prodotto> prodotti) {
        this.prodotti = prodotti;
    }

    @DynamoDbAttribute("quantitaTotale")
    public int getQuantitaTotale() {
        return quantitaTotale;
    }

    public void setQuantitaTotale(int quantitaTotale) {
        this.quantitaTotale = quantitaTotale;
    }

    @DynamoDbAttribute("prezzoTotale")
    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }
}
