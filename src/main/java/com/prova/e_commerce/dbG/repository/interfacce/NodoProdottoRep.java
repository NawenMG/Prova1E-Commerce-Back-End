package com.prova.e_commerce.dbG.repository.interfacce;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.prova.e_commerce.dbG.model.NodoProdotto;

public interface NodoProdottoRep extends Neo4jRepository<NodoProdotto, Long> {
}
