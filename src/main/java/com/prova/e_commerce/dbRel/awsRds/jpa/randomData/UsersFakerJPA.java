package com.prova.e_commerce.dbRel.awsRds.jpa.randomData;

import com.github.javafaker.Faker;
import com.prova.e_commerce.dbRel.awsRds.jpa.entity.Users;
import com.prova.e_commerce.dbRel.awsRds.jpa.repository.interfacce.UsersRepJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsersFakerJPA {

    private Faker faker = new Faker();

    // Autowiring del repository per salvare i dati nel DB
    @Autowired
    private UsersRepJPA usersRepository;

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

        // Salva l'utente nel database tramite JPA
        usersRepository.save(user);

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
