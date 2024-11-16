package com.prova.e_commerce.dbCol.service;

import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneSegnalazioniRep;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.model.ArchiviazioneSegnalazioni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchiviazioneSegnalazioniService {

    @Autowired
    private ArchiviazioneSegnalazioniRep archiviazioneSegnalazioniRep;

    // Metodo per eseguire una query dinamica sulla tabella ArchiviazioneSegnalazioni
    public List<ArchiviazioneSegnalazioni> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneSegnalazioni segnalazione) {
        return archiviazioneSegnalazioniRep.queryDinamica(paramQuery, segnalazione);
    }

    // Metodo per salvare una nuova segnalazione
    public void saveSegnalazione(ArchiviazioneSegnalazioni segnalazione) {
        archiviazioneSegnalazioniRep.saveSegnalazione(segnalazione);
    }

    // Metodo per aggiornare una segnalazione esistente
    public void updateSegnalazione(String id, ArchiviazioneSegnalazioni segnalazione) {
        archiviazioneSegnalazioniRep.updateSegnalazione(id, segnalazione);
    }

    // Metodo per eliminare una segnalazione
    public void deleteSegnalazione(String id) {
        archiviazioneSegnalazioniRep.deleteSegnalazione(id);
    }

    // Metodo per trovare una segnalazione in base al suo ID
    public ArchiviazioneSegnalazioni findSegnalazioneById(String id) {
        return archiviazioneSegnalazioniRep.findSegnalazioneById(id);
    }

    // Metodo per recuperare tutte le segnalazioni
    public List<ArchiviazioneSegnalazioni> findAllSegnalazioni() {
        return archiviazioneSegnalazioniRep.findAllSegnalazioni();
    }
}
