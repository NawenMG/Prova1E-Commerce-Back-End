package com.prova.e_commerce.dbDoc.service;

import com.prova.e_commerce.dbDoc.entity.SchedeProdotti;
import com.prova.e_commerce.dbDoc.parametri.ParamQueryDbDoc;
import com.prova.e_commerce.dbDoc.randomData.SchedeProdottiFaker;
import com.prova.e_commerce.dbDoc.repository.interfacce.SchedeProdottiRep;
import com.prova.e_commerce.dbDoc.repository.interfacce.SchedeProdottiRepCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SchedeProdottiService {

    @Autowired
    private SchedeProdottiRep schedeProdottiRep;

    @Autowired
    private SchedeProdottiFaker prodottiFaker;

    @Autowired
    private SchedeProdottiRepCustom schedeProdottiRepCustom;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_SCHEDA_PRODOTTI_SAVE = "scheda-prodotti-topic-save"; 
    private static final String TOPIC_SCHEDA_PRODOTTI_UPDATE = "scheda-prodotti-topic-update";

    /**
     * Cache per cercare prodotti per nome (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#nome", unless = "#result == null")
    public List<SchedeProdotti> findByNome(String nome) {
        return schedeProdottiRep.findByNome(nome);
    }

    /**
     * Cache per cercare prodotti con prezzo inferiore a una certa soglia (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#prezzo", unless = "#result == null")
    public List<SchedeProdotti> findByPrezzoLessThan(BigDecimal prezzo) {
        return schedeProdottiRep.findByPrezzoLessThan(prezzo);
    }

    /**
     * Cache per cercare prodotti con criteri dinamici (10 minuti in Caffeine, 30 minuti in Redis).
     * La cache è memorizzata con la chiave unica della query.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQueryDbDoc.query", unless = "#result == null")
    public List<SchedeProdotti> queryDynamic(ParamQueryDbDoc paramQueryDbDoc, SchedeProdotti schedeProdotti) {
        return schedeProdottiRepCustom.query(paramQueryDbDoc, schedeProdotti);
    }

    /**
     * Metodo per inserire un nuovo prodotto e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public SchedeProdotti insert(SchedeProdotti prodotto) {
     SchedeProdotti savedProdotto = schedeProdottiRep.save(prodotto);
     // Invia evento Kafka
     kafkaTemplate.send(TOPIC_SCHEDA_PRODOTTI_SAVE, "ProdottoCreato", savedProdotto);
     return savedProdotto;
    }


    /**
     * Metodo per generare una lista fittizia di schede prodotti.
     */
    public List<SchedeProdotti> generateRandomSchedeProdotti(int count) {
        List<SchedeProdotti> schedeProdottiList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            schedeProdottiList.add(prodottiFaker.generateFakeProduct());
        }
        return schedeProdottiList;
    }

    /**
     * Metodo per aggiornare un prodotto esistente e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    @Transactional
    public SchedeProdotti update(String id, SchedeProdotti prodotto) {
     Optional<SchedeProdotti> existingProdotto = schedeProdottiRep.findById(id);
     if (existingProdotto.isPresent()) {
        SchedeProdotti updatedProdotto = existingProdotto.get();
        updatedProdotto.setNome(prodotto.getNome());
        updatedProdotto.setPrezzo(prodotto.getPrezzo());
        updatedProdotto.setParametriDescrittivi(prodotto.getParametriDescrittivi());
        SchedeProdotti savedProdotto = schedeProdottiRep.save(updatedProdotto);
        // Invia evento Kafka
        kafkaTemplate.send(TOPIC_SCHEDA_PRODOTTI_UPDATE, "ProdottoAggiornato", savedProdotto);
        return savedProdotto;
     } else {
        throw new RuntimeException("Prodotto con ID " + id + " non trovato.");
     }
    }


    /**
     * Metodo per eliminare un prodotto per ID e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void deleteById(String id) {
        if (schedeProdottiRep.existsById(id)) {
            schedeProdottiRep.deleteById(id);
        } else {
            throw new RuntimeException("Prodotto con ID " + id + " non trovato.");
        }
    }
}
