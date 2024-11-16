package com.prova.e_commerce.dbCol.repository.interfacce;

import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.model.ArchiviazioneSegnalazioni;

import java.util.List;

public interface ArchiviazioneSegnalazioniRep {

    // Metodo per eseguire una query dinamica sulla tabella ArchiviazioneSegnalazioni
    List<ArchiviazioneSegnalazioni> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneSegnalazioni segnalazione);

    // Metodo per salvare una nuova segnalazione
    void saveSegnalazione(ArchiviazioneSegnalazioni segnalazione);

    // Metodo per aggiornare una segnalazione esistente
    void updateSegnalazione(String id, ArchiviazioneSegnalazioni segnalazione);

    // Metodo per eliminare una segnalazione
    void deleteSegnalazione(String id);

    // Metodo per trovare una segnalazione in base al suo ID
    ArchiviazioneSegnalazioni findSegnalazioneById(String id);

    // Metodo per recuperare tutte le segnalazioni
    List<ArchiviazioneSegnalazioni> findAllSegnalazioni();
}
