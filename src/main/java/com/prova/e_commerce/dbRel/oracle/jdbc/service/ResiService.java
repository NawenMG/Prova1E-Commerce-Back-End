package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Resi;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.ResiRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResiService {

    @Autowired
    private ResiRep resiRep;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Kafka producer

    private static final String KAFKA_TOPIC_RESI_AGGIUNGI = "resi-topic-aggiungi"; 
    private static final String KAFKA_TOPIC_RESI_AGGIORNA = "resi-topic-aggiorna";

    /**
     * Metodo per eseguire una query avanzata sui resi in base a parametri dinamici.
     * Usa Caffeine per 10 minuti e Redis per 30 minuti.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #resi.toString()")
    public List<Resi> queryResi(ParamQuery paramQuery, Resi resi) {
        return resiRep.query(paramQuery, resi);
    }

    /**
     * Metodo per inserire un nuovo reso.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Produce un messaggio Kafka per notificare l'inserimento di un nuovo reso.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String inserisciReso(Resi resi) {
        String result = resiRep.insertReturn(resi);
        
        // Invia un messaggio Kafka che indica l'inserimento di un nuovo reso
        kafkaTemplate.send(KAFKA_TOPIC_RESI_AGGIUNGI, "Nuovo reso inserito: " + resi.getReturnsID());
        
        return result;
    }

    /**
     * Metodo per aggiornare i dati di un reso specifico in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Produce un messaggio Kafka per notificare l'aggiornamento di un reso.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String aggiornaReso(String returnID, Resi resi) {
        String result = resiRep.updateReturn(returnID, resi);
        
        // Invia un messaggio Kafka che indica l'aggiornamento del reso
        kafkaTemplate.send(KAFKA_TOPIC_RESI_AGGIORNA, "Reso aggiornato: " + returnID);
        
        return result;
    }

    /**
     * Metodo per eliminare un reso in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Produce un messaggio Kafka per notificare l'eliminazione di un reso.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String eliminaReso(String returnID) {
        String result = resiRep.deleteReturn(returnID);        
        return result;
    }

    /**
     * Metodo per generare un numero specificato di resi con dati casuali e salvarli nel database.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Produce un messaggio Kafka per notificare il salvataggio di resi casuali.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String salvaResiCasuali(int numero) {
        String result = resiRep.saveAll(numero);        
        return result;
    }
}
