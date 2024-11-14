package com.prova.e_commerce.dbDoc.randomData;

import com.github.javafaker.Faker;
import com.prova.e_commerce.dbDoc.entity.Recensioni;
import com.prova.e_commerce.dbDoc.entity.Recensioni.Risposta;

import java.util.ArrayList;
import java.util.List;

public class RecensioniFaker {

    private Faker faker = new Faker();

    public Recensioni generateFakeReview() {
        Recensioni recensione = new Recensioni();

        // ID recensione casuale (esempio: "REV1234")
        recensione.setId("REV" + faker.number().numberBetween(1000, 9999));

        // ID utente e prodotto casuali
        recensione.setUserId("USER" + faker.number().numberBetween(1000, 9999));
        recensione.setProductId("PROD" + faker.number().numberBetween(1000, 9999));

        // Voto casuale tra 1 e 5
        recensione.setVoto(faker.number().numberBetween(1, 5));

        // Titolo e descrizione casuali
        recensione.setTitolo(faker.lorem().sentence(3));
        recensione.setDescrizione(faker.lorem().paragraph(2));

        // Immagine e video URL (opzionali)
        recensione.setImmagine(faker.internet().url() + "/image.jpg");
        recensione.setVideo(faker.internet().url() + "/video.mp4");

        // Like e dislike casuali
        recensione.setLike(faker.number().numberBetween(0, 500));
        recensione.setDislike(faker.number().numberBetween(0, 100));

        // Lista di risposte casuali
        recensione.setRisposte(generateFakeResponses());

        return recensione;
    }

    // Metodo per generare una lista di risposte casuali
    private List<Risposta> generateFakeResponses() {
        List<Risposta> risposte = new ArrayList<>();
        int numRisposte = faker.number().numberBetween(0, 5); // Genera fino a 5 risposte

        for (int i = 0; i < numRisposte; i++) {
            Risposta risposta = new Risposta();
            risposta.setUserId("USER" + faker.number().numberBetween(1000, 9999));
            risposta.setTesto(faker.lorem().sentence(5));
            risposta.setDataRisposta(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS).toString());

            risposte.add(risposta);
        }
        return risposte;
    }
}
