package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Prodotti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.ProdottiService;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProdottiControllerGraphql {
    @Autowired
    private ProdottiService prodottiService;

    public List<Prodotti> prodotti(DataFetchingEnvironment env) {
        // Estrazione degli argomenti dal DataFetchingEnvironment
        ParamQuery paramQuery = env.getArgument("paramQuery");
        Prodotti prodotti = env.getArgument("prodotti");
        return prodottiService.queryProdotti(paramQuery, prodotti);
    }
}
