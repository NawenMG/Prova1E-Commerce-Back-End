package com.prova.e_commerce.dbKey.repository.classi;

import com.prova.e_commerce.dbKey.model.SettingSite;
import com.prova.e_commerce.dbKey.repository.interfacce.SettingSiteRep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.arangodb.ArangoDB;
import com.arangodb.entity.BaseDocument;
import com.prova.e_commerce.dbKey.model.SottoClassi.Notifica;

import java.util.List;
import java.util.Optional;

@Repository
public class SettingSiteRepImp implements SettingSiteRep {

    @Autowired
    private ArangoDB arangoDB;
    private final String databaseName = "eCommerce";
    private final String collectionName = "SettingSite";

    /**
     * Recupera le impostazioni per un utente specifico.
     * @param userId ID dell'utente
     * @return Optional<SettingSite> contenente le impostazioni se trovate, altrimenti vuoto
     */
    @Override
    public Optional<SettingSite> trovaImpostazioni(String userId) {
        try {
            BaseDocument document = arangoDB.db(databaseName)
                    .collection(collectionName)
                    .getDocument(userId, BaseDocument.class);
            if (document != null) {
                SettingSite settings = mapToSettingSite(document);
                return Optional.of(settings);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Aggiungi o aggiorna le impostazioni per un utente specifico.
     * @param userId ID dell'utente
     * @param settings Oggetto SettingSite con le nuove impostazioni
     */
    @Override
    public void salvaImpostazioni(String userId, SettingSite settings) {
        try {
            BaseDocument document = mapToBaseDocument(userId, settings);
            arangoDB.db(databaseName)
                    .collection(collectionName)
                    .insertDocument(document);
        } catch (Exception e) {
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
            BaseDocument document = arangoDB.db(databaseName)
                    .collection(collectionName)
                    .getDocument(userId, BaseDocument.class);

            if (document != null) {
                // Reset dei valori
                document.removeAttribute("prodottiPerPagina");
                document.removeAttribute("tema");
                document.removeAttribute("layout");
                document.removeAttribute("lingua");
                document.removeAttribute("notifiche");

                // Aggiorna il documento resettato
                arangoDB.db(databaseName)
                        .collection(collectionName)
                        .updateDocument(userId, document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mappatura da BaseDocument a SettingSite
    private SettingSite mapToSettingSite(BaseDocument document) {
        SettingSite settings = new SettingSite();
        settings.setUserId(document.getKey());
        settings.setProdottiPerPagina((Integer) document.getAttribute("prodottiPerPagina"));
        settings.setTema((String) document.getAttribute("tema"));
        settings.setLayout((String) document.getAttribute("layout"));
        settings.setLingua((String) document.getAttribute("lingua"));
        settings.setNotifiche((List<Notifica>) document.getAttribute("notifiche"));
        return settings;
    }

    // Mappatura da SettingSite a BaseDocument
    private BaseDocument mapToBaseDocument(String userId, SettingSite settings) {
        BaseDocument document = new BaseDocument(userId);
        document.addAttribute("prodottiPerPagina", settings.getProdottiPerPagina());
        document.addAttribute("tema", settings.getTema());
        document.addAttribute("layout", settings.getLayout());
        document.addAttribute("lingua", settings.getLingua());
        document.addAttribute("notifiche", settings.getNotifiche());
        return document;
    }
}
