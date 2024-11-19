package com.prova.e_commerce.dbKey.service;

import com.prova.e_commerce.dbKey.model.WishList;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.WishListRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishListService {

    @Autowired
    private WishListRep wishListRep;

    /**
     * Aggiungi prodotti alla wishlist dell'utente specificato.
     * 
     * @param userId l'ID dell'utente
     * @param nuoviProdotti la lista di prodotti da aggiungere
     */
    public void aggiungiProdotti(String userId, List<Prodotto> nuoviProdotti) {
        // Aggiungi i prodotti alla wishlist dell'utente
        wishListRep.aggiungiProdotti(userId, nuoviProdotti);
    }

    /**
     * Recupera la wishlist dell'utente specificato.
     * 
     * @param userId l'ID dell'utente
     * @return un Optional contenente la wishlist, se presente
     */
    public Optional<WishList> trovaWishList(String userId) {
        return wishListRep.trovaWishList(userId);
    }

    /**
     * Rimuovi un prodotto dalla wishlist dell'utente specificato.
     * 
     * @param userId l'ID dell'utente
     * @param prodottoId l'ID del prodotto da rimuovere
     */
    public void rimuoviProdotto(String userId, String prodottoId) {
        // Rimuovi il prodotto dalla wishlist dell'utente
        wishListRep.rimuoviProdotto(userId, prodottoId);
    }

    /**
     * Resetta la wishlist dell'utente specificato.
     * 
     * @param userId l'ID dell'utente
     */
    public void resetWishList(String userId) {
        // Resetta la wishlist dell'utente
        wishListRep.resetWishList(userId);
    }
}
