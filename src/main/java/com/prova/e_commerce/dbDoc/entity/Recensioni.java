package com.prova.e_commerce.dbDoc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "Recensioni")
public class Recensioni {

    @Id
    private String id; // ID recensione

    @Indexed
    @Field("User_id")
    private String userId; // ID dell'utente che ha scritto la recensione

    @Indexed
    @Field("Product_id")
    private String productId; // ID del prodotto recensito

    @Field("voto")
    private Integer voto; // Voto (da 1 a 5, ad esempio)

    @Field("titolo")
    private String titolo; // Titolo della recensione

    @Field("descrizione")
    private String descrizione; // Descrizione della recensione

    @Field("immagine")
    private String immagine; // URL dell'immagine (opzionale)

    @Field("video")
    private String video; // URL del video (opzionale)

    @Field("like")
    private Integer like; // Numero di like

    @Field("dislike")
    private Integer dislike; // Numero di dislike

    @Field("risposte")
    private List<Risposta> risposte; // Lista di risposte alla recensione (opzionale)

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getVoto() {
        return voto;
    }

    public void setVoto(Integer voto) {
        this.voto = voto;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public Integer getDislike() {
        return dislike;
    }

    public void setDislike(Integer dislike) {
        this.dislike = dislike;
    }

    public List<Risposta> getRisposte() {
        return risposte;
    }

    public void setRisposte(List<Risposta> risposte) {
        this.risposte = risposte;
    }

    // Classe per rappresentare una risposta alla recensione
    public static class Risposta {
        private String userId; // ID dell'utente che risponde
        private String testo; // Testo della risposta
        private String dataRisposta; // Data della risposta

        // Getters and Setters
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTesto() {
            return testo;
        }

        public void setTesto(String testo) {
            this.testo = testo;
        }

        public String getDataRisposta() {
            return dataRisposta;
        }

        public void setDataRisposta(String dataRisposta) {
            this.dataRisposta = dataRisposta;
        }
    }
}
