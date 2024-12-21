package com.prova.e_commerce.dbKey.service;

import com.prova.e_commerce.dbKey.model.Carrello;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.CarrelloRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class CarrelloService {

    private static final Logger logger = LoggerFactory.getLogger(CarrelloService.class);

    @Autowired
    private CarrelloRep carrelloRep;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    private static final String TOPIC_EVENTI_CARRELLO_AGGIUNGI = "eventi-carrello-topic-aggiungi";

    public CarrelloService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.carrello.aggiungi.count");
        meterRegistry.counter("service.carrello.get.count");
        meterRegistry.counter("service.carrello.rimuovi.count");
        meterRegistry.counter("service.carrello.svuota.count");
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void aggiungiProdotti(String userId, List<Prodotto> nuoviProdotti) {
        logger.info("Aggiunta prodotti al carrello: userId={}, prodotti={}", userId, nuoviProdotti);
        Span span = tracer.spanBuilder("aggiungiProdotti").startSpan();
        try {
            if (nuoviProdotti == null || nuoviProdotti.isEmpty()) {
                throw new IllegalArgumentException("La lista dei prodotti non può essere vuota o null.");
            }
            meterRegistry.counter("service.carrello.aggiungi.count").increment();
            carrelloRep.aggiungiProdotti(userId, nuoviProdotti);
            kafkaTemplate.send(TOPIC_EVENTI_CARRELLO_AGGIUNGI, "ProdottiAggiunti", "Prodotti aggiunti al carrello dell'utente " + userId);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#userId", unless = "#result == null")
    public Optional<Carrello> getCarrello(String userId) {
        logger.info("Recupero carrello per l'utente: userId={}", userId);
        Span span = tracer.spanBuilder("getCarrello").startSpan();
        try {
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
            }
            meterRegistry.counter("service.carrello.get.count").increment();
            return carrelloRep.trovaCarrello(userId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void rimuoviProdotto(String userId, String prodottoId) {
        logger.info("Rimozione prodotto dal carrello: userId={}, prodottoId={}", userId, prodottoId);
        Span span = tracer.spanBuilder("rimuoviProdotto").startSpan();
        try {
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
            }
            if (prodottoId == null || prodottoId.isEmpty()) {
                throw new IllegalArgumentException("Il prodottoId non può essere null o vuoto.");
            }
            meterRegistry.counter("service.carrello.rimuovi.count").increment();
            carrelloRep.eliminaProdotto(userId, prodottoId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void svuotaCarrello(String userId) {
        logger.info("Svuotamento carrello per l'utente: userId={}", userId);
        Span span = tracer.spanBuilder("svuotaCarrello").startSpan();
        try {
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
            }
            meterRegistry.counter("service.carrello.svuota.count").increment();
            carrelloRep.resetCarrello(userId);
        } finally {
            span.end();
        }
    }
}
