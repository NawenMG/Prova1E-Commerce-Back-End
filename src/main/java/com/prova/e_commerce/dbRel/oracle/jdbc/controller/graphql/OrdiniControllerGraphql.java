package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Ordini;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.OrdiniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrdiniControllerGraphql implements GraphQLQueryResolver{

    @Autowired
    private OrdiniService ordiniService;

    // Query per ottenere ordini
    public List<Ordini> ordini(ParamQuery paramQuery, Ordini ordine) {
        return ordiniService.queryOrdini(paramQuery, ordine);
    }
}
