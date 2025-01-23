package com.prova.e_commerce.dbKey.service;

import com.prova.e_commerce.dbKey.model.Cronologia;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.CronologiaRep;
import com.prova.e_commerce.security.security1.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class CronologiaService {

    private static final Logger logger = LoggerFactory.getLogger(CronologiaService.class);

    @Autowired
    private CronologiaRep cronologiaRep;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    private static final String TOPIC_EVENTI_CRONOLOGIA_AGGIUNGI = "eventi-cronologia-topic-aggiungi";

    public CronologiaService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.cronologia.aggiungi.count");
        meterRegistry.counter("service.cronologia.visualizza.count");
        meterRegistry.counter("service.cronologia.elimina.singola.count");
        meterRegistry.counter("service.cronologia.elimina.tutte.count");
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void aggiungiDatiCronologici(String userId, List<Prodotto> nuoviProdotti) {
        userId = SecurityUtils.getCurrentUsername();
        logger.info("Aggiunta prodotti alla cronologia: userId={}, prodotti={}", userId, nuoviProdotti);
        Span span = tracer.spanBuilder("aggiungiDatiCronologici").startSpan();
        try {
            if (nuoviProdotti == null || nuoviProdotti.isEmpty()) {
                throw new IllegalArgumentException("La lista dei prodotti non può essere vuota o null.");
            }
            meterRegistry.counter("service.cronologia.aggiungi.count").increment();
            cronologiaRep.aggiungiDatiCronologici(userId, nuoviProdotti);
            kafkaTemplate.send(TOPIC_EVENTI_CRONOLOGIA_AGGIUNGI, "ProdottiAggiuntiCronologia", 
                               "Prodotti aggiunti alla cronologia dell'utente " + userId);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#userId", unless = "#result == null")
    public Optional<Cronologia> visualizzaCronologia(String userId) {
        userId = SecurityUtils.getCurrentUsername();
        logger.info("Visualizzazione cronologia per l'utente: userId={}", userId);
        Span span = tracer.spanBuilder("visualizzaCronologia").startSpan();
        try {
            meterRegistry.counter("service.cronologia.visualizza.count").increment();
            return cronologiaRep.visualizzaCronologia(userId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void eliminaSingolaRicerca(String userId, String productId) {
        userId = SecurityUtils.getCurrentUsername();
        logger.info("Eliminazione singola ricerca dalla cronologia: userId={}, productId={}", userId, productId);
        Span span = tracer.spanBuilder("eliminaSingolaRicerca").startSpan();
        try {
            if (productId == null || productId.isEmpty()) {
                throw new IllegalArgumentException("Il productId non può essere null o vuoto.");
            }
            meterRegistry.counter("service.cronologia.elimina.singola.count").increment();
            cronologiaRep.eliminaSingolaRicerca(userId, productId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void eliminaTutteLeRicerche(String userId) {
        userId = SecurityUtils.getCurrentUsername();
        logger.info("Eliminazione di tutte le ricerche dalla cronologia: userId={}", userId);
        Span span = tracer.spanBuilder("eliminaTutteLeRicerche").startSpan();
        try {
            meterRegistry.counter("service.cronologia.elimina.tutte.count").increment();
            cronologiaRep.eliminaTutteLeRicerche(userId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    private void evictCronologiaCache(String userId) {
        logger.info("Invalidazione della cache per l'utente: userId={}", userId);
    }
}
