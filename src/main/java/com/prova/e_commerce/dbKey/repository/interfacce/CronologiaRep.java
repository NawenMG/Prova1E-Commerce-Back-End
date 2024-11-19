package com.prova.e_commerce.dbKey.repository.interfacce;

import com.prova.e_commerce.dbKey.model.Cronologia;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;

import java.util.List;
import java.util.Optional;

public interface CronologiaRep {

    // Aggiungi nuovi prodotti alla cronologia dell'utente
    void aggiungiDatiCronologici(String userId, List<Prodotto> nuoviProdotti);

    // Visualizza la cronologia di un utente
    Optional<Cronologia> visualizzaCronologia(String userId);

    // Elimina un singolo prodotto dalla cronologia dell'utente
    void eliminaSingolaRicerca(String userId, String productId);

    // Elimina tutte le ricerche (prodotti) dalla cronologia dell'utente
    void eliminaTutteLeRicerche(String userId);
}
