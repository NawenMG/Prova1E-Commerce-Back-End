package com.prova.e_commerce.dbRel.oracle.jdbc.repository.classi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.randomData.CategorieFaker;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Categorie;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.CategorieRep;
import org.springframework.beans.factory.annotation.Qualifier;

@Repository
public class CategorieRepImp implements CategorieRep {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Iniezione tramite costruttore con qualificatore
    public CategorieRepImp(@Qualifier("jdbcTemplateLocal") JdbcTemplate jdbcTemplate,
                           NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    // Query
    @Override
    public List<Categorie> query(ParamQuery paramQuery, Categorie categorie) {
        StringBuilder sql = new StringBuilder("SELECT ");

        // Distinct
        if (paramQuery.isDistinct()) {
            sql.append("DISTINCT ");
        }

        //Operatori di aggregazione
        if(paramQuery.buildAggregationClause() != null) {
            if(paramQuery.buildAggregationClause().equals("MIN(column)")) {
                sql.append("MIN(  ");
            } else if(paramQuery.buildAggregationClause().equals("MAX(column)")) {
                sql.append("MAX(  ");
            } else if(paramQuery.buildAggregationClause().equals("COUNT(column)")) {
                sql.append("COUNT(  ");
            } else if(paramQuery.buildAggregationClause().equals("AVG(column)")) {
                sql.append("AVG(  ");
            } else if(paramQuery.buildAggregationClause().equals("SUM(column)")) {
                sql.append("SUM(  ");
            }
        }

        // Selezione dinamica delle colonne in base agli attributi dell'oggetto 'Categorie'
        if (categorie.getCategoryID() != null) {
            sql.append("ID, ");
        }
        if (categorie.getName() != null) {
            sql.append("Nome, ");
        }
        if (paramQuery.getAll() != false){
            sql.append("* ");
        }

        // Rimuovi l'ultima virgola, se presente
        if (sql.charAt(sql.length() - 2) == ',') {
            sql.deleteCharAt(sql.length() - 2);
        }

        if(paramQuery.buildAggregationClause() != null && !paramQuery.buildAggregationClause().equals("column")){
            sql.append(")  ");
        }

        sql.append("FROM Categorie");

        // Condizioni WHERE
        sql.append(paramQuery.buildWhereClause());

        // Ordinamento (ORDER BY)
        sql.append(paramQuery.buildOrderByClause());

        // Paginazione: OFFSET e LIMIT
        if (paramQuery.getOffset() > 0) {
            sql.append("OFFSET ").append(paramQuery.getOffset()).append(" ROWS ");
        }

        if (paramQuery.getLimit() > 0) {
            sql.append("FETCH FIRST ").append(paramQuery.getLimit()).append(" ROWS ONLY ");
        }

        // Esecuzione della query
        Map<String, Object> params = new HashMap<>();
        // Aggiungi qui la logica per popolare 'params' se necessario

        return namedParameterJdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> {
            Categorie c = new Categorie();
            c.setCategoryID(rs.getString("ID"));
            c.setName(rs.getString("Nome"));
            return c;
        });
    }

    // Per implementare il faker
    @Override
    public String saveAll(int number) {
        String sql = "INSERT INTO Categorie (ID, Nome) VALUES (?, ?)";
        CategorieFaker categorieFaker = new CategorieFaker();
    
        for (int i = 0; i < number; i++) {
            // Genera una categoria fittizia
            Categorie categorie = categorieFaker.generateFakeCategory(number);
    
            // Salva la categoria nel database
            jdbcTemplate.update(sql, categorie.getCategoryID(), categorie.getName());
        }
        return "Dati generati con successo";
    }

    // Insert
    @Override
    public String insertCategory(Categorie categorie) {
        String sql = "INSERT INTO Categorie (ID, Nome) VALUES (?, ?)";
        jdbcTemplate.update(sql,
        categorie.getCategoryID(),
        categorie.getName());
        return "Dati inseriti con successo";
    }

    // Update
    @Override
    public String updateCategory(String categoryID, Categorie categorie) {
        String sql = "UPDATE Categorie SET Nome = ? WHERE ID = ?";
        jdbcTemplate.update(sql, 
        categorie.getName(),
        categoryID);
        return "Dati aggiornati con successo";
    }

    // Delete
    @Override
    public String deleteCategory(String categoryID) {
        String sql = "DELETE FROM Categorie WHERE ID = ?";
        jdbcTemplate.update(sql, categoryID);
        return "Dati eliminati con successo";
    }
}
