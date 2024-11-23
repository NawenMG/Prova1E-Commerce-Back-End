package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Resi;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.ResiService;

import jakarta.validation.Valid;

import java.util.List;

@Component
public class ResiResolverGraphql {

    @Autowired
    private ResiService resiService;

    @QueryMapping
    public List<Resi> resi(ParamQuery paramQuery, @Valid Resi resi) {
        return resiService.queryResi(paramQuery, resi);
    }
}
