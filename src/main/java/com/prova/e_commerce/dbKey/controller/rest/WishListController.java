package com.prova.e_commerce.dbKey.controller.rest;

import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.model.WishList;
import com.prova.e_commerce.dbKey.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/wishlist")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    /**
     * Endpoint per aggiungere prodotti alla wishlist di un utente
     * 
     * @param userId l'ID dell'utente
     * @param prodotti la lista di prodotti da aggiungere
     * @return una risposta che indica l'esito dell'operazione
     */
    @PostMapping("/add")
    public ResponseEntity<String> aggiungiProdotti(
            @RequestParam String userId, 
            @RequestBody List<Prodotto> prodotti) {
        
        wishListService.aggiungiProdotti(userId, prodotti);
        return ResponseEntity.ok("Prodotti aggiunti alla wishlist con successo");
    }

    /**
     * Endpoint per ottenere la wishlist di un utente
     * 
     * @param userId l'ID dell'utente
     * @return la wishlist dell'utente, se esistente
     */
    @GetMapping("/{userId}")
    public ResponseEntity<WishList> getWishList(@PathVariable String userId) {
        Optional<WishList> wishList = wishListService.trovaWishList(userId);
        
        if (wishList.isPresent()) {
            return ResponseEntity.ok(wishList.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint per rimuovere un prodotto dalla wishlist di un utente
     * 
     * @param userId l'ID dell'utente
     * @param prodottoId l'ID del prodotto da rimuovere
     * @return una risposta che indica l'esito dell'operazione
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> rimuoviProdotto(
            @RequestParam String userId, 
            @RequestParam String prodottoId) {
        
        wishListService.rimuoviProdotto(userId, prodottoId);
        return ResponseEntity.ok("Prodotto rimosso dalla wishlist con successo");
    }

    /**
     * Endpoint per resettare la wishlist di un utente
     * 
     * @param userId l'ID dell'utente
     * @return una risposta che indica l'esito dell'operazione
     */
    @DeleteMapping("/reset")
    public ResponseEntity<String> resetWishList(@RequestParam String userId) {
        wishListService.resetWishList(userId);
        return ResponseEntity.ok("Wishlist resettata con successo");
    }
}
