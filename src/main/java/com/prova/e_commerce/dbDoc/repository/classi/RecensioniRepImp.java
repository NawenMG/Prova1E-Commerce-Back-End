package com.prova.e_commerce.dbDoc.repository.classi;

import com.prova.e_commerce.dbDoc.entity.Recensioni;
import com.prova.e_commerce.dbDoc.parametri.ParamQueryDbDoc;
import com.prova.e_commerce.dbDoc.repository.interfacce.RecensioniRepCustom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecensioniRepImp implements RecensioniRepCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Recensioni> query(ParamQueryDbDoc paramQueryDbDoc, Recensioni recensioni) {

                Query query = new Query();

                // Condizioni dinamiche per la collezione "Recensioni"
                if (recensioni.getUserId() != null) {
                    query.addCriteria(Criteria.where("userId").is(recensioni.getUserId()));
                }
                if (recensioni.getProductId() != null) {
                    query.addCriteria(Criteria.where("productId").is(recensioni.getProductId()));
                }
                if (recensioni.getVoto() != null) {
                    query.addCriteria(Criteria.where("voto").is(recensioni.getVoto()));
                }
                if (recensioni.getTitolo() != null) {
                    query.addCriteria(Criteria.where("titolo").regex(recensioni.getTitolo(), "i")); // Ricerca case-insensitive
                }
                if (recensioni.getLike() != null) {
                    query.addCriteria(Criteria.where("like").is(recensioni.getLike()));
                }
                if (recensioni.getDislike() != null) {
                    query.addCriteria(Criteria.where("dislike").is(recensioni.getDislike()));
                }
        
                // Filtri aggiuntivi definiti in ParamQuery
                if (paramQueryDbDoc.getFilters() != null) {
                    paramQueryDbDoc.getFilters().forEach((field, value) -> 
                        query.addCriteria(Criteria.where(field).is(value))
                    );
                }
        
                // Ordinamento
                if (paramQueryDbDoc.getSortBy() != null) {
                    query.with(paramQueryDbDoc.getOrder().equalsIgnoreCase("DESC") ?
                            org.springframework.data.domain.Sort.by(paramQueryDbDoc.getSortBy()).descending() :
                            org.springframework.data.domain.Sort.by(paramQueryDbDoc.getSortBy()).ascending());
                }
        
                // Paginazione
                if (paramQueryDbDoc.getPage() != null && paramQueryDbDoc.getSize() != null) {
                    query.skip(paramQueryDbDoc.getPage() * paramQueryDbDoc.getSize()).limit(paramQueryDbDoc.getSize());
                }
            
                    return mongoTemplate.find(query, Recensioni.class);
                }
               
    }

