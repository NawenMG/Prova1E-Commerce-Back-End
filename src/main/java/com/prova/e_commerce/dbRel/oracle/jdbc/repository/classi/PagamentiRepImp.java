package com.prova.e_commerce.dbRel.oracle.jdbc.repository.classi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.randomData.PagamentiFaker;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.PagamentiRep;

@Repository
public class PagamentiRepImp implements PagamentiRep {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Query
    public List<Pagamenti> query(ParamQuery paramQuery, Pagamenti pagamenti) {
        StringBuilder sql = new StringBuilder("SELECT ");

        // Distinct
        if (paramQuery.isDistinct()) {
            sql.append("DISTINCT ");
        }

        //Operatori di aggregazione
        if(paramQuery.buildAggregationClause() == "MIN(column)"){
            sql.append("MIN(  ");
        }
        if(paramQuery.buildAggregationClause() == "MAX(column)"){
            sql.append("MAX(  ");
        }
        if(paramQuery.buildAggregationClause() == "COUNT(column)"){
            sql.append("COUNT(  ");
        }
        if(paramQuery.buildAggregationClause() == "AVG(column)"){
            sql.append("AVG(  ");
        }
        if(paramQuery.buildAggregationClause() == "SUM(column)"){
            sql.append("SUM(  ");
        }


         // Selezione dinamica delle colonne in base agli attributi dell'oggetto 'Categorie'
         if (pagamenti.getPaymentsID() != null) {
            sql.append("ID, ");
        }
        if (pagamenti.getType() != null) {
            sql.append("Type, ");
        }
        if (pagamenti.getData() != null) {
            sql.append("Data, ");
        }
        if (pagamenti.getStatus() != null) {
            sql.append("Status, ");
        }
        if (pagamenti.getTotal() != null) {
            sql.append("Totale, ");
        }
        if(paramQuery.getAll() != false){
            sql.append("* ");
        }

         // Rimuovi l'ultima virgola, se presente
         if (sql.charAt(sql.length() - 2) == ',') {
            sql.deleteCharAt(sql.length() - 2);
        }

        if(paramQuery.buildAggregationClause() != "column"){
            sql.append(")  ");
        }


        sql.append("FROM Pagamenti");

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
            Pagamenti p = new Pagamenti();
            p.setPaymentsID(rs.getString("ID"));
            p.setType(rs.getString("Type"));
            p.setData(rs.getDate("Data").toLocalDate());
            p.setStatus(rs.getBoolean("Status"));
            p.setTotal(rs.getBigDecimal("Total"));
            return p;
        });   
    }

    // Per implementare il faker
    public String saveAll(int number) {
        String sql = "INSERT INTO Pagamenti (ID, Type, Data, Status, Total) VALUES (?, ?, ?, ?, ?)";
        PagamentiFaker pagamentiFaker = new PagamentiFaker();
    
        for (int i = 0; i < number; i++) {
            // Genera una categoria fittizia
            Pagamenti pagamenti = pagamentiFaker.generateFakePayment(number);
    
            // Salva la categoria nel database
            jdbcTemplate.update(sql, pagamenti);
        }
        return "Dati generati con successo";
    }

    // Insert
    public String insertPayment(Pagamenti pagamenti) {
        String sql = "INSERT INTO Pagamenti (ID, Type, Data, Status, Total) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
        pagamenti.getPaymentsID(),
        pagamenti.getType(),
        pagamenti.getData(),
        pagamenti.getStatus(),
        pagamenti.getTotal()
        );
        return "Dati inseriti con successo";
    }

    // Update
    public String updatePayment(String paymentID, Pagamenti pagamenti) {
        String sql = "UPDATE Pagamenti SET Type = ?, Data = ?, Status = ?, Total = ? WHERE ID = ?";
        jdbcTemplate.update(sql, 
        pagamenti.getType(),
        pagamenti.getData(),
        pagamenti.getStatus(),
        pagamenti.getTotal(),
        paymentID
        );
        return "Dati aggiornati con successo";
    }

    // Delete
    public String deletePayment(String paymentID) {
        String sql = "DELETE FROM Pagamenti WHERE ID = ?";
        jdbcTemplate.update(sql, paymentID);
        return "Dati eliminati con successo";
    }

    
}
