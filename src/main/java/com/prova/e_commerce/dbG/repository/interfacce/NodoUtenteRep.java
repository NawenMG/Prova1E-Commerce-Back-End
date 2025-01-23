package com.prova.e_commerce.dbG.repository.interfacce;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.prova.e_commerce.dbG.model.NodoUtente;

public interface NodoUtenteRep extends Neo4jRepository<NodoUtente, Long> {

    NodoUtente save(NodoUtente utente, String currentUsername);
}
