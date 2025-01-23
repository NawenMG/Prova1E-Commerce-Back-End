package com.prova.e_commerce.dbKey.repository.interfacce;

import com.prova.e_commerce.dbKey.model.Carrello;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;

import java.util.List;
import java.util.Optional;

public interface CarrelloRep {

    // Aggiungi prodotti al carrello
    void aggiungiProdotti(String userId, List<Prodotto> nuoviProdotti);

    // Seleziona tutti gli elementi del carrello con la chiave (userId)
    Optional<Carrello> trovaCarrello(String userId);

    // Elimina singolo prodotto dal carrello
    void eliminaProdotto(String userId, String prodottoId);

    // Resetta tutti i prodotti nel carrello
    void resetCarrello(String userId);

    void creaCarrelloVuoto(String userId);
}
