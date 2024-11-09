package com.prova.e_commerce.dbRel.oracle.jdbc.randomData;

import com.github.javafaker.Faker;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Ordini;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrdiniFaker {

    private Faker faker = new Faker();

    public Ordini generateFakeOrder(int number) {
        Ordini order = new Ordini();

        // Genera un ID ordine casuale come stringa
        order.setOrderID("ORD" + faker.number().numberBetween(1000, 9999)); // Esempio di ID come "ORD1234"

        // Genera un ID utente casuale (String)
        order.setUsersID("USER" + faker.number().numberBetween(1, 1000)); // Esempio di ID come "USER123"

        // Stato di spedizione come un LocalDateTime
        order.setStatoDiSpedizione(LocalDateTime.now().minusHours(faker.number().numberBetween(1, 12))); // Stato di spedizione nelle ultime 12 ore

        // Data di consegna casuale (tra 1 e 10 giorni dopo oggi)
        order.setDataDiConsegna(LocalDate.now().plusDays(faker.number().numberBetween(1, 10)));

        // Data di richiesta (tra 1 e 10 giorni prima di oggi)
        order.setDataDiRichiesta(LocalDate.now().minusDays(faker.number().numberBetween(1, 10)));

        // Accettazione ordine (booleano)
        order.setAccettazioneOrdine(faker.bool().bool());

        // Stato dell'ordine (es. "In Progress" o "Shipped")
        order.setStatus(faker.options().option("In Progress", "Shipped", "Delivered", "Canceled"));

        // Nome del corriere
        order.setCorriere(faker.company().name());

        // Posizione (città casuale)
        order.setPosizione(faker.address().cityName());

        return order;
    }
}
