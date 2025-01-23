package com.prova.e_commerce.dbG.repository.interfacce;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.prova.e_commerce.dbG.model.NodoLocazioneUtente;

public interface NodoLocazioneUtenteRep extends Neo4jRepository<NodoLocazioneUtente, Long> {

    NodoLocazioneUtente save(NodoLocazioneUtente locazioneUtente, String currentUsername);
}
