package com.prova.e_commerce.dbKey.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;

@DynamoDbBean
public class Cronologia {

    @NotNull(message = "L'ID utente è obbligatorio")
    @Size(min = 3, max = 100, message = "L'ID utente deve essere tra 3 e 100 caratteri")
    private String userId;

    @NotEmpty(message = "La lista dei prodotti non può essere vuota")
    private List<Prodotto> prodotti;

    // Partition key
    @DynamoDbPartitionKey
    @DynamoDbAttribute("UserID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDbAttribute("Prodotti")
    public List<Prodotto> getProdotti() {
        return prodotti;
    }

    public void setProdotti(List<Prodotto> prodotti) {
        this.prodotti = prodotti;
    }
}
