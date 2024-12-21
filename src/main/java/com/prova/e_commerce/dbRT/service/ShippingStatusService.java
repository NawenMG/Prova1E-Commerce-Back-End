package com.prova.e_commerce.dbRT.service;

import com.prova.e_commerce.dbRT.model.ShippingStatus;
import com.prova.e_commerce.dbRT.repository.interfacce.ShippingStatusRep;

import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ShippingStatusService {

    private static final Logger logger = LoggerFactory.getLogger(ShippingStatusService.class);

    @Autowired
    private ShippingStatusRep shippingStatusRep;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    private static final String KAFKA_TOPIC_SHIPPING_AGGIUNGI = "shipping-status-topic-aggiungi";
    private static final String KAFKA_TOPIC_SHIPPING_AGGIORNA = "shipping-status-topic-aggiorna";

    public ShippingStatusService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.shipping.create.count");
        meterRegistry.counter("service.shipping.update.count");
        meterRegistry.counter("service.shipping.delete.count");
        meterRegistry.counter("service.shipping.locations.count");
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public CompletableFuture<Void> createShippingStatus(ShippingStatus shippingStatus) {
        logger.info("Creazione stato di spedizione: {}", shippingStatus.getId());
        Span span = tracer.spanBuilder("createShippingStatus").startSpan();
        try {
            meterRegistry.counter("service.shipping.create.count").increment();
            return CompletableFuture.runAsync(() -> {
                try {
                    shippingStatusRep.createShippingStatus(shippingStatus).get();
                    kafkaTemplate.send(KAFKA_TOPIC_SHIPPING_AGGIUNGI, "Nuovo stato di spedizione creato: " + shippingStatus.getId());
                } catch (Exception e) {
                    logger.error("Errore durante la creazione dello stato della spedizione", e);
                    throw new RuntimeException("Errore durante la creazione dello stato della spedizione", e);
                }
            });
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatus.id")
    public CompletableFuture<Void> updateShippingStatus(ShippingStatus shippingStatus) {
        logger.info("Aggiornamento stato di spedizione: {}", shippingStatus.getId());
        Span span = tracer.spanBuilder("updateShippingStatus").startSpan();
        try {
            meterRegistry.counter("service.shipping.update.count").increment();
            return CompletableFuture.runAsync(() -> {
                try {
                    shippingStatusRep.updateShippingStatus(shippingStatus).get();
                    kafkaTemplate.send(KAFKA_TOPIC_SHIPPING_AGGIORNA, "Stato di spedizione aggiornato: " + shippingStatus.getId());
                } catch (Exception e) {
                    logger.error("Errore durante l'aggiornamento dello stato della spedizione", e);
                    throw new RuntimeException("Errore durante l'aggiornamento dello stato della spedizione", e);
                }
            });
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<ShippingStatus> getShippingStatusById(String shippingStatusId) {
        logger.info("Recupero stato di spedizione per ID: {}", shippingStatusId);
        Span span = tracer.spanBuilder("getShippingStatusById").startSpan();
        try {
            meterRegistry.counter("service.shipping.get.count").increment();
            return shippingStatusRep.selectShippingStatusById(shippingStatusId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<Void> deleteShippingStatus(String shippingStatusId) {
        logger.info("Eliminazione stato di spedizione per ID: {}", shippingStatusId);
        Span span = tracer.spanBuilder("deleteShippingStatus").startSpan();
        try {
            meterRegistry.counter("service.shipping.delete.count").increment();
            return CompletableFuture.runAsync(() -> {
                try {
                    shippingStatusRep.deleteShippingStatus(shippingStatusId).get();
                } catch (Exception e) {
                    logger.error("Errore durante l'eliminazione dello stato della spedizione", e);
                    throw new RuntimeException("Errore durante l'eliminazione dello stato della spedizione", e);
                }
            });
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<Void> addLocationToShipping(String shippingStatusId, ShippingStatus.CurrentLocation location) {
        logger.info("Aggiunta locazione per spedizione: {} - Locazione: {}", shippingStatusId, location);
        Span span = tracer.spanBuilder("addLocationToShipping").startSpan();
        try {
            meterRegistry.counter("service.shipping.locations.count").increment();
            return CompletableFuture.runAsync(() -> {
                try {
                    shippingStatusRep.createLocationForShipping(shippingStatusId, location).get();
                } catch (Exception e) {
                    logger.error("Errore durante l'aggiunta della locazione alla spedizione", e);
                    throw new RuntimeException("Errore durante l'aggiunta della locazione alla spedizione", e);
                }
            });
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<List<ShippingStatus.HistoricalLocation>> getLocationsForShipping(String shippingStatusId) {
        logger.info("Recupero locazioni storiche per spedizione: {}", shippingStatusId);
        Span span = tracer.spanBuilder("getLocationsForShipping").startSpan();
        try {
            return shippingStatusRep.selectLocationsForShipping(shippingStatusId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<Void> deleteLocationFromShipping(String shippingStatusId, String locationId) {
        logger.info("Eliminazione locazione per spedizione: {} - Locazione ID: {}", shippingStatusId, locationId);
        Span span = tracer.spanBuilder("deleteLocationFromShipping").startSpan();
        try {
            return CompletableFuture.runAsync(() -> {
                try {
                    shippingStatusRep.deleteLocationFromShipping(shippingStatusId, locationId).get();
                } catch (Exception e) {
                    logger.error("Errore durante l'eliminazione della locazione dalla spedizione", e);
                    throw new RuntimeException("Errore durante l'eliminazione della locazione dalla spedizione", e);
                }
            });
        } finally {
            span.end();
        }
    }
}




