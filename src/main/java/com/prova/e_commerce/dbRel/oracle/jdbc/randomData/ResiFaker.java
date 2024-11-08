package com.prova.e_commerce.dbRel.oracle.jdbc.randomData;

import com.github.javafaker.Faker;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Resi;

import java.time.LocalDate;

public class ResiFaker {

    private Faker faker = new Faker();

    public Resi generateFakeReturn(int number) {
        Resi returnObject = new Resi();

        // Genera un ID del reso come stringa "RESO1234"
        returnObject.setReturnsID("RESO" + faker.number().numberBetween(1000, 9999));

        // Genera un ID utente come "USER123"
        returnObject.setUsersID("USER" + faker.number().numberBetween(1, 1000));

        // Stato del reso (true/false)
        returnObject.setStatus(faker.bool().bool());

        // Accettazione del reso (true/false)
        returnObject.setAccettazioneReso(faker.bool().bool());

        // Data di richiesta del reso (negli ultimi 30 giorni)
        returnObject.setDataRichiesta(LocalDate.now().minusDays(faker.number().numberBetween(1, 30)));

        return returnObject;
    }
}
