package com.prova.e_commerce.dbTS.randomData;

import com.github.javafaker.Faker;
import com.prova.e_commerce.dbTS.model.UserAnalysis;

import java.time.Instant;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class UserAnalysisFaker {

    private static Faker faker = new Faker();
    private static Random random = new Random();

    // Metodo per generare un oggetto UserAnalysis casuale
    public UserAnalysis generateRandomUserAnalysis() {
        // Genera i dati casuali
        String utente = faker.name().username();  // Nome utente casuale
        String tipoDiDispositivo = random.nextBoolean() ? "mobile" : "desktop";  // Tipo di dispositivo casuale
        String azione = getRandomAction();  // Azione casuale
        Double durataAzione = random.nextDouble() * 120;  // Durata azione casuale (tra 0 e 120 secondi)
        Instant time = Instant.now().minusSeconds(random.nextInt(1000000));  // Timestamp casuale nel passato

        // Restituisce un'istanza di UserAnalysis con i dati casuali
        return new UserAnalysis(utente, tipoDiDispositivo, azione, durataAzione, time);
    }

    // Metodo per ottenere un'azione casuale
    private String getRandomAction() {
        String[] azioni = {"login", "purchase", "view_item", "logout", "add_to_cart", "checkout"};
        return azioni[random.nextInt(azioni.length)];
    }
}