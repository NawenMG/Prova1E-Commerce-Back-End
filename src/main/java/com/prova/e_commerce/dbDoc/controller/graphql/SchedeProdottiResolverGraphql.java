package com.prova.e_commerce.dbDoc.controller.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import com.prova.e_commerce.dbDoc.entity.SchedeProdotti;
import com.prova.e_commerce.dbDoc.parametri.ParamQueryDbDoc;
import com.prova.e_commerce.dbDoc.service.SchedeProdottiService;


import java.util.List;

@Component
public class SchedeProdottiResolverGraphql {

    @Autowired
    private SchedeProdottiService schedeProdottiService;

    @QueryMapping
    public List<SchedeProdotti> schedeprodotti(ParamQueryDbDoc paramQuery, SchedeProdotti schedeProdotti) {
        return schedeProdottiService.queryDynamic(paramQuery, schedeProdotti);
    }
}
