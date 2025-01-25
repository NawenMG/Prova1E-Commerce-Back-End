package com.prova.e_commerce.dbCol.controller.graphql;

import com.prova.e_commerce.dbCol.model.ArchiviazioneOrdini;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.service.ArchiviazioneOrdiniService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PreAuthorize("hasAnyRole('USERDELIVERY', 'USER')")
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
    public ArchiviazioneOrdini findOrdineById(@Valid @Argument String id) {
        return archiviazioneOrdiniService.findOrdineById(id);
    }

    // Query dinamica per eseguire query personalizzate sugli ordini
    @QueryMapping
    public List<ArchiviazioneOrdini> queryDinamica(@Valid @Argument ParamQueryCassandra paramQuery, @Valid @Argument ArchiviazioneOrdini ordine) {
        return archiviazioneOrdiniService.queryDinamica(paramQuery, ordine);
    }
}
