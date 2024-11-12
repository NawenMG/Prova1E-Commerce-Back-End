package com.prova.e_commerce.dbDoc.repository.classi;

import com.prova.e_commerce.dbDoc.entity.SchedeProdotti;
import com.prova.e_commerce.dbDoc.repository.interfacce.SchedeProdottiRepCustom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class SchedeProdottiRepImp implements SchedeProdottiRepCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<SchedeProdotti> findByDynamicCriteria(
        String nome,
        BigDecimal prezzoMin,
        BigDecimal prezzoMax,
        Map<String, String> parametriDescrittivi) {

        Query query = new Query();

        if (nome != null && !nome.isEmpty()) {
            query.addCriteria(Criteria.where("nome").is(nome));
        }
        if (prezzoMin != null) {
            query.addCriteria(Criteria.where("prezzo").gte(prezzoMin));
        }
        if (prezzoMax != null) {
            query.addCriteria(Criteria.where("prezzo").lte(prezzoMax));
        }
        if (parametriDescrittivi != null && !parametriDescrittivi.isEmpty()) {
            parametriDescrittivi.forEach((key, value) ->
                query.addCriteria(Criteria.where("parametriDescrittivi." + key).is(value))
            );
        }

        return mongoTemplate.find(query, SchedeProdotti.class);
    }
}
