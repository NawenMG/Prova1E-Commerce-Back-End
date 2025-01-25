package com.prova.e_commerce.dbDoc.controller.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import com.prova.e_commerce.dbDoc.entity.Recensioni;
import com.prova.e_commerce.dbDoc.parametri.ParamQueryDbDoc;
import com.prova.e_commerce.dbDoc.service.RecensioniService;

import jakarta.validation.Valid;

import java.util.List;

@Component
public class RecensioniResolverGraphql {

    @Autowired
    private RecensioniService recensioniService;

    @QueryMapping
    public List<Recensioni> recensioni(@Valid @Argument ParamQueryDbDoc paramQuery, @Valid @Argument Recensioni recensioni) {
        return recensioniService.queryDynamic(paramQuery, recensioni);
    }
}
