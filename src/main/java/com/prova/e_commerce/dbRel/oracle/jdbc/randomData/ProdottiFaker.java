package com.prova.e_commerce.dbRel.oracle.jdbc.randomData;

import com.github.javafaker.Faker;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Prodotti;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProdottiFaker {

    private Faker faker = new Faker();

    public Prodotti generateFakeProduct( int number) {
        Prodotti product = new Prodotti();

        // Genera un ID prodotto come stringa "PROD1234"
        product.setProductId("PROD" + faker.number().numberBetween(1000, 9999));

        // Nome del prodotto casuale
        product.setNome(faker.commerce().productName());

        // Prezzo del prodotto tra 5 e 1000, con 2 decimali
        product.setPrezzo(BigDecimal.valueOf(faker.number().randomDouble(2, 5, 1000)));

        // Descrizione casuale, può essere estesa con più frasi
        product.setDescrizione(faker.lorem().sentence(5));

        // Immagine casuale - in questo caso, usiamo l'URL di un avatar, che è una stringa
/*         product.setImmagine(faker.internet().avatar());
 */
        // Quantità disponibile casuale
        product.setAmountAvailable(faker.number().numberBetween(0, 100));

        // Categoria del prodotto
        product.setCategoria(faker.commerce().department());

        // Data di inserimento (un anno fa al massimo)
        product.setDataDiInserimento(LocalDate.now().minusDays(faker.number().numberBetween(1, 365)));

        return product;
    }
}
