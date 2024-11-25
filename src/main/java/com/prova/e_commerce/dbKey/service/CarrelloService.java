package com.prova.e_commerce.dbKey.service;

import com.prova.e_commerce.dbKey.model.Carrello;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.CarrelloRep;
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
public class CarrelloService {

    @Autowired
    private CarrelloRep carrelloRep;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_EVENTI_CARRELLO_AGGIUNGI = "eventi-carrello-topic-aggiungi";  // Nome del topic Kafka

    /**
     * Metodo per aggiungere nuovi prodotti al carrello e invalidare la cache.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void aggiungiProdotti(String userId, List<Prodotto> nuoviProdotti) {
     if (nuoviProdotti == null || nuoviProdotti.isEmpty()) {
        throw new IllegalArgumentException("La lista dei prodotti non può essere vuota o null.");
     }
     carrelloRep.aggiungiProdotti(userId, nuoviProdotti);

     // Invia evento Kafka per l'aggiunta di prodotti
     kafkaTemplate.send(TOPIC_EVENTI_CARRELLO_AGGIUNGI, "ProdottiAggiunti", "Prodotti aggiunti al carrello dell'utente " + userId);
    }


    /**
     * Metodo per ottenere il carrello di un utente. La cache è attiva per 10 minuti in Caffeine e 30 minuti in Redis.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#userId", unless = "#result == null")
    public Optional<Carrello> getCarrello(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
        }
        return carrelloRep.trovaCarrello(userId);
    }

    /**
     * Metodo per rimuovere un prodotto dal carrello e invalidare la cache.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void rimuoviProdotto(String userId, String prodottoId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
        }
        if (prodottoId == null || prodottoId.isEmpty()) {
            throw new IllegalArgumentException("Il prodottoId non può essere null o vuoto.");
        }
        carrelloRep.eliminaProdotto(userId, prodottoId);
    }

    /**
     * Metodo per svuotare il carrello di un utente e invalidare la cache.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void svuotaCarrello(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
        }
        carrelloRep.resetCarrello(userId);
    }

    // **Gestione dei file per i prodotti**

    /**
     * Metodo per caricare un'immagine del prodotto e invalidare la cache.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public String caricaImmagineProdotto(String userId, String prodottoId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Il file non può essere null o vuoto.");
        }

        // Recupera il carrello dell'utente
        Carrello carrello = carrelloRep.trovaCarrello(userId)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato per l'userId: " + userId));

        // Trova il prodotto specifico
        Prodotto prodotto = carrello.getProdotti().stream()
                .filter(p -> p.getProductId().equals(prodottoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato nel carrello: " + prodottoId));

        // Carica l'immagine su S3
        String folder = "carrello/" + userId + "/" + prodottoId;
        String fileUrl = s3Service.uploadFile(folder, file);

        // Aggiorna il prodotto con l'URL del file
        prodotto.setImmagine(fileUrl);

        // Salva nuovamente il carrello
        carrelloRep.resetCarrello(userId); // Reset temporaneo
        carrelloRep.aggiungiProdotti(userId, carrello.getProdotti());

        return fileUrl;
    }

    /**
     * Metodo per scaricare l'immagine del prodotto.
     */
    public InputStream scaricaImmagineProdotto(String userId, String prodottoId) throws IOException {
        // Recupera il carrello dell'utente
        Carrello carrello = carrelloRep.trovaCarrello(userId)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato per l'userId: " + userId));

        // Trova il prodotto specifico
        Prodotto prodotto = carrello.getProdotti().stream()
                .filter(p -> p.getProductId().equals(prodottoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato nel carrello: " + prodottoId));

        String fileUrl = prodotto.getImmagine();

        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new RuntimeException("Nessuna immagine trovata per il prodotto: " + prodottoId);
        }

        // Estrai la chiave del file dall'URL
        String key = fileUrl.substring(fileUrl.indexOf("amazonaws.com/") + 14);

        return s3Service.downloadFile(key);
    }

    /**
     * Metodo per eliminare l'immagine del prodotto e invalidare la cache.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void eliminaImmagineProdotto(String userId, String prodottoId) {
        // Recupera il carrello dell'utente
        Carrello carrello = carrelloRep.trovaCarrello(userId)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato per l'userId: " + userId));

        // Trova il prodotto specifico
        Prodotto prodotto = carrello.getProdotti().stream()
                .filter(p -> p.getProductId().equals(prodottoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato nel carrello: " + prodottoId));

        String fileUrl = prodotto.getImmagine();

        if (fileUrl != null && !fileUrl.isEmpty()) {
            // Estrai la chiave del file dall'URL
            String key = fileUrl.substring(fileUrl.indexOf("amazonaws.com/") + 14);

            // Elimina il file su S3
            s3Service.deleteFile(key);

            // Rimuovi l'URL dal prodotto
            prodotto.setImmagine(null);

            // Salva nuovamente il carrello
            carrelloRep.resetCarrello(userId); // Reset temporaneo
            carrelloRep.aggiungiProdotti(userId, carrello.getProdotti());
        } else {
            throw new RuntimeException("Nessuna immagine associata al prodotto: " + prodottoId);
        }
    }
}
