package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Ordini;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.OrdiniService;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OrdiniControllerGraphql {
    @Autowired
    private OrdiniService ordiniService;

    public List<Ordini> ordini(DataFetchingEnvironment env) {
        // Estrazione degli argomenti dal DataFetchingEnvironment
        ParamQuery paramQuery = env.getArgument("paramQuery");
        Ordini ordini = env.getArgument("ordini");
        return ordiniService.queryOrdini(paramQuery, ordini);
    }
}
