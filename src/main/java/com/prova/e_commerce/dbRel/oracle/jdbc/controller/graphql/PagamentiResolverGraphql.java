package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.PagamentiService;

import java.util.List;

@Component
public class PagamentiResolverGraphql {

    @Autowired
    private PagamentiService pagamentiService;

    @QueryMapping
    public List<Pagamenti> pagamenti(ParamQuery paramQuery, Pagamenti pagamenti) {
        return pagamentiService.queryPagamenti(paramQuery, pagamenti);
    }
}
