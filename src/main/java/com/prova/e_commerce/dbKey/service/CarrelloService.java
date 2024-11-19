package com.prova.e_commerce.dbKey.service;

import com.prova.e_commerce.dbKey.model.Carrello;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.CarrelloRep;
import com.prova.e_commerce.dbKey.service.CarrelloService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarrelloService {

    @Autowired
    private CarrelloRep carrelloRep;

    public void aggiungiProdotti(String userId, List<Prodotto> nuoviProdotti) {
        if (nuoviProdotti == null || nuoviProdotti.isEmpty()) {
            throw new IllegalArgumentException("La lista dei prodotti non può essere vuota o null.");
        }
        carrelloRep.aggiungiProdotti(userId, nuoviProdotti);
    }

    public Optional<Carrello> getCarrello(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
        }
        return carrelloRep.trovaCarrello(userId);
    }

    public void rimuoviProdotto(String userId, String prodottoId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
        }
        if (prodottoId == null || prodottoId.isEmpty()) {
            throw new IllegalArgumentException("Il prodottoId non può essere null o vuoto.");
        }
        carrelloRep.eliminaProdotto(userId, prodottoId);
    }

    public void svuotaCarrello(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("L'userId non può essere null o vuoto.");
        }
        carrelloRep.resetCarrello(userId);
    }
}
