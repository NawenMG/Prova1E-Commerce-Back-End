package com.prova.e_commerce.dbKey.controller.rest;

import com.prova.e_commerce.dbKey.model.Carrello;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.service.CarrelloService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrello")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;

    // Aggiungi prodotti al carrello
    @PostMapping("/{userId}/aggiungi")
    public ResponseEntity<String> aggiungiProdotti(
            @PathVariable String userId,
            @RequestBody List<Prodotto> prodotti) {
        try {
            carrelloService.aggiungiProdotti(userId, prodotti);
            return ResponseEntity.ok("Prodotti aggiunti al carrello.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Ottieni il carrello per un dato userId
    @GetMapping("/{userId}")
    public ResponseEntity<Carrello> getCarrello(@PathVariable String userId) {
        return carrelloService.getCarrello(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Elimina un prodotto dal carrello
    @DeleteMapping("/{userId}/prodotto/{prodottoId}")
    public ResponseEntity<String> eliminaProdotto(
            @PathVariable String userId,
            @PathVariable String prodottoId) {
        try {
            carrelloService.rimuoviProdotto(userId, prodottoId);
            return ResponseEntity.ok("Prodotto rimosso dal carrello.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Resetta il carrello
    @DeleteMapping("/{userId}/reset")
    public ResponseEntity<String> resetCarrello(@PathVariable String userId) {
        try {
            carrelloService.svuotaCarrello(userId);
            return ResponseEntity.ok("Carrello svuotato.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
