package com.prova.e_commerce.dbRel.oracle.jdbc.repository.classi;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.randomData.OrdiniFaker;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Ordini;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.OrdiniRep;

@Repository
public class OrdiniRepImp implements OrdiniRep {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Query
    public List<Ordini> query(ParamQuery paramQuery, Ordini ordini) {
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
         if (ordini.getOrderID() != null) {
            sql.append("ID, ");
        }
        if (ordini.getUsersID() != null) {
            sql.append("User_id, ");
        }
        if (ordini.getStatoDiSpedizione() != null) {
            sql.append("Stato_spedizione, ");
        }
        if (ordini.getDataDiConsegna() != null) {
            sql.append("Data_di_consegna, ");
        }
        if (ordini.getDataDiRichiesta() != null) {
            sql.append("Data_di_richiesta, ");
        }
        if (ordini.isAccettazioneOrdine() != false) {
            sql.append("Accetazione_ordine, ");
        }
        if (ordini.getStatus() != null) {
            sql.append("Status, ");
        }
        if (ordini.getCorriere() != null) {
            sql.append("Corriere, ");
        }
        if (ordini.getPosizione() != null) {
            sql.append("Posizione, ");
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


        sql.append("FROM Ordini");

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
            Ordini o = new Ordini();
            o.setOrderID(rs.getString("ID"));
            o.setUsersID(rs.getString("User_id"));
            o.setStatoDiSpedizione(rs.getObject("Stato_spedizione", LocalDateTime.class));
            o.setDataDiConsegna(rs.getDate("Data_di_consegna").toLocalDate());
            o.setDataDiRichiesta(rs.getDate("Data_di_richiesta").toLocalDate());
            o.setAccettazioneOrdine(rs.getBoolean("Accettazione_ordine"));
            o.setStatus(rs.getString("Status"));
            o.setCorriere(rs.getString("Corriere"));
            o.setPosizione(rs.getString("Posizione"));
            return o;
        });   
    }

    // Per implementare il faker
    public String saveAll(int number) {
        String sql = "INSERT INTO Ordini (ID, User_id, Stato_spedizione, Data_di_consegna, Data_di_richiesta, Accettazione_ordine, Status, Corriere, Posizione) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        OrdiniFaker ordiniFaker = new OrdiniFaker();
    
        for (int i = 0; i < number; i++) {
            // Genera una categoria fittizia
            Ordini ordini = ordiniFaker.generateFakeOrder(number);
    
            // Salva la categoria nel database
            jdbcTemplate.update(sql, ordini);
        }
        return "Dati generati con successo";
    }

    // Insert
    public String insertOrdini(Ordini ordini) {
        String sql = "INSERT INTO Ordini (ID, User_id, Stato_spedizione, Data_di_consegna, Data_di_richiesta, Accettazione_ordine, Status, Corriere, Posizione) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
        ordini.getOrderID(),
        ordini.getUsersID(),
        ordini.getStatoDiSpedizione(),
        ordini.getDataDiConsegna(),
        ordini.getDataDiRichiesta(),
        ordini.isAccettazioneOrdine(),
        ordini.getStatus(), 
        ordini.getCorriere(),
        ordini.getPosizione()
        );
        return "Dati generati con successo";
    }

    // Update
    public String updateOrdini(int orderID, Ordini ordini) {
        String sql = "UPDATE Ordini SET User_id = ?, Stato_spedizione = ?, Data_di_consegna = ?, Data_di_richiesta = ?, Accettazione_ordine = ?, Status = ?, Corriere = ?, Posizione = ?  WHERE ID = ?";
        jdbcTemplate.update(sql, 
        ordini.getUsersID(),
        ordini.getStatoDiSpedizione(),
        ordini.getDataDiConsegna(),
        ordini.getDataDiRichiesta(),
        ordini.isAccettazioneOrdine(),
        ordini.getStatus(), 
        ordini.getCorriere(),
        ordini.getPosizione(), 
        orderID
        );
        return "Dati aggiornati con successo";
    }

    // Delete
    public String deleteOrdini(int orderID) {
        String sql = "DELETE FROM Ordini WHERE ID = ?";
        jdbcTemplate.update(sql, orderID);
        return "Dati eliminati con successo";
    }
}
