package com.prova.e_commerce.dbKey.repository.classi;

import com.prova.e_commerce.dbKey.model.WishList;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.WishListRep;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.Optional;

@Repository
public class WishListRepImp implements WishListRep {

    private final DynamoDbTable<WishList> wishListTable;

    // Costruttore per configurare la connessione alla tabella DynamoDB
    public WishListRepImp(DynamoDbClient dynamoDbClient) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        this.wishListTable = enhancedClient.table("WishList", TableSchema.fromBean(WishList.class));
    }

    // Aggiungi prodotti alla wishlist
    public void aggiungiProdotti(String userId, List<Prodotto> nuoviProdotti) {
        WishList wishList = wishListTable.getItem(r -> r.key(k -> k.partitionValue(userId)));

        if (wishList == null) {
            // Se la wishlist non esiste, creala con i nuovi prodotti
            wishList = new WishList();
            wishList.setUserId(userId);
            wishList.setProducts(nuoviProdotti);
        } else {
            // Se esiste, aggiungi i nuovi prodotti alla lista
            List<Prodotto> prodottiEsistenti = wishList.getProducts();
            prodottiEsistenti.addAll(nuoviProdotti);
            wishList.setProducts(prodottiEsistenti);
        }

        // Salva la wishlist aggiornata nella tabella DynamoDB
        wishListTable.putItem(wishList);
    }

    // Seleziona la wishlist di un utente
    public Optional<WishList> trovaWishList(String userId) {
        return Optional.ofNullable(wishListTable.getItem(r -> r.key(k -> k.partitionValue(userId))));
    }

    // Rimuovi un prodotto dalla wishlist
    public void rimuoviProdotto(String userId, String prodottoId) {
        WishList wishList = wishListTable.getItem(r -> r.key(k -> k.partitionValue(userId)));

        if (wishList != null && wishList.getProducts() != null) {
            List<Prodotto> prodotti = wishList.getProducts();
            prodotti.removeIf(prodotto -> prodotto.getProductId().equals(prodottoId)); // Rimuove il prodotto con l'ID specificato

            wishList.setProducts(prodotti);

            // Salva la wishlist aggiornata
            wishListTable.putItem(wishList);
        }
    }

    // Resetta tutti i prodotti nella wishlist (può essere utile per rimuovere tutti i prodotti)
    public void resetWishList(String userId) {
        WishList wishList = wishListTable.getItem(r -> r.key(k -> k.partitionValue(userId)));

        if (wishList != null) {
            wishList.setProducts(null); // Rimuove tutti i prodotti dalla wishlist
            wishListTable.putItem(wishList); // Salva la wishlist vuota
        }
    }
}
