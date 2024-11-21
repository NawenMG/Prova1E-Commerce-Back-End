package com.prova.e_commerce.dbKey.repository.classi;

import com.prova.e_commerce.dbKey.model.Carrello;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.CarrelloRep;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.Optional;

@Repository
public class CarrelloRepImp implements CarrelloRep {

    private final DynamoDbTable<Carrello> carrelloTable;

    public CarrelloRepImp(DynamoDbClient dynamoDbClient) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        this.carrelloTable = enhancedClient.table("Carrello", TableSchema.fromBean(Carrello.class));
    }

    // Aggiungi prodotti al carrello
    public void aggiungiProdotti(String userId, List<Prodotto> nuoviProdotti) {
        Carrello carrello = carrelloTable.getItem(r -> r.key(k -> k.partitionValue(userId)));

        if (carrello == null) {
            // Se il carrello non esiste, lo crea con i nuovi prodotti
            carrello = new Carrello();
            carrello.setUserId(userId);
            carrello.setProdotti(nuoviProdotti);
            carrello.setQuantitaTotale(nuoviProdotti.stream().mapToInt(Prodotto::getQuantita).sum());
            carrello.setPrezzoTotale(nuoviProdotti.stream().mapToDouble(p -> p.getPrezzoTotale() * p.getQuantita()).sum());
        } else {
            // Aggiorna il carrello esistente aggiungendo i nuovi prodotti
            List<Prodotto> prodottiEsistenti = carrello.getProdotti();
            prodottiEsistenti.addAll(nuoviProdotti);
            carrello.setProdotti(prodottiEsistenti);
            carrello.setQuantitaTotale(
                    prodottiEsistenti.stream().mapToInt(Prodotto::getQuantita).sum()
            );
            carrello.setPrezzoTotale(
                    prodottiEsistenti.stream().mapToDouble(p -> p.getPrezzoTotale() * p.getQuantita()).sum()
            );
        }

        carrelloTable.putItem(carrello);
    }

    // Seleziona tutti gli elementi del carrello con la chiave (userId)
    public Optional<Carrello> trovaCarrello(String userId) {
        return Optional.ofNullable(carrelloTable.getItem(r -> r.key(k -> k.partitionValue(userId))));
    }

    // Elimina singolo prodotto dal carrello
    public void eliminaProdotto(String userId, String prodottoId) {
        Carrello carrello = carrelloTable.getItem(r -> r.key(k -> k.partitionValue(userId)));

        if (carrello != null && carrello.getProdotti() != null) {
            List<Prodotto> prodotti = carrello.getProdotti();
            prodotti.removeIf(prodotto -> prodotto.getProductId().equals(prodottoId)); // Rimuove il prodotto con l'ID specificato

            // Aggiorna quantità totale e prezzo totale
            carrello.setQuantitaTotale(prodotti.stream().mapToInt(Prodotto::getQuantita).sum());
            carrello.setPrezzoTotale(prodotti.stream().mapToDouble(p -> p.getPrezzoTotale() * p.getQuantita()).sum());
            carrello.setProdotti(prodotti);

            carrelloTable.putItem(carrello); // Salva le modifiche
        }
    }

    // Resetta tutti i prodotti nel carrello
    public void resetCarrello(String userId) {
        Carrello carrello = carrelloTable.getItem(r -> r.key(k -> k.partitionValue(userId)));

        if (carrello != null) {
            carrello.setProdotti(null);
            carrello.setQuantitaTotale(0);
            carrello.setPrezzoTotale(0.0);

            carrelloTable.putItem(carrello); // Salva le modifiche
        }
    }
}
