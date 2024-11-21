package com.prova.e_commerce.dbKey.repository.interfacce;

import com.prova.e_commerce.dbKey.model.SettingSite;

import java.util.Optional;

public interface SettingSiteRep {

    // Recupera le impostazioni del sito per un utente
    Optional<SettingSite> trovaImpostazioni(String userId);

    // Aggiungi o aggiorna le impostazioni del sito per un utente
    void salvaImpostazioni(String userId, SettingSite settings);

    // Resetta le impostazioni per un utente
    void resetImpostazioni(String userId);
}
