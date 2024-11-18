package com.prova.e_commerce.dbRT.repository.interfacce;

import com.prova.e_commerce.dbRT.model.ShippingStatus;
import com.google.api.core.ApiFuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ShippingStatusRep {

    // ==============================
    // Operazioni sullo Stato della Spedizione
    // ==============================

    // Crea una nuova spedizione
    ApiFuture<Void> createShippingStatus(ShippingStatus shippingStatus);

    // Modifica una spedizione esistente
    ApiFuture<Void> updateShippingStatus(ShippingStatus shippingStatus);

    // Seleziona lo stato di spedizione per ID
    CompletableFuture<ShippingStatus> selectShippingStatusById(String shippingStatusId);

    // Elimina lo stato di spedizione
    ApiFuture<Void> deleteShippingStatus(String shippingStatusId);

    // ==============================
    // Operazioni sulle Locazioni
    // ==============================

    // Crea una nuova locazione per una spedizione
    ApiFuture<Void> createLocationForShipping(String shippingStatusId, ShippingStatus.CurrentLocation location);

    // Seleziona tutte le locazioni storiche di una spedizione
    CompletableFuture<List<ShippingStatus.HistoricalLocation>> selectLocationsForShipping(String shippingStatusId);

    // Elimina una locazione storica da una spedizione
    ApiFuture<Void> deleteLocationFromShipping(String shippingStatusId, String locationId);
}
