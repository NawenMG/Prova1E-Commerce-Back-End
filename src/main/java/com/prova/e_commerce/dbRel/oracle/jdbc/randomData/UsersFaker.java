package com.prova.e_commerce.dbRel.oracle.jdbc.randomData;

import com.github.javafaker.Faker;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Users;

public class UsersFaker {

    private Faker faker = new Faker();

    public Users generateFakeUser(int number) {
        Users user = new Users(); // Crea un'istanza vuota di Users

        // Usa i metodi setter per impostare i valori fittizi
        user.setUsersID("USER" + faker.number().numberBetween(1000, 9999));   // ID utente formattato
        user.setNome(faker.name().firstName());                               // Nome casuale
        user.setCognome(faker.name().lastName());                             // Cognome casuale
        user.setRuolo(generateRandomRole());                                  // Ruolo casuale da lista predefinita
        user.setNomeUtente(generateRandomUserName());                         // Nome utente casuale
        user.setEmail(faker.internet().emailAddress());                       // Email casuale
        user.setPassword(faker.internet().password(8, 16));                   // Password casuale tra 8 e 16 caratteri
        user.setImmagine(faker.internet().avatar().getBytes());               // Immagine avatar casuale (byte[])

        return user; // Restituisce l'oggetto Users con i dati fittizi
    }

    // Metodo per generare un ruolo casuale da una lista predefinita
    private String generateRandomRole() {
        String[] roles = {"admin", "utente", "moderatore", "guest", "customer"};
        return roles[faker.number().numberBetween(0, roles.length)];
    }

    // Metodo per generare un nome utente casuale
    private String generateRandomUserName() {
        return faker.internet().domainWord() + faker.number().numberBetween(100, 999); // Combinazione parola + numero
    }
}
