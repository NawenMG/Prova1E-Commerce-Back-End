package com.prova.e_commerce.dbKey.controller.rest;

import com.prova.e_commerce.dbKey.model.WishList;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.service.WishListService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @PostMapping("/{userId}/prodotti")
    public ResponseEntity<?> aggiungiProdotti(@PathVariable String userId, @RequestBody List<Prodotto> prodotti) {
        try {
            wishListService.aggiungiProdotti(userId, prodotti);
            return ResponseEntity.ok("Prodotti aggiunti con successo alla wishlist.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
public ResponseEntity<?> trovaWishList(@PathVariable String userId) {
    try {
        Optional<WishList> wishList = wishListService.trovaWishList(userId);
        
        // Se la wishlist è presente, restituisci un OK con la wishlist
        if (wishList.isPresent()) {
            return ResponseEntity.ok(wishList.get());
        } else {
            // Se la wishlist non è presente, restituisci una risposta con NOT_FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wishlist non trovata");
        }
    } catch (IllegalArgumentException e) {
        // Gestione degli errori di validazione
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        // Gestione di errori generali
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
    }
}



    @DeleteMapping("/{userId}/prodotti/{prodottoId}")
    public ResponseEntity<?> rimuoviProdotto(@PathVariable String userId, @PathVariable String prodottoId) {
        try {
            wishListService.rimuoviProdotto(userId, prodottoId);
            return ResponseEntity.ok("Prodotto rimosso con successo dalla wishlist.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> resetWishList(@PathVariable String userId) {
        try {
            wishListService.resetWishList(userId);
            return ResponseEntity.ok("Wishlist resettata con successo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/prodotti/{prodottoId}/immagine")
    public ResponseEntity<?> caricaImmagineProdotto(
            @PathVariable String userId,
            @PathVariable String prodottoId,
            @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = wishListService.caricaImmagineProdotto(userId, prodottoId, file);
            return ResponseEntity.ok("Immagine caricata con successo: " + fileUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/prodotti/{prodottoId}/immagine")
    public ResponseEntity<?> scaricaImmagineProdotto(@PathVariable String userId, @PathVariable String prodottoId) {
        try {
            InputStream inputStream = wishListService.scaricaImmagineProdotto(userId, prodottoId);
            return ResponseEntity.ok().body("Immagine scaricata con successo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/prodotti/{prodottoId}/immagine")
    public ResponseEntity<?> eliminaImmagineProdotto(@PathVariable String userId, @PathVariable String prodottoId) {
        try {
            wishListService.eliminaImmagineProdotto(userId, prodottoId);
            return ResponseEntity.ok("Immagine eliminata con successo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno: " + e.getMessage());
        }
    }
}
