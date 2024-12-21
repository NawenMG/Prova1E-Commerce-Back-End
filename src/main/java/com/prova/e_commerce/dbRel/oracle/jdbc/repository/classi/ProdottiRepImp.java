package com.prova.e_commerce.dbRel.oracle.jdbc.repository.classi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.randomData.ProdottiFaker;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Prodotti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.ProdottiRep;

@Repository
public class ProdottiRepImp implements ProdottiRep {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Query
    public List<Prodotti> query(ParamQuery paramQuery, Prodotti prodotti) {
        StringBuilder sql = new StringBuilder("SELECT ");

        // Distinct
        if (paramQuery.isDistinct()) {
            sql.append("DISTINCT ");
        }

        // Operatori di aggregazione
        if (paramQuery.buildAggregationClause().equals("MIN(column)")) {
            sql.append("MIN(  ");
        }
        if (paramQuery.buildAggregationClause().equals("MAX(column)")) {
            sql.append("MAX(  ");
        }
        if (paramQuery.buildAggregationClause().equals("COUNT(column)")) {
            sql.append("COUNT(  ");
        }
        if (paramQuery.buildAggregationClause().equals("AVG(column)")) {
            sql.append("AVG(  ");
        }
        if (paramQuery.buildAggregationClause().equals("SUM(column)")) {
            sql.append("SUM(  ");
        }

        // Selezione dinamica delle colonne in base agli attributi dell'oggetto 'Prodotti'
        if (prodotti.getProductId() != null) {
            sql.append("ID, ");
        }
        if (prodotti.getNome() != null) {
            sql.append("Nome, ");
        }
        if (prodotti.getPrezzo() != null) {
            sql.append("Prezzo, ");
        }
        if (prodotti.getDescrizione() != null) {
            sql.append("Descrizione, ");
        }
        if (prodotti.getImmagine() != null) {
            sql.append("Immagine, ");
        }
        if (prodotti.getAmountAvailable() != 0) {
            sql.append("Quantità_Disponibile, ");
        }
        if (prodotti.getCategoria() != null) {
            sql.append("Categoria, ");
        }
        if (prodotti.getDataDiInserimento() != null) {
            sql.append("Data_di_inserimento, ");
        }
        if (paramQuery.getAll() != false) {
            sql.append("* ");
        }

        // Rimuovi l'ultima virgola, se presente
        if (sql.charAt(sql.length() - 2) == ',') {
            sql.deleteCharAt(sql.length() - 2);
        }

        if (!paramQuery.buildAggregationClause().equals("column")) {
            sql.append(")  ");
        }

        sql.append("FROM Prodotti");

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
            Prodotti p = new Prodotti();
            p.setProductId(rs.getString("ID"));
            p.setNome(rs.getString("Nome"));
            p.setPrezzo(rs.getBigDecimal("Prezzo"));
            p.setDescrizione(rs.getString("Descrizione"));
            p.setImmagine(rs.getBytes("Immagine"));
            p.setAmountAvailable(rs.getInt("Quantità_Disponibile"));
            p.setCategoria(rs.getString("Categoria"));
            p.setDataDiInserimento(rs.getDate("Data_di_inserimento").toLocalDate());
            return p;
        });
    }

    // Per implementare il faker
    public String saveAll(int number) {
        String sql = "INSERT INTO Prodotti (ID, Nome, Prezzo, Descrizione, Immagine, Quantità_Disponibile, Categoria, Data_di_inserimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        ProdottiFaker prodottiFaker = new ProdottiFaker();

        for (int i = 0; i < number; i++) {
            // Genera un prodotto fittizio
            Prodotti prodotti = prodottiFaker.generateFakeProduct(number);

            // Salva il prodotto nel database
            jdbcTemplate.update(sql,
                prodotti.getProductId(),
                prodotti.getNome(),
                prodotti.getPrezzo(),
                prodotti.getDescrizione(),
                prodotti.getImmagine(),
                prodotti.getAmountAvailable(),
                prodotti.getCategoria(),
                prodotti.getDataDiInserimento()
            );
        }
        return "Dati generati con successo";
    }

    // Insert
    public String insertProduct(Prodotti prodotti) {
        String sql = "INSERT INTO Prodotti (ID, Nome, Prezzo, Descrizione, Immagine, Quantità_Disponibile, Categoria, Data_di_inserimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            prodotti.getProductId(),
            prodotti.getNome(),
            prodotti.getPrezzo(),
            prodotti.getDescrizione(),
            prodotti.getImmagine(),
            prodotti.getAmountAvailable(),
            prodotti.getCategoria(),
            prodotti.getDataDiInserimento()
        );
        return "Dati inseriti con successo";
    }

    // Update
    public String updateProduct(String productID, Prodotti prodotti) {
        String sql = "UPDATE Prodotti SET Nome = ?, Prezzo = ?, Descrizione = ?, Immagine = ?, Quantità_Disponibile = ?, Categoria = ?, Posizione = ? WHERE ID = ?";
        jdbcTemplate.update(sql,
            prodotti.getNome(),
            prodotti.getPrezzo(),
            prodotti.getDescrizione(),
            prodotti.getImmagine(),
            prodotti.getAmountAvailable(),
            prodotti.getCategoria(),
            prodotti.getDataDiInserimento(),
            productID
        );
        return "Dati aggiornati con successo";
    }

    // Delete
    public String deleteProduct(String productID) {
        String sql = "DELETE FROM Prodotti WHERE ID = ?";
        jdbcTemplate.update(sql, productID);
        return "Dati eliminati con successo";
    }
}
