package com.prova.e_commerce.dbKey.controller.rest;

import com.prova.e_commerce.dbKey.model.Carrello;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.service.CarrelloService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carrello")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;

    /**
     * Aggiungi prodotti al carrello.
     */
    @PostMapping("/post/{userId}/prodotti")
    public ResponseEntity<String> aggiungiProdotti(
            @Valid @PathVariable String userId,
            @Valid @RequestBody List<Prodotto> nuoviProdotti) {
        try {
            carrelloService.aggiungiProdotti(userId, nuoviProdotti);
            return ResponseEntity.ok("Prodotti aggiunti al carrello con successo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
        }
    }

    /**
     * Recupera il carrello dell'utente.
     */
    @GetMapping("/get/{userId}")
public ResponseEntity<?> getCarrello(@Valid @PathVariable String userId) {
    try {
        Optional<Carrello> carrello = carrelloService.getCarrello(userId);
        if (carrello.isPresent()) {
            return ResponseEntity.ok(carrello.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrello non trovato");
        }
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
    }
}


    /**
     * Rimuove un prodotto dal carrello.
     */
    @DeleteMapping("/delete/{userId}/prodotti/{prodottoId}")
    public ResponseEntity<String> rimuoviProdotto(
            @Valid @PathVariable String userId,
            @Valid @PathVariable String prodottoId) {
        try {
            carrelloService.rimuoviProdotto(userId, prodottoId);
            return ResponseEntity.ok("Prodotto rimosso dal carrello con successo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
        }
    }

    /**
     * Svuota il carrello dell'utente.
     */
    @DeleteMapping("/svuota/{userId}")
    public ResponseEntity<String> svuotaCarrello(@Valid @PathVariable String userId) {
        try {
            carrelloService.svuotaCarrello(userId);
            return ResponseEntity.ok("Carrello svuotato con successo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
        }
    }
}
