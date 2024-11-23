package com.prova.e_commerce.dbRT.controller.webSocket;

import com.prova.e_commerce.dbRT.model.ShippingStatus;
import com.prova.e_commerce.dbRT.service.ShippingStatusService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
public class ShippingStatusWebSocketController {

    @Autowired
    private ShippingStatusService shippingStatusService;

    // ==============================
    // Operazioni sullo Stato della Spedizione
    // ==============================

    // Crea una nuova spedizione (Riceve i dati dal client, crea la spedizione e invia una risposta)
    @MessageMapping("/createShippingStatus")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> createShippingStatus(@Valid ShippingStatus shippingStatus) {
        return shippingStatusService.createShippingStatus(shippingStatus)
                .thenApply(v -> "Spedizione creata con successo! ID: " + shippingStatus.getId());
    }

    // Modifica una spedizione esistente
    @MessageMapping("/updateShippingStatus")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> updateShippingStatus(@Valid ShippingStatus shippingStatus) {
        return shippingStatusService.updateShippingStatus(shippingStatus)
                .thenApply(v -> "Spedizione aggiornata con successo! ID: " + shippingStatus.getId());
    }

    // Recupera lo stato della spedizione per ID
    @MessageMapping("/getShippingStatusById")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<ShippingStatus> getShippingStatusById(String shippingStatusId) {
        return shippingStatusService.getShippingStatusById(shippingStatusId);
    }

    // Elimina lo stato della spedizione
    @MessageMapping("/deleteShippingStatus")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> deleteShippingStatus(String shippingStatusId) {
        return shippingStatusService.deleteShippingStatus(shippingStatusId)
                .thenApply(v -> "Stato della spedizione eliminato con successo! ID: " + shippingStatusId);
    }

    // ==============================
    // Operazioni sulle Locazioni
    // ==============================

    // Aggiungi una nuova locazione per una spedizione
    @MessageMapping("/addLocationToShipping")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> addLocationToShipping(String shippingStatusId, @Valid ShippingStatus.CurrentLocation location) {
        return shippingStatusService.addLocationToShipping(shippingStatusId, location)
                .thenApply(v -> "Locazione aggiunta con successo per la spedizione ID: " + shippingStatusId);
    }

    // Recupera tutte le locazioni storiche di una spedizione
    @MessageMapping("/getLocationsForShipping")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<List<ShippingStatus.HistoricalLocation>> getLocationsForShipping(String shippingStatusId) {
        return shippingStatusService.getLocationsForShipping(shippingStatusId);
    }

    // Elimina una locazione storica da una spedizione
    @MessageMapping("/deleteLocationFromShipping")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> deleteLocationFromShipping(String shippingStatusId, String locationId) {
        return shippingStatusService.deleteLocationFromShipping(shippingStatusId, locationId)
                .thenApply(v -> "Locazione eliminata con successo dalla spedizione ID: " + shippingStatusId);
    }
}
