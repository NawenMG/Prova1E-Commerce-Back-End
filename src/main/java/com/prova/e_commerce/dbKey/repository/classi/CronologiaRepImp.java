package com.prova.e_commerce.dbKey.repository.classi;

import com.prova.e_commerce.dbKey.model.Cronologia;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.CronologiaRep;
import org.springframework.stereotype.Repository;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Repository
public class CronologiaRepImp implements CronologiaRep {

    private final ArangoDatabase database;
    private final ObjectMapper objectMapper;

    // Costruttore per inizializzare il client di ArangoDB
    public CronologiaRepImp(ArangoDatabase database) {
        this.database = database;
        this.objectMapper = new ObjectMapper(); // Usato per la deserializzazione
    }

    // Aggiungi nuovi prodotti alla cronologia dell'utente
    @Override
    public void aggiungiDatiCronologici(String userId, List<Prodotto> nuoviProdotti) {
        try {
            // Recupera la cronologia esistente dal database
            BaseDocument cronologiaDoc = database.collection("Cronologia").getDocument(userId, BaseDocument.class);

            Cronologia cronologia;
            if (cronologiaDoc == null) {
                // Se la cronologia non esiste, crea un nuovo oggetto Cronologia
                cronologia = new Cronologia();
                cronologia.setUserId(userId);
                cronologia.setProdotti(nuoviProdotti);
            } else {
                // Se la cronologia esiste, aggiungi i nuovi prodotti
                cronologia = objectMapper.convertValue(cronologiaDoc.getProperties(), Cronologia.class);
                List<Prodotto> prodottiEsistenti = cronologia.getProdotti();
                prodottiEsistenti.addAll(nuoviProdotti);
                cronologia.setProdotti(prodottiEsistenti);
            }

            // Salva o aggiorna la cronologia nel database
            database.collection("Cronologia").replaceDocument(userId, cronologia);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Visualizza la cronologia di un utente
    @Override
    public Optional<Cronologia> visualizzaCronologia(String userId) {
        try {
            // Recupera la cronologia dell'utente
            BaseDocument cronologiaDoc = database.collection("Cronologia").getDocument(userId, BaseDocument.class);
            if (cronologiaDoc != null) {
                Cronologia cronologia = objectMapper.convertValue(cronologiaDoc.getProperties(), Cronologia.class);
                return Optional.of(cronologia);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Elimina un singolo prodotto dalla cronologia dell'utente
    @Override
    public void eliminaSingolaRicerca(String userId, String productId) {
        try {
            // Recupera la cronologia dell'utente
            BaseDocument cronologiaDoc = database.collection("Cronologia").getDocument(userId, BaseDocument.class);
            if (cronologiaDoc != null) {
                Cronologia cronologia = objectMapper.convertValue(cronologiaDoc.getProperties(), Cronologia.class);
                List<Prodotto> prodotti = cronologia.getProdotti();

                // Filtra la lista dei prodotti rimuovendo il prodotto specificato
                prodotti.removeIf(prodotto -> prodotto.getKey().equals(productId));

                // Salva la cronologia aggiornata
                cronologia.setProdotti(prodotti);
                database.collection("Cronologia").replaceDocument(userId, cronologia);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Elimina tutte le ricerche (prodotti) dalla cronologia dell'utente
    @Override
    public void eliminaTutteLeRicerche(String userId) {
        try {
            // Recupera la cronologia dell'utente
            BaseDocument cronologiaDoc = database.collection("Cronologia").getDocument(userId, BaseDocument.class);
            if (cronologiaDoc != null) {
                Cronologia cronologia = objectMapper.convertValue(cronologiaDoc.getProperties(), Cronologia.class);
                cronologia.setProdotti(null);

                // Salva la cronologia aggiornata con prodotti nulli
                database.collection("Cronologia").replaceDocument(userId, cronologia);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
