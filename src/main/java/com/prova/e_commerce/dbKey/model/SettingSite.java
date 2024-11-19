package com.prova.e_commerce.dbKey.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

import com.prova.e_commerce.dbKey.model.SottoClassi.Notifica;

@DynamoDbBean
public class SettingSite {

    private String userId;
    private Integer prodottiPerPagina;
    private String tema;
    private String layout;
    private String lingua;
    private List<Notifica> notifiche;

    // Partition key
    @DynamoDbPartitionKey
    @DynamoDbAttribute("UserID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDbAttribute("ProdottiPerPagina")
    public Integer getProdottiPerPagina() {
        return prodottiPerPagina;
    }

    public void setProdottiPerPagina(Integer prodottiPerPagina) {
        this.prodottiPerPagina = prodottiPerPagina;
    }

    @DynamoDbAttribute("Tema")
    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    @DynamoDbAttribute("Layout")
    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    @DynamoDbAttribute("Lingua")
    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    @DynamoDbAttribute("Notifiche")
    public List<Notifica> getNotifiche() {
        return notifiche;
    }

    public void setNotifiche(List<Notifica> notifiche) {
        this.notifiche = notifiche;
    }

}
