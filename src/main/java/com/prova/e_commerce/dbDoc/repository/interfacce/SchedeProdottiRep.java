package com.prova.e_commerce.dbDoc.repository.interfacce;

import com.prova.e_commerce.dbDoc.entity.SchedeProdotti;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SchedeProdottiRep extends MongoRepository<SchedeProdotti, String> {

    // Metodo di query personalizzato per trovare prodotti in base al nome
    List<SchedeProdotti> findByNome(String nome);
    
    // Metodo di query per trovare prodotti con un prezzo inferiore a una certa soglia
    List<SchedeProdotti> findByPrezzoLessThan(BigDecimal prezzo);

    SchedeProdotti save(SchedeProdotti prodotto, String currentUsername);

    
}
