package com.prova.e_commerce.dbTS.randomData;

import com.github.javafaker.Faker;
import com.prova.e_commerce.dbTS.model.TrafficAnalysis;

import java.time.Instant;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class TrafficAnalysisFaker {

    private static Faker faker = new Faker();
    private static Random random = new Random();

    // Metodo per generare un oggetto TrafficAnalysis casuale
    public TrafficAnalysis generateRandomTrafficAnalysis() {
        // Genera i dati casuali
        String urlPagina = "https://www." + faker.internet().domainName() + "/" + faker.internet().slug();  // URL pagina casuale
        Integer numeroDiVisite = random.nextInt(10000) + 1;  // Numero casuale di visite tra 1 e 10000
        Integer numeroDiVisitatoriUnici = random.nextInt(numeroDiVisite) + 1;  // Numero casuale di visitatori unici tra 1 e numeroDiVisite
        Double durataMediaVisite = random.nextDouble() * 300;  // Durata media delle visite (tra 0 e 300 secondi)
        Instant time = Instant.now().minusSeconds(random.nextInt(1000000));  // Timestamp casuale nel passato

        // Restituisce un'istanza di TrafficAnalysis con i dati casuali
        return new TrafficAnalysis(urlPagina, numeroDiVisite, numeroDiVisitatoriUnici, durataMediaVisite, time);
    }
}