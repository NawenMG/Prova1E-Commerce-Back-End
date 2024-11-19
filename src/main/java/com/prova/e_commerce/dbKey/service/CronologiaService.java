package com.prova.e_commerce.dbKey.service;

import com.prova.e_commerce.dbKey.model.Cronologia;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.CronologiaRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CronologiaService {

    @Autowired
    private CronologiaRep cronologiaRep;

    /**
     * Aggiungi nuovi prodotti alla cronologia dell'utente specificato.
     * 
     * @param userId L'ID dell'utente
     * @param nuoviProdotti La lista di nuovi prodotti da aggiungere
     */
    public void aggiungiDatiCronologici(String userId, List<Prodotto> nuoviProdotti) {
        cronologiaRep.aggiungiDatiCronologici(userId, nuoviProdotti);
    }

    /**
     * Visualizza la cronologia dell'utente specificato.
     * 
     * @param userId L'ID dell'utente
     * @return Un Optional contenente la cronologia dell'utente, se presente
     */
    public Optional<Cronologia> visualizzaCronologia(String userId) {
        return cronologiaRep.visualizzaCronologia(userId);
    }

    /**
     * Elimina un singolo prodotto dalla cronologia dell'utente specificato.
     * 
     * @param userId L'ID dell'utente
     * @param productId L'ID del prodotto da eliminare
     */
    public void eliminaSingolaRicerca(String userId, String productId) {
        cronologiaRep.eliminaSingolaRicerca(userId, productId);
    }

    /**
     * Elimina tutte le ricerche dalla cronologia dell'utente specificato.
     * 
     * @param userId L'ID dell'utente
     */
    public void eliminaTutteLeRicerche(String userId) {
        cronologiaRep.eliminaTutteLeRicerche(userId);
    }
}
