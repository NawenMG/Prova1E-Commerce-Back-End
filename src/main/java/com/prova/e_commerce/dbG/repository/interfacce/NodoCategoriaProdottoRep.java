package com.prova.e_commerce.dbG.repository.interfacce;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.prova.e_commerce.dbG.model.NodoCategoriaProdotto;

public interface NodoCategoriaProdottoRep extends Neo4jRepository<NodoCategoriaProdotto, Long> {

    NodoCategoriaProdotto save(NodoCategoriaProdotto categoriaProdotto, String currentUsername);
}
