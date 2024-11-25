package com.prova.e_commerce.dbCol.service;

import com.prova.e_commerce.dbCol.model.ArchiviazioneSegnalazioni;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneSegnalazioniRep;
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
public class ArchiviazioneSegnalazioniService {

    @Autowired
    private ArchiviazioneSegnalazioniRep archiviazioneSegnalazioniRep;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_SEGNALAZIONI_SAVE = "segnalazioni-topic-save";
    private static final String TOPIC_SEGNALAZIONI_UPDATE = "segnalazioni-topic-update";


    /**
     * Cache per eseguire una query dinamica sulle segnalazioni (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #segnalazione.toString()")
    public List<ArchiviazioneSegnalazioni> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneSegnalazioni segnalazione) {
        return archiviazioneSegnalazioniRep.queryDinamica(paramQuery, segnalazione);
    }

    /**
     * Cache per trovare una segnalazione tramite ID (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#id")
    public ArchiviazioneSegnalazioni findSegnalazioneById(String id) {
        return archiviazioneSegnalazioniRep.findSegnalazioneById(id);
    }

    /**
     * Cache per trovare tutte le segnalazioni (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "'findAllSegnalazioni'")
    public List<ArchiviazioneSegnalazioni> findAllSegnalazioni() {
        return archiviazioneSegnalazioniRep.findAllSegnalazioni();
    }

    /**
     * Metodo per salvare una nuova segnalazione e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void saveSegnalazione(ArchiviazioneSegnalazioni segnalazione) {
     archiviazioneSegnalazioniRep.saveSegnalazione(segnalazione);
     // Pubblica evento Kafka
     kafkaTemplate.send(TOPIC_SEGNALAZIONI_SAVE, "SegnalazioneCreata", segnalazione);
    }


    /**
     * Metodo per aggiornare una segnalazione esistente in base all'ID e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void updateSegnalazione(String id, ArchiviazioneSegnalazioni segnalazione) {
     archiviazioneSegnalazioniRep.updateSegnalazione(id, segnalazione);
     // Pubblica evento Kafka
     kafkaTemplate.send(TOPIC_SEGNALAZIONI_UPDATE, "SegnalazioneAggiornata", segnalazione);
    }


    /**
     * Metodo per eliminare una segnalazione in base all'ID e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void deleteSegnalazione(String id) {
     archiviazioneSegnalazioniRep.deleteSegnalazione(id);
    }


    /**
     * Metodo per caricare un file multimediale su S3 e associarlo a una segnalazione.
     */
    public String caricaFileSegnalazione(String segnalazioneId, MultipartFile file) throws IOException {
        // Carica il file su S3 e ottieni l'URL
        String fileUrl = s3Service.uploadFile("segnalazioni/" + segnalazioneId, file);
        
        // Recupera la segnalazione dal database
        ArchiviazioneSegnalazioni segnalazione = archiviazioneSegnalazioniRep.findSegnalazioneById(segnalazioneId);
        if (segnalazione == null) {
            throw new RuntimeException("Segnalazione non trovata");
        }

        // Imposta l'URL del file nel campo 'fileMultimediali' della segnalazione
        segnalazione.setFileMultimediali(fileUrl);  
        
        // Aggiorna la segnalazione nel database con il nuovo URL del file
        archiviazioneSegnalazioniRep.updateSegnalazione(segnalazioneId, segnalazione);
        
        return fileUrl; // Ritorna l'URL del file caricato
    }

    /**
     * Metodo per scaricare il file multimediale associato a una segnalazione da S3.
     */
    public InputStream scaricaFileSegnalazione(String segnalazioneId) throws IOException {
        // Recupera la segnalazione dal database
        ArchiviazioneSegnalazioni segnalazione = archiviazioneSegnalazioniRep.findSegnalazioneById(segnalazioneId);
        if (segnalazione == null || segnalazione.getFileMultimediali() == null) {
            throw new RuntimeException("File associato alla segnalazione non trovato");
        }

        // Ottieni l'URL del file
        String fileUrl = segnalazione.getFileMultimediali();  
        
        // Estrai la chiave dal URL dell'immagine (assumiamo che contenga la chiave completa)
        String key = fileUrl.substring(fileUrl.indexOf("amazonaws.com/") + 14);  // 14 è la lunghezza di "amazonaws.com/"

        return s3Service.downloadFile(key); // Usa S3Service per scaricare il file
    }

    /**
     * Metodo per eliminare il file multimediale associato a una segnalazione da S3 e invalidare la cache.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#segnalazioneId")
    public void eliminaFileSegnalazione(String segnalazioneId) {
        // Recupera la segnalazione dal database
        ArchiviazioneSegnalazioni segnalazione = archiviazioneSegnalazioniRep.findSegnalazioneById(segnalazioneId);
        if (segnalazione == null || segnalazione.getFileMultimediali() == null) {
            throw new RuntimeException("File associato alla segnalazione non trovato");
        }

        // Ottieni l'URL del file
        String fileUrl = segnalazione.getFileMultimediali();  

        // Estrai la chiave dal URL del file
        String key = fileUrl.substring(fileUrl.indexOf("amazonaws.com/") + 14);  // Estrai la chiave del file

        // Elimina il file da S3
        s3Service.deleteFile(key);

        // Rimuovi l'URL del file dalla segnalazione
        segnalazione.setFileMultimediali(null);
        
        // Aggiorna la segnalazione nel database
        archiviazioneSegnalazioniRep.updateSegnalazione(segnalazioneId, segnalazione);
    }
}
