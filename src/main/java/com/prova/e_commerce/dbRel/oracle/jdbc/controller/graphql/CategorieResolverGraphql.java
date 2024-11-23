package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Categorie;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.CategorieService;

import jakarta.validation.Valid;

import java.util.List;

@Component
public class CategorieResolverGraphql {

    @Autowired
    private CategorieService categorieService;

    @QueryMapping
    public List<Categorie> categorie(ParamQuery paramQuery, @Valid Categorie categorie) {
        return categorieService.queryCategorie(paramQuery, categorie);
    }
}
