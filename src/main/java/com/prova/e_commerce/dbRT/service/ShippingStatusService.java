package com.prova.e_commerce.dbRT.service;

import com.prova.e_commerce.dbRT.model.ShippingStatus;
import com.prova.e_commerce.dbRT.repository.interfacce.ShippingStatusRep;
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

    @Autowired
    private ShippingStatusRep shippingStatusRep;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Kafka producer

    private static final String KAFKA_TOPIC_SHIPPING_AGGIUNGI = "shipping-status-topic-aggiungi"; 
    private static final String KAFKA_TOPIC_SHIPPING_AGGIORNA = "shipping-status-topic-aggiorna";
    
    private static final Logger logger = LoggerFactory.getLogger(ShippingStatusService.class);

    // ==============================
    // Operazioni sullo Stato della Spedizione
    // ==============================

    // Crea una nuova spedizione
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public CompletableFuture<Void> createShippingStatus(ShippingStatus shippingStatus) {
        logger.info("Creazione stato di spedizione: {}", shippingStatus.getId());
        
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.createShippingStatus(shippingStatus).get();
                
                // Invia un messaggio Kafka per notificare la creazione della spedizione
                kafkaTemplate.send(KAFKA_TOPIC_SHIPPING_AGGIUNGI, "Nuovo stato di spedizione creato: " + shippingStatus.getId());
            } catch (Exception e) {
                logger.error("Errore durante la creazione dello stato della spedizione", e);
                throw new RuntimeException("Errore durante la creazione dello stato della spedizione", e);
            }
        });
    }

    // Modifica una spedizione esistente
    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatus.id")
    public CompletableFuture<Void> updateShippingStatus(ShippingStatus shippingStatus) {
        logger.info("Aggiornamento stato di spedizione: {}", shippingStatus.getId());
        
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.updateShippingStatus(shippingStatus).get();
                
                // Invia un messaggio Kafka per notificare l'aggiornamento dello stato della spedizione
                kafkaTemplate.send(KAFKA_TOPIC_SHIPPING_AGGIORNA, "Stato di spedizione aggiornato: " + shippingStatus.getId());
            } catch (Exception e) {
                logger.error("Errore durante l'aggiornamento dello stato della spedizione", e);
                throw new RuntimeException("Errore durante l'aggiornamento dello stato della spedizione", e);
            }
        });
    }

    // Recupera lo stato della spedizione per ID (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<ShippingStatus> getShippingStatusById(String shippingStatusId) {
        logger.info("Recupero stato di spedizione per ID: {}", shippingStatusId);
        return shippingStatusRep.selectShippingStatusById(shippingStatusId);
    }

    // Elimina lo stato della spedizione
    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<Void> deleteShippingStatus(String shippingStatusId) {
        logger.info("Eliminazione stato di spedizione per ID: {}", shippingStatusId);
        
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.deleteShippingStatus(shippingStatusId).get();
            } catch (Exception e) {
                logger.error("Errore durante l'eliminazione dello stato della spedizione", e);
                throw new RuntimeException("Errore durante l'eliminazione dello stato della spedizione", e);
            }
        });
    }

    // ==============================
    // Operazioni sulle Locazioni
    // ==============================

    // Aggiungi una nuova locazione per una spedizione
    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<Void> addLocationToShipping(String shippingStatusId, ShippingStatus.CurrentLocation location) {
        logger.info("Aggiunta locazione per spedizione: {} - Locazione: {}", shippingStatusId, location);
        
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.createLocationForShipping(shippingStatusId, location).get();
            } catch (Exception e) {
                logger.error("Errore durante l'aggiunta della locazione alla spedizione", e);
                throw new RuntimeException("Errore durante l'aggiunta della locazione alla spedizione", e);
            }
        });
    }

    // Recupera tutte le locazioni storiche di una spedizione (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<List<ShippingStatus.HistoricalLocation>> getLocationsForShipping(String shippingStatusId) {
        logger.info("Recupero locazioni storiche per spedizione: {}", shippingStatusId);
        return shippingStatusRep.selectLocationsForShipping(shippingStatusId);
    }

    // Elimina una locazione storica da una spedizione
    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<Void> deleteLocationFromShipping(String shippingStatusId, String locationId) {
        logger.info("Eliminazione locazione per spedizione: {} - Locazione ID: {}", shippingStatusId, locationId);
        
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.deleteLocationFromShipping(shippingStatusId, locationId).get();
            } catch (Exception e) {
                logger.error("Errore durante l'eliminazione della locazione dalla spedizione", e);
                throw new RuntimeException("Errore durante l'eliminazione della locazione dalla spedizione", e);
            }
        });
    }
}




///////////////////////////////////
/// DOPO L'IMPLEMENTAZIONE E L'ACCESSO DEI SERVIZI DI SPEDIZIONE  USA QUESTO CODICE
///////////////////////////////////
/*
 * package com.prova.e_commerce.dbRT.service;

import com.prova.e_commerce.dbRT.model.ShippingStatus;
import com.prova.e_commerce.dbRT.repository.interfacce.ShippingStatusRep;
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

    @Autowired
    private ShippingStatusRep shippingStatusRep;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Kafka producer

    // Aggiungere i servizi di spedizione (UPS, FedEx, DHL)
    @Autowired
    private UPSService upsService;
    @Autowired
    private FedExService fedExService;
    @Autowired
    private DHLService dhlService;

    private static final String KAFKA_TOPIC_SHIPPING_AGGIUNGI = "shipping-status-topic-aggiungi"; 
    private static final String KAFKA_TOPIC_SHIPPING_AGGIORNA = "shipping-status-topic-aggiorna";
    
    private static final Logger logger = LoggerFactory.getLogger(ShippingStatusService.class);

    // ==============================
    // Operazioni sullo Stato della Spedizione
    // ==============================

    // Crea una nuova spedizione
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public CompletableFuture<Void> createShippingStatus(ShippingStatus shippingStatus) {
        logger.info("Creazione stato di spedizione: {}", shippingStatus.getId());
        
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.createShippingStatus(shippingStatus).get();
                
                // Invia un messaggio Kafka per notificare la creazione della spedizione
                kafkaTemplate.send(KAFKA_TOPIC_SHIPPING_AGGIUNGI, "Nuovo stato di spedizione creato: " + shippingStatus.getId());
            } catch (Exception e) {
                logger.error("Errore durante la creazione dello stato della spedizione", e);
                throw new RuntimeException("Errore durante la creazione dello stato della spedizione", e);
            }
        });
    }

    // Metodo per creare un'etichetta di spedizione (utilizza il provider scelto)
    public CompletableFuture<String> createShippingLabel(ShippingStatus shippingStatus, String provider) {
        switch (provider.toUpperCase()) {
            case "UPS":
                return upsService.createShippingLabel(shippingStatus);
            case "FEDEX":
                return fedExService.createShippingLabel(shippingStatus);
            case "DHL":
                return dhlService.createShippingLabel(shippingStatus);
            default:
                throw new IllegalArgumentException("Provider di spedizione non supportato: " + provider);
        }
    }

    // Metodo per tracciare una spedizione (utilizza il provider scelto)
    public CompletableFuture<String> trackShipment(String trackingNumber, String provider) {
        switch (provider.toUpperCase()) {
            case "UPS":
                return upsService.trackShipment(trackingNumber);
            case "FEDEX":
                return fedExService.trackShipment(trackingNumber);
            case "DHL":
                return dhlService.trackShipment(trackingNumber);
            default:
                throw new IllegalArgumentException("Provider di spedizione non supportato: " + provider);
        }
    }

    // Modifica una spedizione esistente
    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatus.id")
    public CompletableFuture<Void> updateShippingStatus(ShippingStatus shippingStatus) {
        logger.info("Aggiornamento stato di spedizione: {}", shippingStatus.getId());
        
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.updateShippingStatus(shippingStatus).get();
                
                // Invia un messaggio Kafka per notificare l'aggiornamento dello stato della spedizione
                kafkaTemplate.send(KAFKA_TOPIC_SHIPPING_AGGIORNA, "Stato di spedizione aggiornato: " + shippingStatus.getId());
            } catch (Exception e) {
                logger.error("Errore durante l'aggiornamento dello stato della spedizione", e);
                throw new RuntimeException("Errore durante l'aggiornamento dello stato della spedizione", e);
            }
        });
    }

    // Recupera lo stato della spedizione per ID (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<ShippingStatus> getShippingStatusById(String shippingStatusId) {
        logger.info("Recupero stato di spedizione per ID: {}", shippingStatusId);
        return shippingStatusRep.selectShippingStatusById(shippingStatusId);
    }

    // Elimina lo stato della spedizione
    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<Void> deleteShippingStatus(String shippingStatusId) {
        logger.info("Eliminazione stato di spedizione per ID: {}", shippingStatusId);
        
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.deleteShippingStatus(shippingStatusId).get();
            } catch (Exception e) {
                logger.error("Errore durante l'eliminazione dello stato della spedizione", e);
                throw new RuntimeException("Errore durante l'eliminazione dello stato della spedizione", e);
            }
        });
    }

    // ==============================
    // Operazioni sulle Locazioni
    // ==============================

    // Aggiungi una nuova locazione per una spedizione
    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<Void> addLocationToShipping(String shippingStatusId, ShippingStatus.CurrentLocation location) {
        logger.info("Aggiunta locazione per spedizione: {} - Locazione: {}", shippingStatusId, location);
        
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.createLocationForShipping(shippingStatusId, location).get();
            } catch (Exception e) {
                logger.error("Errore durante l'aggiunta della locazione alla spedizione", e);
                throw new RuntimeException("Errore durante l'aggiunta della locazione alla spedizione", e);
            }
        });
    }

    // Recupera tutte le locazioni storiche di una spedizione (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<List<ShippingStatus.HistoricalLocation>> getLocationsForShipping(String shippingStatusId) {
        logger.info("Recupero locazioni storiche per spedizione: {}", shippingStatusId);
        return shippingStatusRep.selectLocationsForShipping(shippingStatusId);
    }

    // Elimina una locazione storica da una spedizione
    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<Void> deleteLocationFromShipping(String shippingStatusId, String locationId) {
        logger.info("Eliminazione locazione per spedizione: {} - Locazione ID: {}", shippingStatusId, locationId);
        
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.deleteLocationFromShipping(shippingStatusId, locationId).get();
            } catch (Exception e) {
                logger.error("Errore durante l'eliminazione della locazione dalla spedizione", e);
                throw new RuntimeException("Errore durante l'eliminazione della locazione dalla spedizione", e);
            }
        });
    }
}

 */