package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.PagamentiService;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PagamentiControllerGraphql {
    @Autowired
    private PagamentiService pagamentiService;

    public List<Pagamenti> pagamenti(DataFetchingEnvironment env) {
        // Estrazione degli argomenti dal DataFetchingEnvironment
        ParamQuery paramQuery = env.getArgument("paramQuery");
        Pagamenti pagamenti = env.getArgument("pagamenti");
        return pagamentiService.queryPagamenti(paramQuery, pagamenti);
    }
}
