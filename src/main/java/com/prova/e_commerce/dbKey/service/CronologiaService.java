package com.prova.e_commerce.dbKey.service;

import com.prova.e_commerce.dbKey.model.Cronologia;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.CronologiaRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CronologiaService {

    @Autowired
    private CronologiaRep cronologiaRep;

    /**
     * Metodo per aggiungere nuovi prodotti alla cronologia dell'utente e invalidare la cache.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void aggiungiDatiCronologici(String userId, List<Prodotto> nuoviProdotti) {
        cronologiaRep.aggiungiDatiCronologici(userId, nuoviProdotti);
    }

    /**
     * Metodo per visualizzare la cronologia dell'utente specificato. La cache è attiva per 10 minuti in Caffeine
     * e 30 minuti in Redis.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#userId", unless = "#result == null")
    public Optional<Cronologia> visualizzaCronologia(String userId) {
        return cronologiaRep.visualizzaCronologia(userId);
    }

    /**
     * Metodo per eliminare un singolo prodotto dalla cronologia dell'utente e invalidare la cache.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void eliminaSingolaRicerca(String userId, String productId) {
        cronologiaRep.eliminaSingolaRicerca(userId, productId);
    }

    /**
     * Metodo per eliminare tutte le ricerche dalla cronologia dell'utente e invalidare la cache.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void eliminaTutteLeRicerche(String userId) {
        cronologiaRep.eliminaTutteLeRicerche(userId);
    }

    /**
     * Metodo per invalidare la cache quando vengono apportate modifiche alla cronologia dell'utente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    private void evictCronologiaCache(String userId) {
        // Questo metodo non fa nulla visibile, serve solo a invalidare la cache per la cronologia dell'utente specificato
    }
}
