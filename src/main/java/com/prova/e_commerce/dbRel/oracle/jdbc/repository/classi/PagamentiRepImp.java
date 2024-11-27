package com.prova.e_commerce.dbRel.oracle.jdbc.repository.classi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.randomData.PagamentiFaker;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.PagamentiRep;
import org.springframework.beans.factory.annotation.Qualifier;

@Repository
public class PagamentiRepImp implements PagamentiRep {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Iniezione tramite costruttore con qualificatore
    public PagamentiRepImp(@Qualifier("jdbcTemplateLocal") JdbcTemplate jdbcTemplate,
                           NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    // Query
    @Override
    public List<Pagamenti> query(ParamQuery paramQuery, Pagamenti pagamenti) {
        StringBuilder sql = new StringBuilder("SELECT ");

        // Distinct
        if (paramQuery.isDistinct()) {
            sql.append("DISTINCT ");
        }

        // Operatori di aggregazione
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

        // Selezione dinamica delle colonne in base agli attributi dell'oggetto 'Pagamenti'
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

        if(paramQuery.buildAggregationClause() != null && !paramQuery.buildAggregationClause().equals("column")){
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
            p.setTotal(rs.getBigDecimal("Totale"));
            return p;
        });
    }

    // Per implementare il faker
    @Override
    public String saveAll(int number) {
        String sql = "INSERT INTO Pagamenti (ID, Type, Data, Status, Total) VALUES (?, ?, ?, ?, ?)";
        PagamentiFaker pagamentiFaker = new PagamentiFaker();
    
        for (int i = 0; i < number; i++) {
            // Genera un pagamento fittizio
            Pagamenti pagamenti = pagamentiFaker.generateFakePayment(number);
    
            // Salva il pagamento nel database
            jdbcTemplate.update(sql, 
                pagamenti.getPaymentsID(),
                pagamenti.getType(),
                pagamenti.getData(),
                pagamenti.getStatus(),
                pagamenti.getTotal());
        }
        return "Dati generati con successo";
    }

    // Insert
    @Override
    public String insertPayment(Pagamenti pagamenti) {
        String sql = "INSERT INTO Pagamenti (ID, Type, Data, Status, Total) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
        pagamenti.getPaymentsID(),
        pagamenti.getType(),
        pagamenti.getData(),
        pagamenti.getStatus(),
        pagamenti.getTotal());
        return "Dati inseriti con successo";
    }

    // Update
    @Override
    public String updatePayment(String paymentID, Pagamenti pagamenti) {
        String sql = "UPDATE Pagamenti SET Type = ?, Data = ?, Status = ?, Total = ? WHERE ID = ?";
        jdbcTemplate.update(sql, 
        pagamenti.getType(),
        pagamenti.getData(),
        pagamenti.getStatus(),
        pagamenti.getTotal(),
        paymentID);
        return "Dati aggiornati con successo";
    }

    // Delete
    @Override
    public String deletePayment(String paymentID) {
        String sql = "DELETE FROM Pagamenti WHERE ID = ?";
        jdbcTemplate.update(sql, paymentID);
        return "Dati eliminati con successo";
    }


    @Override
    public Pagamenti findByPaymentId(String paymentId) {
    String sql = "SELECT * FROM Pagamenti WHERE ID = :paymentId";

    // Parametri per la query
    Map<String, Object> params = new HashMap<>();
    params.put("paymentId", paymentId);

    // Esegui la query con NamedParameterJdbcTemplate
    return namedParameterJdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
        Pagamenti pagamenti = new Pagamenti();
        pagamenti.setPaymentsID(rs.getString("ID"));
        pagamenti.setType(rs.getString("Type"));
        pagamenti.setData(rs.getDate("Data").toLocalDate());
        pagamenti.setStatus(rs.getBoolean("Status"));
        pagamenti.setTotal(rs.getBigDecimal("Totale"));
        return pagamenti;
    });
}

}


