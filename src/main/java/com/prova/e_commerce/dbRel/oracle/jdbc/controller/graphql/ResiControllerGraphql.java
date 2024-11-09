package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Resi;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.ResiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResiControllerGraphql implements GraphQLQueryResolver{

    @Autowired
    private ResiService resiService;

    // Query per ottenere ordini
    public List<Resi> resi(ParamQuery paramQuery, Resi resi) {
        return resiService.queryResi(paramQuery, resi);
    }
}
