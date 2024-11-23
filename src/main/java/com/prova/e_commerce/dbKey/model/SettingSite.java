package com.prova.e_commerce.dbKey.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import com.prova.e_commerce.dbKey.model.SottoClassi.Notifica;

@DynamoDbBean
public class SettingSite {

    @NotNull(message = "L'ID utente è obbligatorio")
    @Size(min = 3, max = 100, message = "L'ID utente deve essere tra 3 e 100 caratteri")
    private String userId;

    @NotNull(message = "Il numero di prodotti per pagina non può essere nullo")
    @Min(value = 1, message = "I prodotti per pagina devono essere almeno 1")
    private Integer prodottiPerPagina;

    @NotNull(message = "Il tema è obbligatorio")
    @NotEmpty(message = "Il tema non può essere vuoto")
    private String tema;

    @NotNull(message = "Il layout è obbligatorio")
    @NotEmpty(message = "Il layout non può essere vuoto")
    private String layout;

    @NotNull(message = "La lingua è obbligatoria")
    @NotEmpty(message = "La lingua non può essere vuota")
    private String lingua;

    @NotNull(message = "La lista delle notifiche non può essere nulla")
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
