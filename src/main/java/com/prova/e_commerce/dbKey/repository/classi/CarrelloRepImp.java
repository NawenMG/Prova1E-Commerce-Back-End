package com.prova.e_commerce.dbKey.repository.classi;

import com.prova.e_commerce.dbKey.model.Carrello;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.CarrelloRep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Repository
public class CarrelloRepImp implements CarrelloRep {

    @Autowired
    private ArangoDatabase database;

    @Autowired
    private ObjectMapper objectMapper; // Usato per la deserializzazione, Jackson


    // Aggiungi prodotti al carrello
    public void aggiungiProdotti(String userId, List<Prodotto> nuoviProdotti) {
        try {
            BaseDocument carrelloDoc = database.collection("Carrello").getDocument(userId, BaseDocument.class);

            if (carrelloDoc == null) {
                // Se il carrello non esiste, creiamo un nuovo documento
                Carrello carrello = new Carrello();
                carrello.setKey(userId);
                carrello.setProdotti(nuoviProdotti);
                carrello.setQuantitaTotale(nuoviProdotti.stream().mapToInt(Prodotto::getQuantita).sum());
                carrello.setPrezzoTotale(nuoviProdotti.stream().mapToDouble(p -> p.getPrezzoTotale() * p.getQuantita()).sum());

                // Salva il nuovo carrello
                database.collection("Carrello").insertDocument(carrello);
            } else {
                // Se il carrello esiste, aggiorniamo
                Carrello carrello = objectMapper.convertValue(carrelloDoc.getProperties(), Carrello.class);
                List<Prodotto> prodottiEsistenti = carrello.getProdotti();
                prodottiEsistenti.addAll(nuoviProdotti);
                carrello.setProdotti(prodottiEsistenti);
                carrello.setQuantitaTotale(prodottiEsistenti.stream().mapToInt(Prodotto::getQuantita).sum());
                carrello.setPrezzoTotale(prodottiEsistenti.stream().mapToDouble(p -> p.getPrezzoTotale() * p.getQuantita()).sum());

                // Aggiorna il carrello nel database
                database.collection("Carrello").updateDocument(userId, carrello);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Trova carrello per ID utente
    public Optional<Carrello> trovaCarrello(String userId) {
        try {
            BaseDocument carrelloDoc = database.collection("Carrello").getDocument(userId, BaseDocument.class);
            if (carrelloDoc != null) {
                Carrello carrello = objectMapper.convertValue(carrelloDoc.getProperties(), Carrello.class);
                return Optional.of(carrello);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Elimina un prodotto dal carrello
    public void eliminaProdotto(String userId, String prodottoId) {
        try {
            BaseDocument carrelloDoc = database.collection("Carrello").getDocument(userId, BaseDocument.class);
            if (carrelloDoc != null) {
                Carrello carrello = objectMapper.convertValue(carrelloDoc.getProperties(), Carrello.class);
                List<Prodotto> prodotti = carrello.getProdotti();
                prodotti.removeIf(prodotto -> prodotto.getKey().equals(prodottoId)); // Rimuove il prodotto

                // Aggiorna il carrello
                carrello.setQuantitaTotale(prodotti.stream().mapToInt(Prodotto::getQuantita).sum());
                carrello.setPrezzoTotale(prodotti.stream().mapToDouble(p -> p.getPrezzoTotale() * p.getQuantita()).sum());
                carrello.setProdotti(prodotti);

                // Salva il carrello aggiornato
                database.collection("Carrello").updateDocument(userId, carrello);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Resetta tutti i prodotti nel carrello
    public void resetCarrello(String userId) {
        try {
            BaseDocument carrelloDoc = database.collection("Carrello").getDocument(userId, BaseDocument.class);
            if (carrelloDoc != null) {
                Carrello carrello = objectMapper.convertValue(carrelloDoc.getProperties(), Carrello.class);
                carrello.setProdotti(null);
                carrello.setQuantitaTotale(0);
                carrello.setPrezzoTotale(0.0);

                // Salva le modifiche nel carrello
                database.collection("Carrello").updateDocument(userId, carrello);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
