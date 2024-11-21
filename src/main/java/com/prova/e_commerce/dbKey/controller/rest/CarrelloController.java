package com.prova.e_commerce.dbKey.controller.rest;

import com.prova.e_commerce.dbKey.model.Carrello;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.service.CarrelloService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
    @PostMapping("/{userId}/prodotti")
    public ResponseEntity<String> aggiungiProdotti(
            @PathVariable String userId,
            @RequestBody List<Prodotto> nuoviProdotti) {
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
    @GetMapping("/{userId}")
public ResponseEntity<?> getCarrello(@PathVariable String userId) {
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
    @DeleteMapping("/{userId}/prodotti/{prodottoId}")
    public ResponseEntity<String> rimuoviProdotto(
            @PathVariable String userId,
            @PathVariable String prodottoId) {
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
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> svuotaCarrello(@PathVariable String userId) {
        try {
            carrelloService.svuotaCarrello(userId);
            return ResponseEntity.ok("Carrello svuotato con successo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
        }
    }

    /**
     * Carica un'immagine per un prodotto nel carrello.
     */
    @PostMapping("/{userId}/prodotti/{prodottoId}/immagine")
    public ResponseEntity<?> caricaImmagineProdotto(
            @PathVariable String userId,
            @PathVariable String prodottoId,
            @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = carrelloService.caricaImmagineProdotto(userId, prodottoId, file);
            return ResponseEntity.ok("Immagine caricata con successo. URL: " + fileUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il caricamento del file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
        }
    }

    /**
     * Scarica l'immagine di un prodotto.
     */
    @GetMapping("/{userId}/prodotti/{prodottoId}/immagine")
    public ResponseEntity<?> scaricaImmagineProdotto(
            @PathVariable String userId,
            @PathVariable String prodottoId) {
        try (InputStream inputStream = carrelloService.scaricaImmagineProdotto(userId, prodottoId)) {
            return ResponseEntity.ok().body(inputStream.readAllBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il download del file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
        }
    }

    /**
     * Elimina l'immagine di un prodotto dal carrello.
     */
    @DeleteMapping("/{userId}/prodotti/{prodottoId}/immagine")
    public ResponseEntity<String> eliminaImmagineProdotto(
            @PathVariable String userId,
            @PathVariable String prodottoId) {
        try {
            carrelloService.eliminaImmagineProdotto(userId, prodottoId);
            return ResponseEntity.ok("Immagine eliminata con successo.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
        }
    }
}
