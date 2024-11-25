package com.prova.e_commerce.dbDoc.service;

import com.prova.e_commerce.dbDoc.entity.Recensioni;
import com.prova.e_commerce.dbDoc.parametri.ParamQueryDbDoc;
import com.prova.e_commerce.dbDoc.randomData.RecensioniFaker;
import com.prova.e_commerce.dbDoc.repository.interfacce.RecensioniRep;
import com.prova.e_commerce.dbDoc.repository.interfacce.RecensioniRepCustom;
import com.prova.e_commerce.storage.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecensioniService {

    @Autowired
    private RecensioniRep recensioniRep;

    @Autowired
    private RecensioniRepCustom recensioniRepCustom;

    @Autowired
    private RecensioniFaker recensioniFaker;

    @Autowired
    private S3Service s3Service;  // Iniezione del servizio S3

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_RECENSIONI_SAVE = "recensioni-topic-save";  
    private static final String TOPIC_RECENSIONI_UPDATE = "recensioni-topic-update";


    /**
     * Cache per eseguire una query dinamica sulle recensioni (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQueryDbDoc.toString() + #recensioni.toString()")
    public List<Recensioni> queryDynamic(ParamQueryDbDoc paramQueryDbDoc, Recensioni recensioni) {
        return recensioniRepCustom.query(paramQueryDbDoc, recensioni);
    }

    /**
     * Cache per trovare recensioni per un determinato productId (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#productId")
    public List<Recensioni> findByProductId(String productId) {
        return recensioniRep.findByProductId(productId);
    }

    /**
     * Cache per trovare recensioni per un determinato userId (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#userId")
    public List<Recensioni> findByUserId(String userId) {
        return recensioniRep.findByUserId(userId);
    }

    /**
     * Cache per trovare tutte le recensioni (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "'findAll'")
    public List<Recensioni> findAllRecensioni() {
        return recensioniRep.findAll();
    }

    /**
     * Metodo per salvare una nuova recensione e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public Recensioni saveRecensione(Recensioni recensione) {
     Recensioni savedRecensione = recensioniRep.save(recensione);
     // Invia evento Kafka
     kafkaTemplate.send(TOPIC_RECENSIONI_SAVE, "RecensioneCreata", savedRecensione);
     return savedRecensione;
    }


    /**
     * Metodo per aggiornare una recensione esistente e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public Recensioni updateRecensione(String id, Recensioni recensione) {
     if (recensioniRep.existsById(id)) {
        recensione.setId(id); // Impostiamo l'ID per l'update
        Recensioni updatedRecensione = recensioniRep.save(recensione);
        // Invia evento Kafka
        kafkaTemplate.send(TOPIC_RECENSIONI_UPDATE, "RecensioneAggiornata", updatedRecensione);
        return updatedRecensione;
     } else {
        return null; // Puoi gestire il caso di recensione non trovata
     }
    }


    /**
     * Metodo per eliminare una recensione e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public boolean deleteRecensione(String id) {
        if (recensioniRep.existsById(id)) {
            recensioniRep.deleteById(id);
            return true;
        }
        return false; // Se la recensione non esiste
    }

    /**
     * Metodo per generare recensioni fittizie (ad esempio per test o popolamento).
     */
    public List<Recensioni> generateRandomRecensioni(int count) {
        List<Recensioni> recensioniList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            recensioniList.add(recensioniFaker.generateFakeReview());
        }
        return recensioniList;
    }

    /**
     * Metodo per caricare un file multimediale su S3 e associarlo a una recensione.
     */
    public String caricaFileRecensione(String recensioneId, MultipartFile file, String tipoFile) throws IOException {
        // Carica il file su S3 e ottieni l'URL
        String fileUrl = s3Service.uploadFile("recensioni/" + recensioneId, file);
        
        // Recupera la recensione dal database
        Recensioni recensione = recensioniRep.findById(recensioneId).orElseThrow(() -> new RuntimeException("Recensione non trovata"));

        // Assegna l'URL del file in base al tipo di file (immagine o video)
        if ("immagine".equalsIgnoreCase(tipoFile)) {
            recensione.setImmagine(fileUrl);  // Imposta l'URL dell'immagine
        } else if ("video".equalsIgnoreCase(tipoFile)) {
            recensione.setVideo(fileUrl);  // Imposta l'URL del video
        } else {
            throw new IllegalArgumentException("Tipo di file non valido");
        }

        // Aggiorna la recensione nel database con il nuovo URL del file
        recensioniRep.save(recensione);
        
        return fileUrl; // Ritorna l'URL del file caricato
    }

    /**
     * Metodo per scaricare il file multimediale associato a una recensione da S3.
     */
    public InputStream scaricaFileRecensione(String recensioneId, String tipoFile) throws IOException {
        // Recupera la recensione dal database
        Recensioni recensione = recensioniRep.findById(recensioneId).orElseThrow(() -> new RuntimeException("Recensione non trovata"));

        // Ottieni l'URL del file in base al tipo
        String fileUrl = "immagine".equalsIgnoreCase(tipoFile) ? recensione.getImmagine() : recensione.getVideo();
        
        // Verifica se l'URL del file è valido
        if (fileUrl == null) {
            throw new RuntimeException("File non trovato per il tipo specificato");
        }
        
        // Estrai la chiave dal URL del file
        String key = fileUrl.substring(fileUrl.indexOf("amazonaws.com/") + 14);  // Estrai la chiave del file

        return s3Service.downloadFile(key); // Usa S3Service per scaricare il file
    }

    /**
     * Metodo per eliminare il file multimediale associato a una recensione da S3.
     */
    public void eliminaFileRecensione(String recensioneId, String tipoFile) {
        // Recupera la recensione dal database
        Recensioni recensione = recensioniRep.findById(recensioneId).orElseThrow(() -> new RuntimeException("Recensione non trovata"));

        // Ottieni l'URL del file in base al tipo
        String fileUrl = "immagine".equalsIgnoreCase(tipoFile) ? recensione.getImmagine() : recensione.getVideo();

        // Verifica se l'URL del file è valido
        if (fileUrl == null) {
            throw new RuntimeException("File non trovato per il tipo specificato");
        }

        // Estrai la chiave dal URL del file
        String key = fileUrl.substring(fileUrl.indexOf("amazonaws.com/") + 14);  // Estrai la chiave del file

        // Elimina il file da S3
        s3Service.deleteFile(key);

        // Rimuovi l'URL del file dalla recensione
        if ("immagine".equalsIgnoreCase(tipoFile)) {
            recensione.setImmagine(null);
        } else if ("video".equalsIgnoreCase(tipoFile)) {
            recensione.setVideo(null);
        }

        // Aggiorna la recensione nel database
        recensioniRep.save(recensione);
    }
}
