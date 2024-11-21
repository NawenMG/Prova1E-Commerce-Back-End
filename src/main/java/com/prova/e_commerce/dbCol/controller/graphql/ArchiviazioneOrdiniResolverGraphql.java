package com.prova.e_commerce.dbCol.controller.graphql;

import com.prova.e_commerce.dbCol.model.ArchiviazioneOrdini;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.service.ArchiviazioneOrdiniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArchiviazioneOrdiniResolverGraphql {

    @Autowired
    private ArchiviazioneOrdiniService archiviazioneOrdiniService;

    // Query per ottenere tutti gli ordini
    @QueryMapping
    public List<ArchiviazioneOrdini> findAllOrdini() {
        return archiviazioneOrdiniService.findAllOrdini();
    }

    // Query per ottenere un ordine per ID
    @QueryMapping
    public ArchiviazioneOrdini findOrdineById(String id) {
        return archiviazioneOrdiniService.findOrdineById(id);
    }

    // Query dinamica per eseguire query personalizzate sugli ordini
    @QueryMapping
    public List<ArchiviazioneOrdini> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneOrdini ordine) {
        return archiviazioneOrdiniService.queryDinamica(paramQuery, ordine);
    }
}
