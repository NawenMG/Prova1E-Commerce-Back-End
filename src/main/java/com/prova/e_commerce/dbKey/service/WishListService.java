package com.prova.e_commerce.dbKey.service;

import com.prova.e_commerce.dbKey.model.WishList;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.WishListRep;
import com.prova.e_commerce.security.security1.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.Cacheable;
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
public class WishListService {

    private static final Logger logger = LoggerFactory.getLogger(WishListService.class);

    @Autowired
    private WishListRep wishListRep;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    private static final String TOPIC_EVENTI_WISHLIST_AGGIUNGI = "eventi-wishlist-topic-aggiungi";

    public WishListService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.wishlist.aggiungi.count");
        meterRegistry.counter("service.wishlist.trova.count");
        meterRegistry.counter("service.wishlist.rimuovi.count");
        meterRegistry.counter("service.wishlist.reset.count");
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void aggiungiProdotti(String userId, List<Prodotto> nuoviProdotti) {
        userId = SecurityUtils.getCurrentUsername();
        logger.info("Aggiunta prodotti alla wishlist: userId={}, prodotti={}", userId, nuoviProdotti);
        Span span = tracer.spanBuilder("aggiungiProdotti").startSpan();
        try {
            if (nuoviProdotti == null || nuoviProdotti.isEmpty()) {
                throw new IllegalArgumentException("La lista dei prodotti non può essere vuota o null.");
            }
            meterRegistry.counter("service.wishlist.aggiungi.count").increment();
            wishListRep.aggiungiProdotti(userId, nuoviProdotti);
            kafkaTemplate.send(TOPIC_EVENTI_WISHLIST_AGGIUNGI, "ProdottiAggiuntiWishlist", 
                               "Prodotti aggiunti alla wishlist dell'utente " + userId);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#userId", unless = "#result == null")
    public Optional<WishList> trovaWishList(String userId) {
        userId = SecurityUtils.getCurrentUsername();
        logger.info("Recupero wishlist per l'utente: userId={}", userId);
        Span span = tracer.spanBuilder("trovaWishList").startSpan();
        try {
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
            }
            meterRegistry.counter("service.wishlist.trova.count").increment();
            return wishListRep.trovaWishList(userId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void rimuoviProdotto(String userId, String prodottoId) {
        userId = SecurityUtils.getCurrentUsername();
        logger.info("Rimozione prodotto dalla wishlist: userId={}, prodottoId={}", userId, prodottoId);
        Span span = tracer.spanBuilder("rimuoviProdotto").startSpan();
        try {
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
            }
            if (prodottoId == null || prodottoId.isEmpty()) {
                throw new IllegalArgumentException("Il prodottoId non può essere null o vuoto.");
            }
            meterRegistry.counter("service.wishlist.rimuovi.count").increment();
            wishListRep.rimuoviProdotto(userId, prodottoId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void resetWishList(String userId) {
        userId = SecurityUtils.getCurrentUsername();
        logger.info("Reset della wishlist per l'utente: userId={}", userId);
        Span span = tracer.spanBuilder("resetWishList").startSpan();
        try {
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
            }
            meterRegistry.counter("service.wishlist.reset.count").increment();
            wishListRep.resetWishList(userId);
        } finally {
            span.end();
        }
    }
}
