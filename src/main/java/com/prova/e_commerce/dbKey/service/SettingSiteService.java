package com.prova.e_commerce.dbKey.service;

import com.prova.e_commerce.dbKey.model.SettingSite;
import com.prova.e_commerce.dbKey.repository.interfacce.SettingSiteRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingSiteService {

    @Autowired
    private SettingSiteRep settingSiteRep;

    /**
     * Recupera le impostazioni per un utente specificato dal suo ID.
     * @param userId L'ID dell'utente
     * @return Un Optional contenente le impostazioni dell'utente, se esistono
     */
    public Optional<SettingSite> trovaImpostazioni(String userId) {
        // Chiamata al repository per trovare le impostazioni dell'utente
        return settingSiteRep.trovaImpostazioni(userId);
    }

    /**
     * Aggiungi o aggiorna le impostazioni per un utente specificato dal suo ID.
     * @param userId L'ID dell'utente
     * @param settings Le nuove impostazioni da salvare
     */
    public void salvaImpostazioni(String userId, SettingSite settings) {
        // Chiamata al repository per salvare o aggiornare le impostazioni
        settingSiteRep.salvaImpostazioni(userId, settings);
    }

    /**
     * Resetta le impostazioni per un utente specificato dal suo ID.
     * @param userId L'ID dell'utente per il quale resettare le impostazioni
     */
    public void resetImpostazioni(String userId) {
        // Chiamata al repository per resettare le impostazioni
        settingSiteRep.resetImpostazioni(userId);
    }
}
