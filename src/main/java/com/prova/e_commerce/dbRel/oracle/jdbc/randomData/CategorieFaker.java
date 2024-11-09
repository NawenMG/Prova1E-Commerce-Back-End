package com.prova.e_commerce.dbRel.oracle.jdbc.randomData;

import com.github.javafaker.Faker;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Categorie;

public class CategorieFaker {

    private Faker faker = new Faker();

    public Categorie generateFakeCategory(int number) {
        Categorie category = new Categorie();

        // Genera un ID casuale come stringa (ad esempio un UUID o una stringa numerica)
        category.setCategoryID("CAT" + faker.number().numberBetween(1000, 9999)); // Genera un ID come "CAT1234"

        // Genera un nome di categoria casuale
        category.setName(faker.commerce().department()); // Nome di una categoria fittizia (es. 'Electronics')

        return category;
    }
}
