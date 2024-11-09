package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Categorie;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategorieControllerGraphql implements GraphQLQueryResolver{

    @Autowired
    private CategorieService categorieService;

    // Query
    public List<Categorie> categorie(ParamQuery paramQuery, Categorie categorie) {
        return categorieService.queryCategorie(paramQuery, categorie);
    }
}
