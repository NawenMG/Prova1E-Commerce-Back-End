package com.prova.e_commerce.dbKey.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import java.util.List;

import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;

@DynamoDbBean
public class WishList {

    private String userId;
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
