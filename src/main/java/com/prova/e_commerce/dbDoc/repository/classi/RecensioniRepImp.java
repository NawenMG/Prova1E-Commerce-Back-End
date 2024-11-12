package com.prova.e_commerce.dbDoc.repository.classi;

import com.prova.e_commerce.dbDoc.entity.Recensioni;
import com.prova.e_commerce.dbDoc.repository.interfacce.RecensioniRepCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RecensioniRepImp implements RecensioniRepCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Recensioni> findByDynamicCriteria(
            String userId,
            String productId,
            Integer votoMin,
            Integer votoMax,
            String titolo,
            String descrizione,
            Map<String, String> parametriAggiuntivi) {

        // Crea un oggetto Query vuoto
        Query query = new Query();

        // Aggiungi il filtro per "userId" se passato
        if (userId != null && !userId.isEmpty()) {
            query.addCriteria(Criteria.where("User_id").is(userId));
        }

        // Aggiungi il filtro per "productId" se passato
        if (productId != null && !productId.isEmpty()) {
            query.addCriteria(Criteria.where("Product_id").is(productId));
        }

        // Aggiungi il filtro per "voto" (maggiore o uguale a votoMin)
        if (votoMin != null) {
            query.addCriteria(Criteria.where("Voto").gte(votoMin));
        }

        // Aggiungi il filtro per "voto" (minore o uguale a votoMax)
        if (votoMax != null) {
            query.addCriteria(Criteria.where("Voto").lte(votoMax));
        }

        // Aggiungi il filtro per "titolo" (se presente)
        if (titolo != null && !titolo.isEmpty()) {
            query.addCriteria(Criteria.where("Titolo").regex(titolo, "i")); // "i" per case-insensitive
        }

        // Aggiungi il filtro per "descrizione" (se presente)
        if (descrizione != null && !descrizione.isEmpty()) {
            query.addCriteria(Criteria.where("Descrizione").regex(descrizione, "i")); // "i" per case-insensitive
        }

        // Aggiungi i parametri aggiuntivi dinamici (es. like, dislike, ecc.)
        if (parametriAggiuntivi != null && !parametriAggiuntivi.isEmpty()) {
            parametriAggiuntivi.forEach((key, value) -> 
                query.addCriteria(Criteria.where("parametriAggiuntivi." + key).is(value))
            );
        }

        // Esegui la query sul MongoTemplate e restituisci i risultati
        return mongoTemplate.find(query, Recensioni.class);
    }
}
