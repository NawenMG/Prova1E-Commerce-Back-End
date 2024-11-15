package com.prova.e_commerce.dbTS.randomData;

import com.github.javafaker.Faker;
import com.prova.e_commerce.dbTS.model.SalesMonitoring;

import java.time.Instant;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class SalesMonitoringFaker {

    private static Faker faker = new Faker();
    private static Random random = new Random();

    // Metodo per generare un oggetto SalesMonitoring casuale
    public SalesMonitoring generateRandomSalesMonitoring() {
        // Genera i dati casuali
        String prodotto = faker.commerce().productName();
        String categoriaProdotto = faker.commerce().department();
        String venditore = faker.name().fullName();
        Integer numeroOrdini = random.nextInt(1000) + 1;  // Numero casuale tra 1 e 1000
        Integer numeroUnitaVendute = random.nextInt(500) + 1; // Numero casuale tra 1 e 500
        Double ricavo = random.nextDouble() * 1000;  // Ricavo casuale tra 0 e 1000
        Instant time = Instant.now().minusSeconds(random.nextInt(1000000));  // Tempo casuale nel passato

        // Restituisce un'istanza di SalesMonitoring con i dati casuali
        return new SalesMonitoring(prodotto, categoriaProdotto, venditore, numeroOrdini, numeroUnitaVendute, ricavo, time);
    }
}