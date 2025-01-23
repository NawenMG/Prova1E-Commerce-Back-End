package com.prova.e_commerce.dbG.repository.interfacce;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomRepNeo4j {

    @Query("MATCH (u:Utente), (p:Prodotto) WHERE u.id = $utenteId AND p.id = $prodottoId CREATE (u)-[:VISITA]->(p)")
    void visitaProdotto(Long utenteId, Long prodottoId);

    @Query("MATCH (u:Utente), (p:Prodotto) WHERE u.id = $utenteId AND p.id = $prodottoId CREATE (u)-[:ACQUISTA]->(p)")
    void acquistoProdotto(Long utenteId, Long prodottoId);

    @Query("MATCH (p:Prodotto), (c:CategoriaProdotto) WHERE p.id = $prodottoId AND c.nome = $categoriaNome CREATE (p)-[:APPARTIENE_A]->(c)")
    void appartenenzaCategoria(Long prodottoId, String categoriaNome);

    @Query("MATCH (u:Utente), (p:Prodotto) WHERE u.id = $utenteId AND p.id = $prodottoId CREATE (u)-[:PROVIENE_DA]->(p)")
    void provenienzaGeografica(Long utenteId, Long prodottoId);

    @Query("MATCH (u:Utente)-[:VISITA]->(p:Prodotto) WHERE u.id = $utenteId RETURN p.id AS prodottoId")
    List<String> getVisiteUtente(Long utenteId);

    @Query("MATCH (u:Utente)-[:ACQUISTA]->(p:Prodotto) WHERE u.id = $utenteId RETURN p.id AS prodottoId")
    List<String> getAcquistiUtente(Long utenteId);

    @Query("MATCH (p:Prodotto)-[:APPARTIENE_A]->(c:CategoriaProdotto) WHERE p.id = $prodottoId RETURN c.nome AS categoriaNome")
    List<String> getCategorieProdotto(Long prodottoId);

    @Query("MATCH (u:Utente)-[:PROVIENE_DA]->(loc:Locazione) WHERE u.id = $utenteId RETURN loc.nome AS localita")
    List<String> getProvenienzeUtente(Long utenteId);
}
