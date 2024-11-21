package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Ordini;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.OrdiniService;

import java.util.List;

@Component
public class OrdiniResolverGraphql {

    @Autowired
    private OrdiniService ordiniService;

    @QueryMapping
    public List<Ordini> ordini(ParamQuery paramQuery, Ordini ordini) {
        return ordiniService.queryOrdini(paramQuery, ordini);
    }
}
