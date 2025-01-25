package com.prova.e_commerce.dbCol.controller.graphql;

import com.prova.e_commerce.dbCol.model.ArchiviazioneResi;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.service.ArchiviazioneResiService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ArchiviazioneResiGraphQLController {

    @Autowired
    private ArchiviazioneResiService archiviazioneResiService;

    /**
     * Recupera un reso specifico tramite ID.
     * @param id L'ID del reso.
     * @return Il reso corrispondente.
     */
    @QueryMapping
    public ArchiviazioneResi getResoByID(@Valid @Argument String id) {
        return archiviazioneResiService.getResoByID(id);
    }

    /**
     * Esegue una query dinamica sui resi.
     * @param paramQuery Parametri della query.
     * @param reso Filtri sul reso.
     * @return La lista di resi filtrati.
     */
    @QueryMapping
    public List<ArchiviazioneResi> queryDinamica(
           @Valid @Argument ParamQueryCassandra paramQuery,
           @Valid @Argument ArchiviazioneResi reso) {
        return archiviazioneResiService.queryDinamica(paramQuery, reso);
    }
}
