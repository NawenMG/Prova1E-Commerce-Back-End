package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Prodotti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.ProdottiService;

import jakarta.validation.Valid;

import java.util.List;

@Component
public class ProdottiResolverGraphql {

    @Autowired
    private ProdottiService prodottiService;

    @QueryMapping
    public List<Prodotti> prodotti(@Valid @Argument ParamQuery paramQuery, @Valid @Argument Prodotti prodotti) {
        return prodottiService.queryProdotti(paramQuery, prodotti);
    }
}
