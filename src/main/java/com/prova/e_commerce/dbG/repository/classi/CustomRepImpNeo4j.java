package com.prova.e_commerce.dbG.repository.classi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbG.repository.interfacce.CustomRepNeo4j;

@Repository
public class CustomRepImpNeo4j implements CustomRepNeo4j {

    @Autowired
    private Neo4jClient neo4jClient;

    @Override
    public void visitaProdotto(Long utenteId, Long prodottoId) {
        neo4jClient.query(
                "MATCH (u:Utente), (p:Prodotto) " +
                "WHERE u.id = $utenteId AND p.id = $prodottoId " +
                "CREATE (u)-[:VISITA]->(p)")
                .bind(utenteId).to("utenteId")
                .bind(prodottoId).to("prodottoId")
                .run();
    }

    @Override
    public void acquistoProdotto(Long utenteId, Long prodottoId) {
        neo4jClient.query(
                "MATCH (u:Utente), (p:Prodotto) " +
                "WHERE u.id = $utenteId AND p.id = $prodottoId " +
                "CREATE (u)-[:ACQUISTA]->(p)")
                .bind(utenteId).to("utenteId")
                .bind(prodottoId).to("prodottoId")
                .run();
    }

    @Override
    public void appartenenzaCategoria(Long prodottoId, String categoriaNome) {
        neo4jClient.query(
                "MATCH (p:Prodotto), (c:CategoriaProdotto) " +
                "WHERE p.id = $prodottoId AND c.nome = $categoriaNome " +
                "CREATE (p)-[:APPARTIENE_A]->(c)")
                .bind(prodottoId).to("prodottoId")
                .bind(categoriaNome).to("categoriaNome")
                .run();
    }

    @Override
    public void provenienzaGeografica(Long utenteId, Long prodottoId) {
        neo4jClient.query(
                "MATCH (u:Utente), (p:Prodotto) " +
                "WHERE u.id = $utenteId AND p.id = $prodottoId " +
                "CREATE (u)-[:PROVIENE_DA]->(p)")
                .bind(utenteId).to("utenteId")
                .bind(prodottoId).to("prodottoId")
                .run();
    }
}
