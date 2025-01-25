package com.prova.e_commerce.dbRT.controller.webSocket;

import com.prova.e_commerce.dbRT.model.ShippingStatus;
import com.prova.e_commerce.dbRT.service.ShippingStatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Controller
@PreAuthorize("hasRole('DELIVERYUSER')")
public class ShippingStatusWebSocketController {

    @Autowired
    private ShippingStatusService shippingStatusService;

    // ==================================================
    //             OPERAZIONI SULLO SHIPPING STATUS
    // ==================================================

    /**
     * Creazione di un nuovo ShippingStatus.
     * - Il client invia un messaggio a "/app/createShippingStatus"
     * - Il server risponde su "/topic/shippingStatus"
     *
     * @param shippingStatus Dati dello stato di spedizione da creare
     * @return Un messaggio di conferma
     */
    @MessageMapping("/createShippingStatus")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> createShippingStatus(@Valid ShippingStatus shippingStatus) {
        return shippingStatusService.createShippingStatus(shippingStatus)
            .thenApply(v -> "Spedizione creata con successo! ID: " + shippingStatus.getId());
    }

    /**
     * Aggiornamento di uno ShippingStatus esistente.
     * - Il client invia un messaggio a "/app/updateShippingStatus"
     * - Il server risponde su "/topic/shippingStatus"
     *
     * @param shippingStatus Dati aggiornati dello stato di spedizione
     * @return Messaggio di conferma
     */
    @MessageMapping("/updateShippingStatus")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> updateShippingStatus(@Valid ShippingStatus shippingStatus) {
        return shippingStatusService.updateShippingStatus(shippingStatus)
            .thenApply(v -> "Spedizione aggiornata con successo! ID: " + shippingStatus.getId());
    }

    /**
     * Recupera uno ShippingStatus per ID.
     * - Il client invia un messaggio a "/app/getShippingStatusById"
     * - Il server risponde su "/topic/shippingStatus" con l'oggetto ShippingStatus
     *
     * @param shippingStatusId L'ID della spedizione
     * @return L'oggetto ShippingStatus corrispondente
     */
    @MessageMapping("/getShippingStatusById")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<ShippingStatus> getShippingStatusById(String shippingStatusId) {
        return shippingStatusService.getShippingStatusById(shippingStatusId);
    }

    /**
     * Elimina uno ShippingStatus per ID.
     * - Il client invia un messaggio a "/app/deleteShippingStatus"
     * - Il server risponde su "/topic/shippingStatus" con un messaggio di conferma
     *
     * @param shippingStatusId L'ID della spedizione da eliminare
     * @return Messaggio di conferma
     */
    @MessageMapping("/deleteShippingStatus")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> deleteShippingStatus(String shippingStatusId) {
        return shippingStatusService.deleteShippingStatus(shippingStatusId)
            .thenApply(v -> "Stato della spedizione eliminato con successo! ID: " + shippingStatusId);
    }

    // ==================================================
    //           OPERAZIONI SULLE LOCAZIONI
    // ==================================================

    /**
     * Aggiunge una CurrentLocation a uno ShippingStatus esistente.
     * - Il client invia un messaggio a "/app/addLocationToShipping"
     * - Il server risponde su "/topic/shippingStatus" con messaggio di conferma
     *
     * @param shippingStatusId ID della spedizione
     * @param location         L'oggetto CurrentLocation da aggiungere
     * @return Messaggio di conferma
     */
    @MessageMapping("/addLocationToShipping")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> addLocationToShipping(String shippingStatusId,
                                                           @Valid ShippingStatus.CurrentLocation location) {
        return shippingStatusService.addLocationToShipping(shippingStatusId, location)
            .thenApply(v -> "Locazione aggiunta con successo per la spedizione ID: " + shippingStatusId);
    }

    /**
     * Recupera tutte le locazioni storiche di uno ShippingStatus.
     * - Il client invia un messaggio a "/app/getLocationsForShipping"
     * - Il server risponde su "/topic/shippingStatus" con la lista di locazioni storiche
     *
     * @param shippingStatusId ID della spedizione
     * @return La lista di HistoricalLocation
     */
    @MessageMapping("/getLocationsForShipping")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<List<ShippingStatus.HistoricalLocation>> getLocationsForShipping(String shippingStatusId) {
        return shippingStatusService.getLocationsForShipping(shippingStatusId);
    }

    /**
     * Elimina una locazione (HistoricalLocation) da uno ShippingStatus esistente.
     * - Il client invia un messaggio a "/app/deleteLocationFromShipping"
     * - Il server risponde su "/topic/shippingStatus" con un messaggio di conferma
     *
     * @param shippingStatusId L'ID della spedizione
     * @param locationId       L'ID della locazione da eliminare
     * @return Messaggio di conferma
     */
    @MessageMapping("/deleteLocationFromShipping")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> deleteLocationFromShipping(String shippingStatusId, String locationId) {
        return shippingStatusService.deleteLocationFromShipping(shippingStatusId, locationId)
            .thenApply(v -> "Locazione eliminata con successo dalla spedizione ID: " + shippingStatusId);
    }

    // ==================================================
    //           FUNZIONALITÀ RABBITMQ (OPZIONALE)
    // ==================================================

    /**
     * Invia una richiesta al router delle spedizioni tramite RabbitMQ.
     * - Il client invia un messaggio a "/app/sendToShippingRouter"
     * - Il server risponde su "/topic/shippingStatus" con un messaggio di conferma
     *
     * @param requestData Mappa di dati da inoltrare
     * @return Messaggio di conferma
     */
    @MessageMapping("/sendToShippingRouter")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> sendToShippingRouter(Map<String, Object> requestData) {
        return shippingStatusService.sendToShippingRouter(requestData)
            .thenApply(v -> "Richiesta inviata al router delle spedizioni: " + requestData);
    }
}
