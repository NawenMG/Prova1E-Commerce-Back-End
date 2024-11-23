package com.prova.e_commerce.dbRT.service;

import com.prova.e_commerce.dbRT.model.ShippingStatus;
import com.prova.e_commerce.dbRT.repository.interfacce.ShippingStatusRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ShippingStatusService {

    @Autowired
    private ShippingStatusRep shippingStatusRep;

    // ==============================
    // Operazioni sullo Stato della Spedizione
    // ==============================

    // Crea una nuova spedizione
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public CompletableFuture<Void> createShippingStatus(ShippingStatus shippingStatus) {
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.createShippingStatus(shippingStatus).get();
            } catch (Exception e) {
                throw new RuntimeException("Errore durante la creazione dello stato della spedizione", e);
            }
        });
    }

    // Modifica una spedizione esistente
    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatus.id")
    public CompletableFuture<Void> updateShippingStatus(ShippingStatus shippingStatus) {
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.updateShippingStatus(shippingStatus).get();
            } catch (Exception e) {
                throw new RuntimeException("Errore durante l'aggiornamento dello stato della spedizione", e);
            }
        });
    }

    // Recupera lo stato della spedizione per ID (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<ShippingStatus> getShippingStatusById(String shippingStatusId) {
        return shippingStatusRep.selectShippingStatusById(shippingStatusId);
    }

    // Elimina lo stato della spedizione
    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<Void> deleteShippingStatus(String shippingStatusId) {
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.deleteShippingStatus(shippingStatusId).get();
            } catch (Exception e) {
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
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.createLocationForShipping(shippingStatusId, location).get();
            } catch (Exception e) {
                throw new RuntimeException("Errore durante l'aggiunta della locazione alla spedizione", e);
            }
        });
    }

    // Recupera tutte le locazioni storiche di una spedizione (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<List<ShippingStatus.HistoricalLocation>> getLocationsForShipping(String shippingStatusId) {
        return shippingStatusRep.selectLocationsForShipping(shippingStatusId);
    }

    // Elimina una locazione storica da una spedizione
    @CacheEvict(value = {"caffeine", "redis"}, key = "#shippingStatusId")
    public CompletableFuture<Void> deleteLocationFromShipping(String shippingStatusId, String locationId) {
        return CompletableFuture.runAsync(() -> {
            try {
                shippingStatusRep.deleteLocationFromShipping(shippingStatusId, locationId).get();
            } catch (Exception e) {
                throw new RuntimeException("Errore durante l'eliminazione della locazione dalla spedizione", e);
            }
        });
    }
}
