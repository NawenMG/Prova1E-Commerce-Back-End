package com.prova.e_commerce.dbKey.service;

import com.prova.e_commerce.dbKey.model.SettingSite;
import com.prova.e_commerce.dbKey.repository.interfacce.SettingSiteRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingSiteService {

    @Autowired
    private SettingSiteRep settingSiteRep;

    /**
     * Metodo per recuperare le impostazioni di un utente dalla cache o dal repository.
     * La cache è attiva per 10 minuti in Caffeine e 30 minuti in Redis.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#userId", unless = "#result == null")
    public Optional<SettingSite> trovaImpostazioni(String userId) {
        // Chiamata al repository per trovare le impostazioni dell'utente
        return settingSiteRep.trovaImpostazioni(userId);
    }

    /**
     * Metodo per aggiungere o aggiornare le impostazioni di un utente.
     * Invalida la cache dell'utente dopo l'operazione.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void salvaImpostazioni(String userId, SettingSite settings) {
        // Chiamata al repository per salvare o aggiornare le impostazioni
        settingSiteRep.salvaImpostazioni(userId, settings);
    }

    /**
     * Metodo per resettare le impostazioni di un utente.
     * Invalida la cache dell'utente dopo l'operazione.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void resetImpostazioni(String userId) {
        // Chiamata al repository per resettare le impostazioni
        settingSiteRep.resetImpostazioni(userId);
    }

    /**
     * Metodo privato per invalidare la cache specifica dell'utente, chiamato dopo le modifiche.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    private void evictSettingCache(String userId) {
        // Questo metodo non ha logica visibile, serve solo a invalidare la cache per l'utente specificato
    }
}
