package com.prova.e_commerce.dbDoc.controller.rest;

import com.prova.e_commerce.dbDoc.entity.Recensioni;
import com.prova.e_commerce.dbDoc.service.RecensioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Endpoint per creare una nuova recensione
    @PostMapping
    public ResponseEntity<Recensioni> createRecensione(@RequestBody Recensioni recensione) {
        Recensioni newRecensione = recensioniService.saveRecensione(recensione);
        return ResponseEntity.status(201).body(newRecensione);  // Restituisce il nuovo oggetto con status 201 Created
    }

    //Generazione di dati randomici
    @GetMapping("/random/{count}")
    public List<Recensioni> getRandomRecensioni(@PathVariable int count) {
        return recensioniService.generateRandomRecensioni(count);
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
