package com.prova.e_commerce.dbKey.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import java.util.List;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;

@DynamoDbBean
public class Cronologia {

    private String userId;
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
