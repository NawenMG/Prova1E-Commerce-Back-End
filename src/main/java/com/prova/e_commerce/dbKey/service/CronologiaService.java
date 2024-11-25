package com.prova.e_commerce.dbKey.service;

import com.prova.e_commerce.dbKey.model.Cronologia;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.CronologiaRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CronologiaService {

    @Autowired
    private CronologiaRep cronologiaRep;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_EVENTI_CRONOLOGIA_AGGIUNGI = "eventi-cronologia-topic-aggiungi";  // Nome del topic Kafka

    /**
     * Metodo per aggiungere nuovi prodotti alla cronologia dell'utente e invalidare la cache.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void aggiungiDatiCronologici(String userId, List<Prodotto> nuoviProdotti) {
        if (nuoviProdotti == null || nuoviProdotti.isEmpty()) {
            throw new IllegalArgumentException("La lista dei prodotti non può essere vuota o null.");
        }
        cronologiaRep.aggiungiDatiCronologici(userId, nuoviProdotti);

        // Invia evento Kafka per l'aggiunta di nuovi prodotti alla cronologia
        kafkaTemplate.send(TOPIC_EVENTI_CRONOLOGIA_AGGIUNGI, "ProdottiAggiuntiCronologia", 
                           "Prodotti aggiunti alla cronologia dell'utente " + userId);
    }

    /**
     * Metodo per visualizzare la cronologia dell'utente specificato. La cache è attiva per 10 minuti in Caffeine
     * e 30 minuti in Redis.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#userId", unless = "#result == null")
    public Optional<Cronologia> visualizzaCronologia(String userId) {
        return cronologiaRep.visualizzaCronologia(userId);
    }

    /**
     * Metodo per eliminare un singolo prodotto dalla cronologia dell'utente e invalidare la cache.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void eliminaSingolaRicerca(String userId, String productId) {
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("Il productId non può essere null o vuoto.");
        }
        cronologiaRep.eliminaSingolaRicerca(userId, productId);
    }

    /**
     * Metodo per eliminare tutte le ricerche dalla cronologia dell'utente e invalidare la cache.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void eliminaTutteLeRicerche(String userId) {
        cronologiaRep.eliminaTutteLeRicerche(userId);
    }

    /**
     * Metodo per invalidare la cache quando vengono apportate modifiche alla cronologia dell'utente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    private void evictCronologiaCache(String userId) {
    }
}
