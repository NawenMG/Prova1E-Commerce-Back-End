package com.prova.e_commerce.dbKey.service;

import com.prova.e_commerce.dbKey.model.WishList;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.WishListRep;
import com.prova.e_commerce.storage.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class WishListService {

    @Autowired
    private WishListRep wishListRep;

    @Autowired
    private S3Service s3Service;

    /**
     * Metodo per aggiungere nuovi prodotti alla wishlist dell'utente.
     * Invalida la cache dell'utente dopo l'operazione.
     */
    @CacheEvict(value = { "caffeine", "redis"}, key = "#userId", allEntries = false)
    public void aggiungiProdotti(String userId, List<Prodotto> nuoviProdotti) {
        if (nuoviProdotti == null || nuoviProdotti.isEmpty()) {
            throw new IllegalArgumentException("La lista dei prodotti non può essere vuota o null.");
        }
        wishListRep.aggiungiProdotti(userId, nuoviProdotti);
    }

    /**
     * Metodo per recuperare la wishlist dell'utente.
     * La cache è attiva per 10 minuti in Caffeine e 30 minuti in Redis.
     */
    @Cacheable(value = { "caffeine", "redis"}, key = "#userId", unless = "#result == null")
    public Optional<WishList> trovaWishList(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
        }
        return wishListRep.trovaWishList(userId);
    }

    /**
     * Metodo per rimuovere un prodotto dalla wishlist.
     * Invalida la cache dell'utente dopo l'operazione.
     */
    @CacheEvict(value = { "caffeine", "redis"}, key = "#userId", allEntries = false)
    public void rimuoviProdotto(String userId, String prodottoId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
        }
        if (prodottoId == null || prodottoId.isEmpty()) {
            throw new IllegalArgumentException("Il prodottoId non può essere null o vuoto.");
        }
        wishListRep.rimuoviProdotto(userId, prodottoId);
    }

    /**
     * Metodo per resettare la wishlist dell'utente.
     * Invalida la cache dell'utente dopo l'operazione.
     */
    @CacheEvict(value = { "caffeine", "redis"}, key = "#userId", allEntries = false)
    public void resetWishList(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
        }
        wishListRep.resetWishList(userId);
    }

    /**
     * Metodo per caricare un'immagine per un prodotto nella wishlist.
     * Invalida la cache dell'utente dopo l'operazione.
     */
    @CacheEvict(value = { "caffeine", "redis"}, key = "#userId", allEntries = false)
    public String caricaImmagineProdotto(String userId, String prodottoId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Il file non può essere null o vuoto.");
        }

        // Recupera la wishlist
        WishList wishList = wishListRep.trovaWishList(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist non trovata per l'userId: " + userId));

        // Trova il prodotto
        Prodotto prodotto = wishList.getProducts().stream()
                .filter(p -> p.getProductId().equals(prodottoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato nella wishlist: " + prodottoId));

        // Carica l'immagine su S3
        String folder = "wishlist/" + userId + "/" + prodottoId;
        String fileUrl = s3Service.uploadFile(folder, file);

        // Aggiorna il prodotto con il nuovo URL dell'immagine
        prodotto.setImmagine(fileUrl);

        // Salva nuovamente la wishlist
        wishListRep.resetWishList(userId);
        wishListRep.aggiungiProdotti(userId, wishList.getProducts());

        return fileUrl;
    }

    /**
     * Metodo per scaricare l'immagine di un prodotto dalla wishlist.
     */
    public InputStream scaricaImmagineProdotto(String userId, String prodottoId) throws IOException {
        WishList wishList = wishListRep.trovaWishList(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist non trovata per l'userId: " + userId));

        Prodotto prodotto = wishList.getProducts().stream()
                .filter(p -> p.getProductId().equals(prodottoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato nella wishlist: " + prodottoId));

        String fileUrl = prodotto.getImmagine();
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new RuntimeException("Nessuna immagine trovata per il prodotto: " + prodottoId);
        }

        // Estrai la chiave dal fileUrl
        String key = fileUrl.substring(fileUrl.indexOf("amazonaws.com/") + 14);

        return s3Service.downloadFile(key);
    }

    /**
     * Metodo per eliminare l'immagine associata a un prodotto nella wishlist.
     * Invalida la cache dell'utente dopo l'operazione.
     */
    @CacheEvict(value = { "caffeine", "redis"}, key = "#userId", allEntries = false)
    public void eliminaImmagineProdotto(String userId, String prodottoId) {
        WishList wishList = wishListRep.trovaWishList(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist non trovata per l'userId: " + userId));

        Prodotto prodotto = wishList.getProducts().stream()
                .filter(p -> p.getProductId().equals(prodottoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato nella wishlist: " + prodottoId));

        String fileUrl = prodotto.getImmagine();

        if (fileUrl != null && !fileUrl.isEmpty()) {
            String key = fileUrl.substring(fileUrl.indexOf("amazonaws.com/") + 14);
            s3Service.deleteFile(key);
            prodotto.setImmagine(null);

            wishListRep.resetWishList(userId);
            wishListRep.aggiungiProdotti(userId, wishList.getProducts());
        } else {
            throw new RuntimeException("Nessuna immagine associata al prodotto: " + prodottoId);
        }
    }
}
