package com.prova.e_commerce.dbCol.repository.classi;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneTransizioniRep;
import com.prova.e_commerce.security.security1.SecurityUtils;
import com.prova.e_commerce.dbCol.model.ArchiviazioneTransizioni;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class ArchiviazioneTransizioniRepImp implements ArchiviazioneTransizioniRep {

    @Autowired
    private CqlSession session;

    public List<ArchiviazioneTransizioni> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneTransizioni transizione) {
        StringBuilder cql = new StringBuilder("SELECT ");

        // Distinct (Cassandra supporta DISTINCT solo su partizioni intere)
        if (paramQuery.isDistinct()) {
            cql.append("DISTINCT ");
        }

        // Selezione dinamica delle colonne in base agli attributi dell'oggetto 'ArchiviazioneTransizioni'
        if (transizione.getId() != null) {
            cql.append("ID, ");
        }
        if (transizione.getOrderId() != null) {
            cql.append("Order_ID, ");
        }
        if (transizione.getTransizioneDate() != null) {
            cql.append("Transizione_date, ");
        }
        if (transizione.getImportoTotale() != null) {
            cql.append("Importo_totale, ");
        }
        if (transizione.getMetodoDiPagamento() != null) {
            cql.append("Metodo_di_pagamento, ");
        }
        if (transizione.getStatus() != null) {
            cql.append("Status, ");
        }
        if (paramQuery.isAll()) {
            cql.append("* ");
        }

        // Rimuovi l'ultima virgola, se presente
        if (cql.charAt(cql.length() - 2) == ',') {
            cql.deleteCharAt(cql.length() - 2);
        }

        cql.append("FROM ArchiviazioneTransizioni ");

        // Clausole WHERE
        cql.append(paramQuery.buildWhereClause());

        // Controllo del ruolo e aggiunta di filtri
        Set<String> roles = SecurityUtils.getCurrentUserRoles();
        if (roles.contains("UserTransition")) {
            // Nessun filtro aggiuntivo: accesso a tutte le transizioni
        } else {
            throw new SecurityException("Accesso non autorizzato: solo utenti con il ruolo UserTransition possono accedere alle transizioni totali");
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
        List<ArchiviazioneTransizioni> transizioni = new ArrayList<>();
        for (Row row : resultSet) {
            transizioni.add(mapRowToTransizione(row));
        }

        return transizioni;
    }

    private ArchiviazioneTransizioni mapRowToTransizione(Row row) {
        ArchiviazioneTransizioni transizioni = new ArchiviazioneTransizioni();
        transizioni.setId(row.getString("ID"));
        transizioni.setOrderId(row.getString("Order_ID"));
        transizioni.setTransizioneDate(row.getInstant("Transizione_date"));
        transizioni.setImportoTotale(row.getBigDecimal("Importo_totale"));
        transizioni.setMetodoDiPagamento(row.getString("Metodo_di_pagamento"));
        transizioni.setStatus(row.getString("Status"));
        return transizioni;
    }

    public void saveTransizione(ArchiviazioneTransizioni transizione, String UserName) {
        String query = "INSERT INTO ArchiviazioneTransizioni (ID, Order_ID, User_ID, Transizione_date, Importo_totale, " +
                       "Metodo_di_pagamento, Status) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = session.prepare(query);
        session.execute(statement.bind(
            transizione.getId(),
            transizione.getOrderId(),
            UserName,
            transizione.getTransizioneDate(),
            transizione.getImportoTotale(),
            transizione.getMetodoDiPagamento(),
            transizione.getStatus()
        ));
    }

    /* public void updateTransizione(String id, ArchiviazioneTransizioni transizione) {
        String query = "UPDATE ArchiviazioneTransizioni SET Order_ID = ?, User_ID = ?, Transizione_date = ?, Importo_totale = ?, " +
                       "Metodo_di_pagamento = ?, Status = ? WHERE ID = ?";
        PreparedStatement statement = session.prepare(query);
        session.execute(statement.bind(
            transizione.getOrderId(),
            transizione.getUserId(),
            transizione.getTransizioneDate(),
            transizione.getImportoTotale(),
            transizione.getMetodoDiPagamento(),
            transizione.getStatus(),
            id
        ));
    } */

    public void deleteTransizione(String id) {
        String query = "DELETE FROM ArchiviazioneTransizioni WHERE ID = ?";
        PreparedStatement statement = session.prepare(query);
        session.execute(statement.bind(id));
    }

    public ArchiviazioneTransizioni findTransizioneById(String id) {
        String query = "SELECT * FROM ArchiviazioneTransizioni WHERE ID = ?";
        PreparedStatement statement = session.prepare(query);
        Row row = session.execute(statement.bind(id)).one();
        return row != null ? mapRowToTransizione(row) : null;
    }

    public List<ArchiviazioneTransizioni> findAllTransizioni() {
        String query = "SELECT * FROM ArchiviazioneTransizioni";
        ResultSet resultSet = session.execute(SimpleStatement.builder(query).build());
        List<ArchiviazioneTransizioni> transizioni = new ArrayList<>();
        for (Row row : resultSet) {
            transizioni.add(mapRowToTransizione(row));
        }
        return transizioni;
    }
}
