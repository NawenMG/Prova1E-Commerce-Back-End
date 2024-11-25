package com.prova.e_commerce.dbRel.awsRds.jpa.service;

import com.prova.e_commerce.dbRel.awsRds.jpa.entity.Users;
import com.prova.e_commerce.dbRel.awsRds.jpa.repository.classi.UsersRepImpJPA;
import com.prova.e_commerce.storage.S3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceJPA {

    @Autowired
    private UsersRepImpJPA usersRepImpJPA;  // Repository per l'accesso ai dati utente nel database

    @Autowired
    private S3Service s3Service;  // Servizio per la gestione dei file su AWS S3

     @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;  // Template per inviare messaggi Kafka

    private static final String TOPIC_EVENTI_UTENTE_AGGIUNGI = "eventi-utente-topic-aggiungi";
    private static final String TOPIC_EVENTI_UTENTE_AGGIORNA = "eventi-utente-topic-aggiorna";  


    /**
     * Metodo per ottenere tutti gli utenti.
     * La cache è attiva per 10 minuti in Caffeine e 30 minuti in Redis.
     * La chiave della cache è 'allUsers'.
     */
    @Cacheable(value = {"usersCache", "caffeine", "redis"}, key = "'allUsers'", unless = "#result == null")
    public List<Users> getAllUsers() {
        return usersRepImpJPA.findAll();  // Recupera tutti gli utenti dal repository
    }

    /**
     * Metodo per trovare un utente per ID.
     * La cache è attiva per 10 minuti in Caffeine e 30 minuti in Redis.
     * La chiave della cache è l'ID dell'utente.
     */
    @Cacheable(value = {"usersCache", "caffeine", "redis"}, key = "#id", unless = "#result == null")
    public Optional<Users> getUserById(String id) {
        return usersRepImpJPA.findById(id);  // Recupera un utente in base all'ID dal repository
    }

    /**
     * Metodo per trovare utenti per nome.
     * La cache è attiva per 10 minuti in Caffeine e 30 minuti in Redis.
     * La chiave della cache è il nome dell'utente.
     */
    @Cacheable(value = {"usersCache", "caffeine", "redis"}, key = "#firstName", unless = "#result == null")
    public List<Users> getUsersByFirstName(String firstName) {
        return usersRepImpJPA.findByFirstName(firstName);  // Recupera utenti con il primo nome specificato
    }

    /**
     * Metodo per trovare utenti per cognome.
     * La cache è attiva per 10 minuti in Caffeine e 30 minuti in Redis.
     * La chiave della cache è il cognome dell'utente.
     */
    @Cacheable(value = {"usersCache", "caffeine", "redis"}, key = "#lastName", unless = "#result == null")
    public List<Users> getUsersByLastName(String lastName) {
        return usersRepImpJPA.findByLastName(lastName);  // Recupera utenti con il cognome specificato
    }

    /**
     * Metodo per trovare un utente per nome utente.
     * La cache è attiva per 10 minuti in Caffeine e 30 minuti in Redis.
     * La chiave della cache è il nome utente dell'utente.
     */
    @Cacheable(value = {"usersCache", "caffeine", "redis"}, key = "#username", unless = "#result == null")
    public Optional<Users> getUserByUsername(String username) {
        return usersRepImpJPA.findByUsername(username);  // Recupera un utente con il nome utente specificato
    }

    /**
     * Metodo per trovare un utente per email.
     * La cache è attiva per 10 minuti in Caffeine e 30 minuti in Redis.
     * La chiave della cache è l'email dell'utente.
     */
    @Cacheable(value = {"usersCache", "caffeine", "redis"}, key = "#email", unless = "#result == null")
    public Optional<Users> getUserByEmail(String email) {
        return usersRepImpJPA.findByEmail(email);  // Recupera un utente con l'email specificata
    }

    /**
     * Metodo per aggiungere un nuovo utente.
     * Invalida la cache di tutti gli utenti dopo l'operazione.
     */
    @CacheEvict(value = {"usersCache", "caffeine", "redis"}, key = "'allUsers'", allEntries = true)
    public void addUser(Users user) {
        usersRepImpJPA.insert(user);  // Aggiunge un nuovo utente nel repository
        kafkaTemplate.send(TOPIC_EVENTI_UTENTE_AGGIUNGI, "Aggiunto", "Nuovo utente aggiunto con ID: " + user.getUsersID());

    }

    /**
     * Metodo per aggiornare un utente esistente.
     * Invalida la cache dell'utente dopo l'operazione.
     */
    @CacheEvict(value = {"usersCache", "caffeine", "redis"}, key = "#user.id", allEntries = false)
    public void updateUser(Users user) {
        usersRepImpJPA.update(user);  // Aggiorna le informazioni dell'utente nel repository
        kafkaTemplate.send(TOPIC_EVENTI_UTENTE_AGGIORNA, "Aggiornato", "Utente aggiornato con ID: " + user.getUsersID());

    }

    /**
     * Metodo per eliminare un utente per ID.
     * Invalida la cache dell'utente dopo l'operazione.
     */
    @CacheEvict(value = {"usersCache", "caffeine", "redis"}, key = "#id", allEntries = false)
    public void deleteUserById(String id) {
        usersRepImpJPA.deleteById(id);  // Elimina un utente in base all'ID dal repository

    }

    /**
     * Metodo per caricare l'immagine di un utente su S3.
     * Invalida la cache dell'utente dopo l'operazione.
     */
    @CacheEvict(value = {"usersCache", "caffeine", "redis"}, key = "#userId", allEntries = false)
    public String uploadUserImage(String userId, MultipartFile file) throws IOException {
        // Verifica se l'utente esiste
        Optional<Users> userOpt = usersRepImpJPA.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        Users user = userOpt.get();

        // Carica il file su S3 e ottieni l'URL
        String imageUrl = s3Service.uploadFile("users/images", file); // 'users/images' è la cartella nel bucket S3

        // Aggiorna l'utente con l'URL dell'immagine
        user.setImmagine(imageUrl.getBytes());
        usersRepImpJPA.update(user);  // Salva l'utente con il nuovo URL dell'immagine

        return imageUrl;  // Restituisce l'URL dell'immagine caricata
    }

    /**
     * Metodo per scaricare l'immagine di un utente da S3.
     */
    public InputStream downloadUserImage(String userId) throws IOException {
        // Verifica se l'utente esiste
        Optional<Users> userOpt = usersRepImpJPA.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        Users user = userOpt.get();
        String imageUrl = new String(user.getImmagine());  // Ottiene l'URL dell'immagine

        // Scarica il file da S3 utilizzando l'URL
        return s3Service.downloadFile(imageUrl);
    }

    /**
     * Metodo per eliminare l'immagine di un utente da S3.
     * Invalida la cache dell'utente dopo l'operazione.
     */
    @CacheEvict(value = {"usersCache", "caffeine", "redis"}, key = "#userId", allEntries = false)
    public void deleteUserImage(String userId) {
        // Verifica se l'utente esiste
        Optional<Users> userOpt = usersRepImpJPA.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        Users user = userOpt.get();
        String imageUrl = new String(user.getImmagine());  // Ottiene l'URL dell'immagine

        // Elimina il file da S3
        s3Service.deleteFile(imageUrl);

        // Rimuovi l'URL dell'immagine dall'utente
        user.setImmagine(null);
        usersRepImpJPA.update(user);  // Salva l'utente senza l'URL dell'immagine
    }
}
