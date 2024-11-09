package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.PagamentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PagamentiControllerGraphql implements GraphQLQueryResolver{

    @Autowired
    private PagamentiService pagamentiService;

    // Query per ottenere ordini
    public List<Pagamenti> pagamenti(ParamQuery paramQuery, Pagamenti pagamenti) {
        return pagamentiService.queryPagamenti(paramQuery, pagamenti);
    }
}
