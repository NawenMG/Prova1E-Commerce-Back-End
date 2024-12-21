package com.prova.e_commerce.dbKey.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import com.prova.e_commerce.dbKey.model.SottoClassi.Notifica;

public class SettingSite {

    @NotNull(message = "L'ID utente è obbligatorio")
    @Size(min = 3, max = 100, message = "L'ID utente deve essere tra 3 e 100 caratteri")
    private String _key;  // La chiave del documento in ArangoDB (equivalente a DynamoDB Partition Key)

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

    // Metodo per ottenere la chiave del documento in ArangoDB
    public String getKey() {
        return _key;
    }

    // Metodo per impostare la chiave del documento in ArangoDB
    public void setKey(String key) {
        this._key = key;
    }

    // Getter e setter per prodotti per pagina
    public Integer getProdottiPerPagina() {
        return prodottiPerPagina;
    }

    public void setProdottiPerPagina(Integer prodottiPerPagina) {
        this.prodottiPerPagina = prodottiPerPagina;
    }

    // Getter e setter per il tema
    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    // Getter e setter per il layout
    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    // Getter e setter per la lingua
    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    // Getter e setter per le notifiche
    public List<Notifica> getNotifiche() {
        return notifiche;
    }

    public void setNotifiche(List<Notifica> notifiche) {
        this.notifiche = notifiche;
    }
}
