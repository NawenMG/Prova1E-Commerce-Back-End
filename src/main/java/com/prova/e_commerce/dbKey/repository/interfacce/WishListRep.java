package com.prova.e_commerce.dbKey.repository.interfacce;

import com.prova.e_commerce.dbKey.model.WishList;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;

import java.util.List;
import java.util.Optional;

public interface WishListRep {
    
    // Aggiungi una lista di prodotti alla wishlist di un utente
    void aggiungiProdotti(String userId, List<Prodotto> nuoviProdotti);
    
    // Recupera la wishlist di un utente
    Optional<WishList> trovaWishList(String userId);
    
    // Rimuove un prodotto dalla wishlist
    void rimuoviProdotto(String userId, String prodottoId);
    
    // Resetta la wishlist di un utente
    void resetWishList(String userId);
}
