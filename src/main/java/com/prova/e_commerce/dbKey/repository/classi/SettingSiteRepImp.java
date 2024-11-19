package com.prova.e_commerce.dbKey.repository.classi;

import com.prova.e_commerce.dbKey.model.SettingSite;
import com.prova.e_commerce.dbKey.repository.interfacce.SettingSiteRep;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.Optional;

@Repository
public class SettingSiteRepImp implements SettingSiteRep {

    private final DynamoDbTable<SettingSite> settingsTable;

    // Costruttore per inizializzare il DynamoDbEnhancedClient e il riferimento alla tabella
    public SettingSiteRepImp(DynamoDbClient dynamoDbClient) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        // Inizializza la tabella associata al modello SettingSite
        this.settingsTable = enhancedClient.table("SettingSite", TableSchema.fromBean(SettingSite.class));
    }

    /**
     * Recupera le impostazioni per un utente specifico.
     * @param userId ID dell'utente
     * @return Optional<SettingSite> contenente le impostazioni se trovate, altrimenti vuoto
     */
    @Override
    public Optional<SettingSite> trovaImpostazioni(String userId) {
        try {
            // Recupera il singolo item dalla tabella utilizzando la chiave UserID
            return Optional.ofNullable(settingsTable.getItem(r -> r.key(k -> k.partitionValue(userId))));
        } catch (Exception e) {
            // Gestione dell'errore, log dell'eccezione (puoi aggiungere il logging qui)
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Aggiungi o aggiorna le impostazioni per un utente specifico.
     * @param userId ID dell'utente
     * @param settings Oggetto SettingSite con le nuove impostazioni
     */
    @Override
    public void salvaImpostazioni(String userId, SettingSite settings) {
        try {
            // Associa le impostazioni all'utente e salva o aggiorna la tabella
            settingsTable.putItem(settings);
        } catch (Exception e) {
            // Gestione dell'errore, log dell'eccezione (puoi aggiungere il logging qui)
            e.printStackTrace();
        }
    }

    /**
     * Resetta tutte le impostazioni di un utente, lasciando vuoti i valori.
     * @param userId ID dell'utente per il quale resettare le impostazioni
     */
    @Override
    public void resetImpostazioni(String userId) {
        try {
            // Recupera le impostazioni dell'utente
            SettingSite settings = settingsTable.getItem(r -> r.key(k -> k.partitionValue(userId)));

            if (settings != null) {
                // Resetta tutti i campi delle impostazioni
                settings.setProdottiPerPagina(null);  // reset del valore
                settings.setTema(null);               // reset del valore
                settings.setLayout(null);             // reset del valore
                settings.setLingua(null);             // reset del valore
                settings.setNotifiche(null);          // reset della lista di notifiche

                // Salva le impostazioni resettate
                settingsTable.putItem(settings);
            }
        } catch (Exception e) {
            // Gestione dell'errore, log dell'eccezione (puoi aggiungere il logging qui)
            e.printStackTrace();
        }
    }
}
