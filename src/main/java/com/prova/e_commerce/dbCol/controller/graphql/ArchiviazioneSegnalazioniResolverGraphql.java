package com.prova.e_commerce.dbCol.controller.graphql;

import com.prova.e_commerce.dbCol.model.ArchiviazioneSegnalazioni;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.service.ArchiviazioneSegnalazioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArchiviazioneSegnalazioniResolverGraphql {

    @Autowired
    private ArchiviazioneSegnalazioniService archiviazioneSegnalazioniService;

    // Query per ottenere tutte le segnalazioni
    @QueryMapping
    public List<ArchiviazioneSegnalazioni> findAllSegnalazioni() {
        return archiviazioneSegnalazioniService.findAllSegnalazioni();
    }

    // Query per ottenere una segnalazione per ID
    @QueryMapping
    public ArchiviazioneSegnalazioni findSegnalazioneById(String id) {
        return archiviazioneSegnalazioniService.findSegnalazioneById(id);
    }

    // Query dinamica per eseguire query personalizzate sulle segnalazioni
    @QueryMapping
    public List<ArchiviazioneSegnalazioni> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneSegnalazioni segnalazione) {
        return archiviazioneSegnalazioniService.queryDinamica(paramQuery, segnalazione);
    }
}
