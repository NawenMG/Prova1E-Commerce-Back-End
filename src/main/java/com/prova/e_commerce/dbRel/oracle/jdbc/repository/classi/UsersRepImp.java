package com.prova.e_commerce.dbRel.oracle.jdbc.repository.classi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.randomData.UsersFaker;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Users;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.UsersRep;

@Repository
public class UsersRepImp implements UsersRep {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Query
    public List<Users> query(ParamQuery paramQuery, Users users) {
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
         if (users.getUsersID() != null) {
            sql.append("ID, ");
        }
        if (users.getNome() != null) {
            sql.append("Nome, ");
        }
        if (users.getCognome() != null) {
            sql.append("Cognome, ");
        }
        if (users.getRuolo() != null) {
            sql.append("Ruolo, ");
        }
        if (users.getNomeUtente() != null) {
            sql.append("Nome_Utente, ");
        }
        if (users.getEmail() != null) {
            sql.append("Email, ");
        }
        if (users.getPassword() != null) {
            sql.append("Password, ");
        }
        if (users.getImmagine() != null) {
            sql.append("Immagine, ");
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


        sql.append("FROM Users");

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
            Users u = new Users();
            u.setUsersID(rs.getString("ID"));
            u.setNome(rs.getString("Nome"));
            u.setCognome(rs.getString("Cognome"));
            u.setRuolo(rs.getString("Ruolo"));
            u.setNomeUtente(rs.getString("Nome_Utente"));
            u.setEmail(rs.getString("Email"));
            u.setPassword(rs.getString("Password"));
            u.setImmagine(rs.getBytes("Immagine"));
            return u;
        });   
    }

    // Per implementare il faker
    public void saveAll(int number) {
        String sql = "INSERT INTO Users (ID, Nome, Cognome, Ruolo, Nome_Utente, Email, Password, Immagine) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        UsersFaker usersFaker = new UsersFaker();
    
        for (int i = 0; i < number; i++) {
            // Genera una categoria fittizia
            Users users = usersFaker.generateFakeUser(number);
    
            // Salva la categoria nel database
            jdbcTemplate.update(sql, users);
        }
    }

    // Insert
    public void insertUser(Users users) {
        String sql = "INSERT INTO Users (ID, Nome, Cognome, Ruolo, Nome_Utente, Email, Password, Immagine) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
        users.getUsersID(),
        users.getNome(),
        users.getCognome(),
        users.getRuolo(),
        users.getNomeUtente(),
        users.getEmail(),
        users.getPassword(), 
        users.getImmagine()         
        );
    }

    // Update
    public void updateUser(int userID, Users users) {
        String sql = "UPDATE Users SET Nome = ?, Cognome = ?, Ruolo = ?, Nome_Utente = ?, Email = ?, Password = ?, Immagine = ?, Posizione = ?  WHERE ID = ?";
        jdbcTemplate.update(sql, 
        users.getNome(),
        users.getCognome(),
        users.getRuolo(),
        users.getNomeUtente(),
        users.getEmail(),
        users.getPassword(), 
        users.getImmagine(),  
        userID
        );
    }

    // Delete
    public void deleteUser(int userID) {
        String sql = "DELETE FROM Users WHERE ID = ?";
        jdbcTemplate.update(sql, userID);
    }
}
