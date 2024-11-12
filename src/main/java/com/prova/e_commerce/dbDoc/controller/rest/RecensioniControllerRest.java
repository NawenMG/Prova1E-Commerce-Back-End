package com.prova.e_commerce.dbDoc.controller.rest;

import com.prova.e_commerce.dbDoc.entity.Recensioni;
import com.prova.e_commerce.dbDoc.service.RecensioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recensioni")
public class RecensioniControllerRest {

    @Autowired
    private RecensioniService recensioniService;

    // Endpoint per ottenere tutte le recensioni di un prodotto specifico tramite il productId
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Recensioni>> getByProductId(@PathVariable String productId) {
        List<Recensioni> recensioni = recensioniService.findByProductId(productId);
        return recensioni.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(recensioni);
    }

    // Endpoint per ottenere tutte le recensioni scritte da un determinato utente tramite userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recensioni>> getByUserId(@PathVariable String userId) {
        List<Recensioni> recensioni = recensioniService.findByUserId(userId);
        return recensioni.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(recensioni);
    }

    // Endpoint per eseguire una ricerca dinamica delle recensioni
    @GetMapping("/ricerca-dinamica")
    public ResponseEntity<List<Recensioni>> getByDynamicCriteria(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String productId,
            @RequestParam(required = false) Integer votoMin,
            @RequestParam(required = false) Integer votoMax,
            @RequestParam(required = false) String titolo,
            @RequestParam(required = false) String descrizione,
            @RequestParam(required = false) Map<String, String> parametriAggiuntivi) {
        
        List<Recensioni> recensioni = recensioniService.findByDynamicCriteria(
                userId, productId, votoMin, votoMax, titolo, descrizione, parametriAggiuntivi);
        
        return recensioni.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(recensioni);
    }

    // Endpoint per creare una nuova recensione
    @PostMapping
    public ResponseEntity<Recensioni> createRecensione(@RequestBody Recensioni recensione) {
        Recensioni newRecensione = recensioniService.saveRecensione(recensione);
        return ResponseEntity.status(201).body(newRecensione);  // Restituisce il nuovo oggetto con status 201 Created
    }

    // Endpoint per aggiornare una recensione esistente tramite l'ID
    @PutMapping("/{id}")
    public ResponseEntity<Recensioni> updateRecensione(@PathVariable String id, @RequestBody Recensioni recensione) {
        Recensioni updatedRecensione = recensioniService.updateRecensione(id, recensione);
        return updatedRecensione != null 
                ? ResponseEntity.ok(updatedRecensione) 
                : ResponseEntity.notFound().build();
    }

    // Endpoint per eliminare una recensione esistente tramite l'ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecensione(@PathVariable String id) {
        return recensioniService.deleteRecensione(id) 
                ? ResponseEntity.noContent().build()  // Restituisce status 204 No Content se la cancellazione è avvenuta con successo
                : ResponseEntity.notFound().build();  // Restituisce status 404 Not Found se la recensione non è stata trovata
    }
}
