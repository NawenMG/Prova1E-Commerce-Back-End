package com.prova.e_commerce.dbDoc.repository.classi;

import com.prova.e_commerce.dbDoc.entity.SchedeProdotti;
import com.prova.e_commerce.dbDoc.parametri.ParamQueryDbDoc;
import com.prova.e_commerce.dbDoc.repository.interfacce.SchedeProdottiRepCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SchedeProdottiRepImp implements SchedeProdottiRepCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<SchedeProdotti> query(ParamQueryDbDoc paramQuery, SchedeProdotti schedeProdotti) {
        Query query = new Query();

        // Condizioni dinamiche basate sui campi dell'entity "SchedeProdotti"
        if (schedeProdotti.getNome() != null) {
            query.addCriteria(Criteria.where("nome").regex(schedeProdotti.getNome(), "i")); // Case-insensitive
        }
        if (schedeProdotti.getPrezzo() != null) {
            query.addCriteria(Criteria.where("prezzo").is(schedeProdotti.getPrezzo()));
        }
        if (schedeProdotti.getParametriDescrittivi() != null && !schedeProdotti.getParametriDescrittivi().isEmpty()) {
            schedeProdotti.getParametriDescrittivi().forEach((key, value) -> 
                query.addCriteria(Criteria.where("parametriDescrittivi." + key).is(value))
            );
        }

        // Filtri aggiuntivi definiti in ParamQuery
        if (paramQuery.getFilters() != null) {
            paramQuery.getFilters().forEach((field, value) -> 
                query.addCriteria(Criteria.where(field).is(value))
            );
        }

        // Ordinamento
        if (paramQuery.getSortBy() != null) {
            query.with(paramQuery.getOrder().equalsIgnoreCase("DESC") ?
                    org.springframework.data.domain.Sort.by(paramQuery.getSortBy()).descending() :
                    org.springframework.data.domain.Sort.by(paramQuery.getSortBy()).ascending());
        }

        // Paginazione
        if (paramQuery.getPage() != null && paramQuery.getSize() != null) {
            query.skip(paramQuery.getPage() * paramQuery.getSize()).limit(paramQuery.getSize());
        }

        // Esegui la query e restituisci i risultati
        return mongoTemplate.find(query, SchedeProdotti.class);
    }
}
