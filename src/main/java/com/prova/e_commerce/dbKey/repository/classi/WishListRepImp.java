package com.prova.e_commerce.dbKey.repository.classi;

import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoCollection;
import com.arangodb.entity.BaseDocument;
import com.prova.e_commerce.dbKey.model.WishList;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.WishListRep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class WishListRepImp implements WishListRep {

    @Autowired
    private ArangoDatabase arangoDatabase;

    @Autowired
    private ArangoCollection wishListCollection;

    // Aggiungi prodotti alla wishlist
    @Override
    public void aggiungiProdotti(String userId, List<Prodotto> nuoviProdotti) {
        // Recupera la wishlist esistente per l'utente
        Optional<WishList> existingWishList = trovaWishList(userId);

        WishList wishList;
        if (existingWishList.isEmpty()) {
            // Se la wishlist non esiste, creala
            wishList = new WishList();
            wishList.setKey(userId);
            wishList.setProducts(nuoviProdotti);
        } else {
            // Se esiste, aggiungi i nuovi prodotti
            List<Prodotto> prodottiEsistenti = existingWishList.get().getProducts();
            prodottiEsistenti.addAll(nuoviProdotti);
            wishList = existingWishList.get();
            wishList.setProducts(prodottiEsistenti);
        }

        // Crea il documento da inserire o aggiornare in ArangoDB
        BaseDocument document = new BaseDocument(userId);
        document.addAttribute("products", wishList.getProducts());

        // Salva il documento nel database
        wishListCollection.insertDocument(document);
    }

    // Seleziona la wishlist di un utente
    @Override
    public Optional<WishList> trovaWishList(String userId) {
        try {
            // Recupera il documento della wishlist
            BaseDocument document = wishListCollection.getDocument(userId, BaseDocument.class);
            if (document != null) {
                // Mappa il documento a un oggetto WishList
                WishList wishList = new WishList();
                wishList.setKey(document.getKey());
                wishList.setProducts((List<Prodotto>) document.getAttribute("products"));
                return Optional.of(wishList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Rimuovi un prodotto dalla wishlist
    @Override
    public void rimuoviProdotto(String userId, String prodottoId) {
        Optional<WishList> wishListOpt = trovaWishList(userId);

        if (wishListOpt.isPresent()) {
            WishList wishList = wishListOpt.get();
            List<Prodotto> prodotti = wishList.getProducts();

            // Filtra il prodotto da rimuovere
            List<Prodotto> prodottiRimanenti = prodotti.stream()
                    .filter(prodotto -> !prodotto.getKey().equals(prodottoId))  // Usa la chiave per il confronto
                    .collect(Collectors.toList());

            // Aggiorna la lista dei prodotti
            wishList.setProducts(prodottiRimanenti);

            // Crea o aggiorna il documento nel database
            BaseDocument document = new BaseDocument(userId);
            document.addAttribute("products", wishList.getProducts());
            wishListCollection.updateDocument(userId, document);
        }
    }

    // Resetta tutti i prodotti nella wishlist
    @Override
    public void resetWishList(String userId) {
        Optional<WishList> wishListOpt = trovaWishList(userId);

        if (wishListOpt.isPresent()) {
            WishList wishList = wishListOpt.get();
            wishList.setProducts(null); // Rimuove tutti i prodotti

            // Aggiorna il documento nel database
            BaseDocument document = new BaseDocument(userId);
            document.addAttribute("products", null);
            wishListCollection.updateDocument(userId, document);
        }
    }
}
