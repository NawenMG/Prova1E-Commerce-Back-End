package com.prova.e_commerce.dbKey.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;

@DynamoDbBean
public class WishList {

    @NotNull(message = "L'ID utente è obbligatorio")
    @Size(min = 3, max = 100, message = "L'ID utente deve essere tra 3 e 100 caratteri")
    private String userId;

    @NotNull(message = "La lista dei prodotti non può essere nulla")
    private List<Prodotto> products;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("UserID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDbAttribute("Products")
    public List<Prodotto> getProducts() {
        return products;
    }

    public void setProducts(List<Prodotto> products) {
        this.products = products;
    }
}
