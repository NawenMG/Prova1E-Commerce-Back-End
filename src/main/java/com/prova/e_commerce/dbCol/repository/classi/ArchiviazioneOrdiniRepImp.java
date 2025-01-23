package com.prova.e_commerce.dbCol.repository.classi;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneOrdiniRep;
import com.prova.e_commerce.security.security1.SecurityUtils;
import com.prova.e_commerce.dbCol.model.ArchiviazioneOrdini;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class ArchiviazioneOrdiniRepImp implements ArchiviazioneOrdiniRep {

    @Autowired
    private CqlSession session;


    public List<ArchiviazioneOrdini> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneOrdini ordine) {
        StringBuilder cql = new StringBuilder("SELECT ");

        // Distinct (Cassandra supporta DISTINCT solo su partizioni intere)
        if (paramQuery.isDistinct()) {
            cql.append("DISTINCT ");
        }

        // Selezione dinamica delle colonne in base agli attributi dell'oggetto 'Ordine'
        if (ordine.getId() != null) {
            cql.append("ID, ");
        }
        if (ordine.getUserId() != null) {
            cql.append("User_ID, ");
        }
        if (ordine.getOrderDate() != null) {
            cql.append("Order_date, ");
        }
        if (ordine.getStatus() != null) {
            cql.append("Status, ");
        }
        if (ordine.getImportoTotale() != null) {
            cql.append("Importo_totale, ");
        }
        if (ordine.getIndirizzoDellaSpedizione() != null) {
            cql.append("Indirizzo_della_spedizione, ");
        }
        if (ordine.getListaArticoliDellOrdine() != null) {
            cql.append("Lista_articoli_dell_ordine, ");
        }
        if (ordine.getConsegna() != null) {
            cql.append("Consegna, ");
        }
        if (ordine.getCorriere() != null) {
            cql.append("Corriere, ");
        }
        if (paramQuery.isAll()) {
            cql.append("* ");
        }

        // Rimuovi l'ultima virgola, se presente
        if (cql.charAt(cql.length() - 2) == ',') {
            cql.deleteCharAt(cql.length() - 2);
        }

        cql.append("FROM ArchiviazioneOrdini ");

        // Clausole WHERE
        cql.append(paramQuery.buildWhereClause());

        // Controllo del ruolo e aggiunta di filtri
        Set<String> roles = SecurityUtils.getCurrentUserRoles();
        if (roles.contains("USERDELIVERY")) {
            // Nessun filtro aggiuntivo: accesso a tutti gli ordini
        } else if (roles.contains("User")) {
            // Filtro per User_ID basato sullo username corrente
            String currentUser = SecurityUtils.getCurrentUsername();
            cql.append(" AND User_ID = '").append(currentUser).append("' ");
        } else {
            throw new SecurityException("Accesso non autorizzato");
        }

        // Clausola ORDER BY
        if (paramQuery.isOrderBy() && !paramQuery.getOrderByColumn().isEmpty()) {
            cql.append("ORDER BY ").append(paramQuery.getOrderByColumn()).append(" ");
        }

        // Allow Filtering (opzionale, necessario se non si rispettano le chiavi di partizione)
        if (paramQuery.isAllowFiltering()) {
            cql.append("ALLOW FILTERING ");
        }

        // Esecuzione della query
        SimpleStatement statement = SimpleStatement.builder(cql.toString()).build();
        ResultSet resultSet = session.execute(statement);

        // Mappatura dei risultati
        List<ArchiviazioneOrdini> ordini = new ArrayList<>();
        for (Row row : resultSet) {
            ordini.add(mapRowToOrdine(row));
        }

        return ordini;
    }

    private ArchiviazioneOrdini mapRowToOrdine(Row row) {
        ArchiviazioneOrdini ordine = new ArchiviazioneOrdini();
        ordine.setId(row.getString("ID"));
        ordine.setUserId(row.getString("User_ID"));
        ordine.setOrderDate(row.getInstant("Order_date"));
        ordine.setStatus(row.getString("Status"));
        ordine.setImportoTotale(row.getBigDecimal("Importo_totale"));
        ordine.setIndirizzoDellaSpedizione(row.getString("Indirizzo_della_spedizione"));
        ordine.setListaArticoliDellOrdine(row.getSet("Lista_articoli_dell_ordine", String.class));
        ordine.setConsegna(row.getInstant("Consegna"));
        ordine.setCorriere(row.getString("Corriere"));
        return ordine;
    }


    public void saveOrdine(ArchiviazioneOrdini ordine, String UserName) {
        String query = "INSERT INTO ArchiviazioneOrdini (ID, User_ID, Order_date, Status, Importo_totale, " +
                       "Indirizzo_della_spedizione, Lista_articoli_dell_ordine, Consegna, Corriere) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = session.prepare(query);
        session.execute(statement.bind(
                ordine.getId(),
                UserName,
                ordine.getOrderDate(),
                ordine.getStatus(),
                ordine.getImportoTotale(),
                ordine.getIndirizzoDellaSpedizione(),
                ordine.getListaArticoliDellOrdine(),
                ordine.getConsegna(),
                ordine.getCorriere()
        ));
    }

     /* // Metodo per aggiornare un ordine
     public void updateOrdine(String id, ArchiviazioneOrdini ordine) {
        String query = "UPDATE ArchiviazioneOrdini SET User_ID = ?, Order_date = ?, Status = ?, Importo_totale = ?, " +
                       "Indirizzo_della_spedizione = ?, Lista_articoli_dell_ordine = ?, Consegna = ?, Corriere = ? " +
                       "WHERE ID = ?";
        PreparedStatement statement = session.prepare(query);
        session.execute(statement.bind(
                ordine.getUserId(),
                ordine.getOrderDate(),
                ordine.getStatus(),
                ordine.getImportoTotale(),
                ordine.getIndirizzoDellaSpedizione(),
                ordine.getListaArticoliDellOrdine(),
                ordine.getConsegna(),
                ordine.getCorriere(),
                id
        ));
    } */

    // Metodo per eliminare un ordine
    public void deleteOrdine(String id) {
        String query = "DELETE FROM ArchiviazioneOrdini WHERE ID = ?";
        PreparedStatement statement = session.prepare(query);
        session.execute(statement.bind(id));
    }


    // Metodo per recuperare un ordine per ID
    public ArchiviazioneOrdini findOrdineById(String id) {
        String query = "SELECT * FROM ArchiviazioneOrdini WHERE ID = ?";
        PreparedStatement statement = session.prepare(query);
        Row row = session.execute(statement.bind(id)).one();
        return row != null ? mapRowToOrdine(row) : null;
    }

    // Metodo per recuperare tutti gli ordini
    public List<ArchiviazioneOrdini> findAllOrdini() {
        String query = "SELECT * FROM ArchiviazioneOrdini";
        ResultSet resultSet = session.execute(SimpleStatement.builder(query).build());
        List<ArchiviazioneOrdini> ordini = new ArrayList<>();
        for (Row row : resultSet) {
            ordini.add(mapRowToOrdine(row));
        }
        return ordini;
    }
}
