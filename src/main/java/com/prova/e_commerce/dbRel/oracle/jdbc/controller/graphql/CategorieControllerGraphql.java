package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Categorie;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.CategorieService;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CategorieControllerGraphql {
    @Autowired
    private CategorieService categorieService;

    public List<Categorie> categorie(DataFetchingEnvironment env) {
        // Estrai gli argomenti dal contesto di DataFetchingEnvironment
        ParamQuery paramQuery = env.getArgument("paramQuery");
        Categorie categorie = env.getArgument("categorie");
        return categorieService.queryCategorie(paramQuery, categorie);
    }
}
