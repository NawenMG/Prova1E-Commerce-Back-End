package com.prova.e_commerce.dbKey.repository.classi;

import com.prova.e_commerce.dbKey.model.Cronologia;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.repository.interfacce.CronologiaRep;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.Optional;

@Repository
public class CronologiaRepImp implements CronologiaRep {

    private final DynamoDbTable<Cronologia> cronologiaTable;

    // Costruttore per inizializzare il client DynamoDB
    public CronologiaRepImp(DynamoDbClient dynamoDbClient) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        this.cronologiaTable = enhancedClient.table("Cronologia", TableSchema.fromBean(Cronologia.class));
    }

    // Aggiungi nuovi prodotti alla cronologia dell'utente
    @Override
    public void aggiungiDatiCronologici(String userId, List<Prodotto> nuoviProdotti) {
        // Recupera la cronologia esistente
        Cronologia cronologia = cronologiaTable.getItem(r -> r.key(k -> k.partitionValue(userId)));

        if (cronologia == null) {
            // Se la cronologia non esiste, crea un nuovo oggetto Cronologia
            cronologia = new Cronologia();
            cronologia.setUserId(userId);
            cronologia.setProdotti(nuoviProdotti);
        } else {
            // Aggiungi i nuovi prodotti alla cronologia esistente
            List<Prodotto> prodottiEsistenti = cronologia.getProdotti();
            prodottiEsistenti.addAll(nuoviProdotti);
            cronologia.setProdotti(prodottiEsistenti);
        }

        // Salva la cronologia aggiornata
        cronologiaTable.putItem(cronologia);
    }

    // Visualizza la cronologia di un utente
    @Override
    public Optional<Cronologia> visualizzaCronologia(String userId) {
        // Recupera la cronologia dell'utente
        Cronologia cronologia = cronologiaTable.getItem(r -> r.key(k -> k.partitionValue(userId)));
        return Optional.ofNullable(cronologia);
    }

    // Elimina un singolo prodotto dalla cronologia dell'utente
    @Override
    public void eliminaSingolaRicerca(String userId, String productId) {
        // Recupera la cronologia dell'utente
        Cronologia cronologia = cronologiaTable.getItem(r -> r.key(k -> k.partitionValue(userId)));

        if (cronologia != null) {
            // Filtra la lista dei prodotti rimuovendo il prodotto specificato
            List<Prodotto> prodotti = cronologia.getProdotti();
            prodotti.removeIf(prodotto -> prodotto.getProductId().equals(productId));

            // Salva la cronologia aggiornata
            cronologia.setProdotti(prodotti);
            cronologiaTable.putItem(cronologia);
        }
    }

    // Elimina tutte le ricerche (prodotti) dalla cronologia dell'utente
    @Override
    public void eliminaTutteLeRicerche(String userId) {
        // Recupera la cronologia dell'utente
        Cronologia cronologia = cronologiaTable.getItem(r -> r.key(k -> k.partitionValue(userId)));

        if (cronologia != null) {
            // Resetta la lista dei prodotti (rimuovendo tutte le ricerche)
            cronologia.setProdotti(null);
            cronologiaTable.putItem(cronologia);
        }
    }
}
