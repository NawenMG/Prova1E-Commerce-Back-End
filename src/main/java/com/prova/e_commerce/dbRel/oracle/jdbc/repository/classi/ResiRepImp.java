package com.prova.e_commerce.dbRel.oracle.jdbc.repository.classi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.randomData.ResiFaker;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Resi;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.ResiRep;

@Repository
public class ResiRepImp implements ResiRep {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Query
    public List<Resi> query(ParamQuery paramQuery, Resi resi) {
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
         if (resi.getReturnsID() != null) {
            sql.append("ID, ");
        }
        if (resi.getUsersID() != null) {
            sql.append("User_id, ");
        }
        if (resi.isStatus() != false) {
            sql.append("Status, ");
        }
        if (resi.isAccettazioneReso() != false) {
            sql.append("Accetazione_reso, ");
        }
        if (resi.getDataRichiesta() != null) {
            sql.append("Data_di_richiesta, ");
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


        sql.append("FROM Resi");

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
            Resi r = new Resi();
            r.setReturnsID(rs.getString("ID"));
            r.setUsersID(rs.getString("User_id"));
            r.setStatus(rs.getBoolean("Status"));
            r.setAccettazioneReso(rs.getBoolean("Accetazione_reso"));
            r.setDataRichiesta(rs.getDate("Data_di_richiesta").toLocalDate());
            return r;
        });   
    }

    // Per implementare il faker
    public void saveAll(int number) {
        String sql = "INSERT INTO Resi (ID, User_id, Status, Accetazione_reso, Data_di_richiesta) VALUES (?, ?, ?, ?, ?)";
        ResiFaker resiFaker = new ResiFaker();
    
        for (int i = 0; i < number; i++) {
            // Genera una categoria fittizia
            Resi resi = resiFaker.generateFakeReturn(number);
    
            // Salva la categoria nel database
            jdbcTemplate.update(sql, resi);
        }
    }

    // Insert
    public void insertReturn(Resi resi) {
        String sql = "INSERT INTO Resi (ID, User_id, Status, Accetazione_reso, Data_di_richiesta) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
        resi.getReturnsID(),
        resi.getUsersID(),
        resi.isStatus(),
        resi.isAccettazioneReso(),
        resi.getDataRichiesta()
        );
    }

    // Update
    public void updateReturn(int returnID, Resi resi) {
        String sql = "UPDATE Resi SET User_id = ?, Status = ?, Accetazione_reso = ?, Data_di_richiesta = ? WHERE ID = ?";
        jdbcTemplate.update(sql,    
        resi.getUsersID(),
        resi.isStatus(),
        resi.isAccettazioneReso(),
        resi.getDataRichiesta(), 
        returnID
        );
    }

    // Delete
    public void deleteReturn(int returnID) {
        String sql = "DELETE FROM Resi WHERE ID = ?";
        jdbcTemplate.update(sql, returnID);
    }
}
