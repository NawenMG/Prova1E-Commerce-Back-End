package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Ordini;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.OrdiniRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdiniService {
    
    @Autowired
    private OrdiniRep ordiniRep;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Kafka producer

    private static final String KAFKA_TOPIC_ORDINI_AGGIUNGI = "ordini-topic-aggiungi"; 
    private static final String KAFKA_TOPIC_ORDINI_AGGIORNA = "ordini-topic-aggiorna"; 


    /**
     * Metodo per eseguire una query avanzata sugli ordini in base a parametri dinamici.
     * Utilizza Caffeine per 10 minuti e Redis per 30 minuti.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #ordini.toString()")
    public List<Ordini> queryOrdini(ParamQuery paramQuery, Ordini ordini) {
        return ordiniRep.query(paramQuery, ordini);
    }

    /**
     * Metodo per inserire un nuovo ordine.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Produce un messaggio Kafka per notificare l'inserimento di un nuovo ordine.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String inserisciOrdine(Ordini ordini) {
        String result = ordiniRep.insertOrdini(ordini);
        
        // Invia un messaggio Kafka che indica l'inserimento di un nuovo ordine
        kafkaTemplate.send(KAFKA_TOPIC_ORDINI_AGGIUNGI, "Nuovo ordine inserito: " + ordini.getOrderID());
        
        return result;
    }

    /**
     * Metodo per aggiornare un ordine esistente in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Produce un messaggio Kafka per notificare l'aggiornamento di un ordine.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String aggiornaOrdine(String orderID, Ordini ordini) {
        String result = ordiniRep.updateOrdini(orderID, ordini);
        
        // Invia un messaggio Kafka che indica l'aggiornamento dell'ordine
        kafkaTemplate.send(KAFKA_TOPIC_ORDINI_AGGIORNA, "Ordine aggiornato: " + orderID);
        
        return result;
    }

    /**
     * Metodo per eliminare un ordine in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Produce un messaggio Kafka per notificare l'eliminazione di un ordine.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String eliminaOrdine(String orderID) {
        String result = ordiniRep.deleteOrdini(orderID);
        
        return result;
    }

    /**
     * Metodo per generare un numero specificato di ordini con dati casuali.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Produce un messaggio Kafka per notificare il salvataggio di ordini casuali.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String salvaOrdiniCasuali(int numero) {
        String result = ordiniRep.saveAll(numero);
        
        return result;
    }
}
