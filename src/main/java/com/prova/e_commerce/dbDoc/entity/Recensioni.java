package com.prova.e_commerce.dbDoc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.bson.types.Binary;

import javax.validation.constraints.*;
import java.util.List;

@Document(collection = "Recensioni")
public class Recensioni {

    @Id
    private String id; // ID recensione

    @Indexed
    @Field("User_id")
    @NotNull(message = "User_id non può essere nullo")
    @Size(min = 1, max = 255, message = "User_id deve essere tra 1 e 255 caratteri")
    private String userId; // ID dell'utente che ha scritto la recensione

    @Indexed
    @Field("Product_id")
    @NotNull(message = "Product_id non può essere nullo")
    @Size(min = 1, max = 255, message = "Product_id deve essere tra 1 e 255 caratteri")
    private String productId; // ID del prodotto recensito

    @Field("voto")
    @Min(value = 1, message = "Il voto deve essere almeno 1")
    @Max(value = 5, message = "Il voto deve essere massimo 5")
    private Integer voto; // Voto (da 1 a 5, ad esempio)

    @Field("titolo")
    @NotNull(message = "Il titolo della recensione non può essere nullo")
    @Size(min = 1, max = 255, message = "Il titolo deve essere tra 1 e 255 caratteri")
    private String titolo; // Titolo della recensione

    @Field("descrizione")
    @Size(max = 2000, message = "La descrizione non può superare i 2000 caratteri")
    private String descrizione; // Descrizione della recensione

    // Modifica il tipo del campo immagine da String a Binary (BLOB)
    @Field("immagine")
    private Binary immagine; // File immagine (come BLOB)

    // Modifica il tipo del campo video da String a Binary (BLOB)
    @Field("video")
    private Binary video; // File video (come BLOB)

    @Field("like")
    @PositiveOrZero(message = "Il numero di like deve essere maggiore o uguale a zero")
    private Integer like; // Numero di like

    @Field("dislike")
    @PositiveOrZero(message = "Il numero di dislike deve essere maggiore o uguale a zero")
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

    public Binary getImmagine() {
        return immagine;
    }

    public void setImmagine(Binary immagine) {
        this.immagine = immagine;
    }

    public Binary getVideo() {
        return video;
    }

    public void setVideo(Binary video) {
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
