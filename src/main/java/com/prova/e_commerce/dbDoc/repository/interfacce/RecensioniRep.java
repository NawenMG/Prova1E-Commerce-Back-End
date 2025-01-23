package com.prova.e_commerce.dbDoc.repository.interfacce;

import com.prova.e_commerce.dbDoc.entity.Recensioni;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecensioniRep extends MongoRepository<Recensioni, String> {
    List<Recensioni> findByProductId(String productId);
    List<Recensioni> findByUserId(String userId);
    Recensioni save(Recensioni recensione, String currentUsername);
}
