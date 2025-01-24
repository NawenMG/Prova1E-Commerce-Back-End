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

    /**
     * Crea un nuovo stato di spedizione.
     * Riceve i dati del nuovo ShippingStatus dal client, lo crea, e invia una risposta al topic.
     *
     * @param shippingStatus Oggetto ShippingStatus ricevuto.
     * @return Messaggio di conferma con l'ID della spedizione creata.
     */
    @MessageMapping("/createShippingStatus")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> createShippingStatus(@Valid ShippingStatus shippingStatus) {
        return shippingStatusService.createShippingStatus(shippingStatus)
                .thenApply(v -> "Spedizione creata con successo! ID: " + shippingStatus.getId());
    }

    /**
     * Aggiorna uno stato di spedizione esistente.
     * Riceve i dati aggiornati dal client e invia una risposta al topic.
     *
     * @param shippingStatus Oggetto ShippingStatus con i dati aggiornati.
     * @return Messaggio di conferma con l'ID della spedizione aggiornata.
     */
    @MessageMapping("/updateShippingStatus")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> updateShippingStatus(@Valid ShippingStatus shippingStatus) {
        return shippingStatusService.updateShippingStatus(shippingStatus)
                .thenApply(v -> "Spedizione aggiornata con successo! ID: " + shippingStatus.getId());
    }

    /**
     * Recupera uno stato di spedizione tramite il suo ID.
     *
     * @param shippingStatusId ID dello stato di spedizione da recuperare.
     * @return Oggetto ShippingStatus corrispondente all'ID fornito.
     */
    @MessageMapping("/getShippingStatusById")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<ShippingStatus> getShippingStatusById(String shippingStatusId) {
        return shippingStatusService.getShippingStatusById(shippingStatusId);
    }

    /**
     * Elimina uno stato di spedizione tramite il suo ID.
     *
     * @param shippingStatusId ID dello stato di spedizione da eliminare.
     * @return Messaggio di conferma con l'ID della spedizione eliminata.
     */
    @MessageMapping("/deleteShippingStatus")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> deleteShippingStatus(String shippingStatusId) {
        return shippingStatusService.deleteShippingStatus(shippingStatusId)
                .thenApply(v -> "Stato della spedizione eliminato con successo! ID: " + shippingStatusId);
    }

    // ==============================
    // Operazioni sulle Locazioni
    // ==============================

    /**
     * Aggiunge una nuova locazione a uno stato di spedizione.
     *
     * @param shippingStatusId ID dello stato di spedizione.
     * @param location         Nuova locazione da aggiungere.
     * @return Messaggio di conferma con l'ID della spedizione e la locazione aggiunta.
     */
    @MessageMapping("/addLocationToShipping")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> addLocationToShipping(String shippingStatusId, @Valid ShippingStatus.CurrentLocation location) {
        return shippingStatusService.addLocationToShipping(shippingStatusId, location)
                .thenApply(v -> "Locazione aggiunta con successo per la spedizione ID: " + shippingStatusId);
    }

    /**
     * Recupera tutte le locazioni storiche di uno stato di spedizione.
     *
     * @param shippingStatusId ID dello stato di spedizione.
     * @return Lista delle locazioni storiche associate alla spedizione.
     */
    @MessageMapping("/getLocationsForShipping")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<List<ShippingStatus.HistoricalLocation>> getLocationsForShipping(String shippingStatusId) {
        return shippingStatusService.getLocationsForShipping(shippingStatusId);
    }

    /**
     * Elimina una locazione storica da uno stato di spedizione.
     *
     * @param shippingStatusId ID dello stato di spedizione.
     * @param locationId       ID della locazione da eliminare.
     * @return Messaggio di conferma con l'ID della spedizione e della locazione eliminata.
     */
    @MessageMapping("/deleteLocationFromShipping")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> deleteLocationFromShipping(String shippingStatusId, String locationId) {
        return shippingStatusService.deleteLocationFromShipping(shippingStatusId, locationId)
                .thenApply(v -> "Locazione eliminata con successo dalla spedizione ID: " + shippingStatusId);
    }

    // ==============================
    // Operazioni RabbitMQ
    // ==============================

    /**
     * Invia uno stato di spedizione a RabbitMQ.
     *
     * @param shippingStatus Oggetto ShippingStatus da inviare.
     * @return Messaggio di conferma per l'invio a RabbitMQ.
     */
    @MessageMapping("/sendToRabbitMQ")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<String> sendToRabbitMQ(@Valid ShippingStatus shippingStatus) {
        return shippingStatusService.sendToRabbitMQ(shippingStatus)
                .thenApply(v -> "Stato di spedizione inviato a RabbitMQ con successo! ID: " + shippingStatus.getId());
    }

    /**
     * Riceve uno stato di spedizione da RabbitMQ.
     *
     * @return Oggetto ShippingStatus ricevuto da RabbitMQ.
     */
    @MessageMapping("/receiveFromRabbitMQ")
    @SendTo("/topic/shippingStatus")
    public CompletableFuture<ShippingStatus> receiveFromRabbitMQ() {
        return shippingStatusService.receiveFromRabbitMQ();
    }
}
