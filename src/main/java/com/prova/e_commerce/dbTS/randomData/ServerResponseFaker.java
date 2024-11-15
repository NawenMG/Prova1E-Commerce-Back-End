package com.prova.e_commerce.dbTS.randomData;

import com.github.javafaker.Faker;
import com.prova.e_commerce.dbTS.model.ServerResponse;

import java.time.Instant;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class ServerResponseFaker {

    private static Faker faker = new Faker();
    private static Random random = new Random();

    // Metodo per generare un oggetto ServerResponse casuale
    public ServerResponse generateRandomServerResponse() {
        // Genera i dati casuali
        String server = "server-" + faker.internet().domainWord();  // Server casuale
        String endpoint = "/api/" + faker.commerce().department(); // Endpoint casuale
        Double responseTimeAverage = random.nextDouble() * 2000; // Tempo di risposta casuale tra 0 e 2000 ms
        Integer numeroDiRequest = random.nextInt(1000) + 1;  // Numero casuale di richieste tra 1 e 1000
        Integer numeroDiErrori = random.nextInt(100); // Numero casuale di errori tra 0 e 100
        Instant time = Instant.now().minusSeconds(random.nextInt(1000000));  // Timestamp casuale nel passato

        // Restituisce un'istanza di ServerResponse con i dati casuali
        return new ServerResponse(server, endpoint, responseTimeAverage, numeroDiRequest, numeroDiErrori, time);
    }
}