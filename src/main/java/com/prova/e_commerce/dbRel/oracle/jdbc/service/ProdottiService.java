package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Prodotti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.ProdottiRep;
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

@Service
public class ProdottiService {
    
    @Autowired
    private ProdottiRep prodottiRep;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Kafka producer

    private static final String KAFKA_TOPIC_PRODOTTI_AGGIUNGI = "prodotti-topic-aggiungi"; 
    private static final String KAFKA_TOPIC_PRODOTTI_AGGIORNA = "prodotti-topic-aggiorna";

    /**
     * Metodo per eseguire una query avanzata sui prodotti in base a parametri dinamici.
     * Utilizza Caffeine per 10 minuti e Redis per 30 minuti.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #prodotti.toString()")
    public List<Prodotti> queryProdotti(ParamQuery paramQuery, Prodotti prodotti) {
        return prodottiRep.query(paramQuery, prodotti);
    }

    /**
     * Metodo per inserire un nuovo prodotto.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String inserisciProdotto(Prodotti prodotto) {
        kafkaTemplate.send(KAFKA_TOPIC_PRODOTTI_AGGIUNGI, "Nuovo prodotto inserito: " + prodotto.getProductId());
        return prodottiRep.insertProduct(prodotto);

    }

    /**
     * Metodo per aggiornare un prodotto esistente in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String aggiornaProdotto(String productID, Prodotti prodotto) {
        kafkaTemplate.send(KAFKA_TOPIC_PRODOTTI_AGGIORNA, "Prodotto aggiornato: " + productID);
        return prodottiRep.updateProduct(productID, prodotto);
    }

    /**
     * Metodo per eliminare un prodotto in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String eliminaProdotto(String productID) {
        return prodottiRep.deleteProduct(productID);
    }

    /**
     * Metodo per generare un numero specificato di prodotti con dati casuali.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String salvaProdottiCasuali(int numero) {
        return prodottiRep.saveAll(numero);
    }

    // Metodo per caricare un'immagine su S3 e associarla a un prodotto
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String caricaImmagineProdotto(String productId, MultipartFile file) throws IOException {
        // Carica il file su S3 e ottieni l'URL
        String fileUrl = s3Service.uploadFile("prodotti/" + productId, file);

        // Recupera il prodotto dal database
        Prodotti prodotto = prodottiRep.query(new ParamQuery(), new Prodotti()).stream()
                                       .filter(p -> p.getProductId().equals(productId))
                                       .findFirst()
                                       .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        // Imposta l'URL dell'immagine nel prodotto
        prodotto.setImmagine(fileUrl);  


        // Aggiorna il prodotto nel database con il nuovo URL dell'immagine
        return prodottiRep.updateProduct(productId, prodotto);
    }

    // Metodo per scaricare l'immagine di un prodotto da S3
    public InputStream scaricaImmagineProdotto(String productId) throws IOException {
        // Recupera il prodotto dal database
        Prodotti prodotto = prodottiRep.query(new ParamQuery(), new Prodotti()).stream()
                                       .filter(p -> p.getProductId().equals(productId))
                                       .findFirst()
                                       .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        // Ottieni l'URL dell'immagine
        String fileUrl = prodotto.getImmagine();  

        
        
        // Estrai la chiave dal URL dell'immagine (assumiamo che contenga la chiave completa)
        String key = fileUrl.substring(fileUrl.indexOf("amazonaws.com/") + 14);  // 14 è la lunghezza di "amazonaws.com/"
        

        return s3Service.downloadFile(key);
    }

    // Metodo per eliminare l'immagine di un prodotto su S3
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void eliminaImmagineProdotto(String productId) {
        // Recupera il prodotto dal database
        Prodotti prodotto = prodottiRep.query(new ParamQuery(), new Prodotti()).stream()
                                       .filter(p -> p.getProductId().equals(productId))
                                       .findFirst()
                                       .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        // Ottieni l'URL dell'immagine
        String fileUrl = prodotto.getImmagine();  

        // Estrai la chiave dal URL dell'immagine
        String key = fileUrl.substring(fileUrl.indexOf("amazonaws.com/") + 14);  // Estrai la chiave del file

        // Elimina il file da S3
        s3Service.deleteFile(key);

        // Rimuovi l'URL dell'immagine dal prodotto
        prodotto.setImmagine(null);

        // Aggiorna il prodotto nel database
        prodottiRep.updateProduct(productId, prodotto);
    }
}
