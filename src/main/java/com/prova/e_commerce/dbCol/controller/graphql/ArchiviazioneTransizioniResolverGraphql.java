package com.prova.e_commerce.dbCol.controller.graphql;

import com.prova.e_commerce.dbCol.model.ArchiviazioneTransizioni;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.service.ArchiviazioneTransizioniService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PreAuthorize("hasRole('USERTRANSITION')")
public class ArchiviazioneTransizioniResolverGraphql {

    @Autowired
    private ArchiviazioneTransizioniService archiviazioneTransizioniService;

    // Query per ottenere tutte le transizioni
    @QueryMapping
    public List<ArchiviazioneTransizioni> findAllTransizioni() {
        return archiviazioneTransizioniService.findAllTransizioni();
    }

    // Query per ottenere una transizione per ID
    @QueryMapping
    public ArchiviazioneTransizioni findTransizioneById(String id) {
        return archiviazioneTransizioniService.findTransizioneById(id);
    }

    // Query dinamica per eseguire query personalizzate sulle transizioni
    @QueryMapping
    public List<ArchiviazioneTransizioni> queryDinamica(ParamQueryCassandra paramQuery, @Valid ArchiviazioneTransizioni transizione) {
        return archiviazioneTransizioniService.queryDinamica(paramQuery, transizione);
    }
}
