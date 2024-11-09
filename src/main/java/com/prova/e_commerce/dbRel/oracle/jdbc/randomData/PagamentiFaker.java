package com.prova.e_commerce.dbRel.oracle.jdbc.randomData;

import com.github.javafaker.Faker;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;

import java.time.LocalDate;
import java.math.BigDecimal;

public class PagamentiFaker {

    private Faker faker = new Faker();

    public Pagamenti generateFakePayment(int number) {
        Pagamenti payment = new Pagamenti();

        // Genera un ID pagamento casuale come stringa (esempio: "PAY1234")
        payment.setPaymentsID("PAY" + faker.number().numberBetween(1000, 9999));

        // Tipo di pagamento casuale
        payment.setType(faker.options().option("Credit Card", "PayPal", "Bank Transfer", "Cash", "Bitcoin", "Stripe"));

        // Data del pagamento casuale (negli ultimi 30 giorni)
        payment.setData(LocalDate.now().minusDays(faker.number().numberBetween(1, 30)));

        // Stato del pagamento (true/false)
        payment.setStatus(faker.bool().bool());

        // Totale del pagamento (tra 10 e 1000 con 2 decimali)
        payment.setTotal(BigDecimal.valueOf(faker.number().randomDouble(2, 10, 1000)));

        return payment;
    }
}
