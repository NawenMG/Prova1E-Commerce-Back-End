package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Prodotti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.ProdottiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProdottiControllerGraphql implements GraphQLQueryResolver{

    @Autowired
    private ProdottiService prodottiService;

    // Query per ottenere ordini
    public List<Prodotti> prodotti(ParamQuery paramQuery, Prodotti prodotti) {
        return prodottiService.queryProdotti(paramQuery, prodotti);
    }
}
