package com.prova.e_commerce.dbCol.controller.graphql;

import com.prova.e_commerce.dbCol.model.ArchiviazioneTransizioni;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.service.ArchiviazioneTransizioniService;

import org.springframework.graphql.data.method.annotation.Argument;
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

    /**
     * Query per ottenere tutte le transizioni.
     * Esempio di schema GraphQL:
     * <pre>
     * type Query {
     *   findAllTransizioni: [ArchiviazioneTransizioni]
     * }
     * </pre>
     *
     * @return la lista di tutte le transizioni
     */
    @QueryMapping
    public List<ArchiviazioneTransizioni> findAllTransizioni() {
        return archiviazioneTransizioniService.findAllTransizioni();
    }

    /**
     * Query per ottenere una transizione per ID.
     * Esempio di schema GraphQL:
     * <pre>
     * type Query {
     *   findTransizioneById(id: String!): ArchiviazioneTransizioni
     * }
     * </pre>
     *
     * @param id L'ID della transizione da cercare
     * @return L'oggetto ArchiviazioneTransizioni corrispondente (o null se non trovato)
     */
    @QueryMapping
    public ArchiviazioneTransizioni findTransizioneById(@Valid @Argument String id) {
        return archiviazioneTransizioniService.findTransizioneById(id);
    }

    /**
     * Query dinamica per eseguire query personalizzate sulle transizioni.
     * Esempio di schema GraphQL:
     * <pre>
     * type Query {
     *   queryDinamica(paramQuery: ParamQueryCassandraInput, transizione: ArchiviazioneTransizioniInput): [ArchiviazioneTransizioni]
     * }
     * </pre>
     *
     * @param paramQuery  parametri generici di ricerca (ad es. filtri e ordering)
     * @param transizione criteri di filtraggio aggiuntivi
     * @return Lista di ArchiviazioneTransizioni che soddisfano i criteri
     */
    @QueryMapping
    public List<ArchiviazioneTransizioni> queryDinamica(@Valid @Argument ParamQueryCassandra paramQuery,
                                                        @Valid @Argument ArchiviazioneTransizioni transizione) {
        return archiviazioneTransizioniService.queryDinamica(paramQuery, transizione);
    }
}
