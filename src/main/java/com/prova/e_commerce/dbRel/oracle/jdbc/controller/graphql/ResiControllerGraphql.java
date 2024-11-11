package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Resi;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.ResiService;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ResiControllerGraphql {
    @Autowired
    private ResiService resiService;

    public List<Resi> resi(DataFetchingEnvironment env) {
        // Estrazione degli argomenti dal DataFetchingEnvironment
        ParamQuery paramQuery = env.getArgument("paramQuery");
        Resi resi = env.getArgument("resi");
        return resiService.queryResi(paramQuery, resi);
    }
}
