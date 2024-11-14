package com.prova.e_commerce.dbDoc.randomData;

import com.github.javafaker.Faker;
import com.prova.e_commerce.dbDoc.entity.SchedeProdotti;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SchedeProdottiFaker {

    private Faker faker = new Faker();

    public SchedeProdotti generateFakeProduct() {
        SchedeProdotti prodotto = new SchedeProdotti();

        // ID prodotto casuale (esempio: "PROD1234")
        prodotto.setId("PROD" + faker.number().numberBetween(1000, 9999));

        // Nome prodotto casuale
        prodotto.setNome(faker.commerce().productName());

        // Prezzo casuale tra 5 e 500 con due decimali
        prodotto.setPrezzo(BigDecimal.valueOf(faker.number().randomDouble(2, 5, 500)));

        // Parametri descrittivi casuali
        prodotto.setParametriDescrittivi(generateFakeParametriDescrittivi());

        return prodotto;
    }

    // Metodo per generare parametri descrittivi casuali
    private Map<String, String> generateFakeParametriDescrittivi() {
        Map<String, String> parametri = new HashMap<>();
        
        // Aggiunge alcune caratteristiche casuali come "Colore", "Dimensioni", etc.
        parametri.put("Colore", faker.options().option("Rosso", "Blu", "Verde", "Giallo", "Nero", "Bianco"));
        parametri.put("Dimensioni", faker.options().option("S", "M", "L", "XL"));
        parametri.put("Materiale", faker.options().option("Cotone", "Poliestere", "Legno", "Metallo", "Plastica"));
        parametri.put("Peso", faker.number().numberBetween(100, 5000) + " g");
        parametri.put("Paese di origine", faker.country().name());

        return parametri;
    }
}
