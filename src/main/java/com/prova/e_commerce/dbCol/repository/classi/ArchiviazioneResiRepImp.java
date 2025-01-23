package com.prova.e_commerce.dbCol.repository.classi;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneResiRep;
import com.prova.e_commerce.security.security1.SecurityUtils;
import com.prova.e_commerce.dbCol.model.ArchiviazioneResi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class ArchiviazioneResiRepImp implements ArchiviazioneResiRep {

    @Autowired
    private CqlSession session;


    public List<ArchiviazioneResi> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneResi resi) {
        StringBuilder cql = new StringBuilder("SELECT ");

        // Distinct (Cassandra supporta DISTINCT solo su partizioni intere)
        if (paramQuery.isDistinct()) {
            cql.append("DISTINCT ");
        }

        // Selezione dinamica delle colonne in base agli attributi dell'oggetto 'Ordine'
        if (resi.getReturnsID() != null) {
            cql.append("ReturnsID, ");
        }
        if (resi.getUserID() != null) {
            cql.append("User_ID, ");
        }
        if (resi.getStatus() != null) {
            cql.append("Status, ");
        }
        if (resi.getAccettazioneReso() != null) {
            cql.append("AccettazioneReso, ");
        }
        if (resi.getDataRichiesta() != null) {
            cql.append("DataRichiesta, ");
        }
        if (paramQuery.isAll()) {
            cql.append("* ");
        }

        // Rimuovi l'ultima virgola, se presente
        if (cql.charAt(cql.length() - 2) == ',') {
            cql.deleteCharAt(cql.length() - 2);
        }

        cql.append("FROM ArchiviazioneResi ");

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
        List<ArchiviazioneResi> reso = new ArrayList<>();
        for (Row row : resultSet) {
            reso.add(mapRowToReso(row));
        }

        return reso;
    }

    private ArchiviazioneResi mapRowToReso(Row row) {
        ArchiviazioneResi reso = new ArchiviazioneResi();
        reso.setReturnsID(row.getString("ReturnsID"));
        reso.setUserID(row.getString("User_ID"));
        reso.setStatus(row.getBoolean("Status"));
        reso.setAccettazioneReso(row.getBoolean("AccettazioneReso"));
        reso.setDataRichiesta(row.getLocalDate("DataRichiesta"));
        return reso;
    }


    public void saveReso(ArchiviazioneResi resi, String UserName) {
        String query = "INSERT INTO ArchiviazioneResi (ReturnsID, User_ID, Status, AccettazioneReso, DataRichiesta, " +
                       "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = session.prepare(query);
        session.execute(statement.bind(
                resi.getReturnsID(),
                UserName,
                resi.getStatus(),
                false,
                resi.getDataRichiesta()
        ));
    }

      // Metodo per aggiornare un ordine
     public void updateReso(String id, ArchiviazioneResi resi) {
        String query = "UPDATE ArchiviazioneResi SET AccettazioneReso = ? " +
                       "WHERE ReturnsID = ?";
        PreparedStatement statement = session.prepare(query);
        session.execute(statement.bind(
            resi.getStatus(),
            id
                
        ));
    } 

    // Metodo per eliminare un ordine
    public void deleteReso(String id) {
        String query = "DELETE FROM ArchiviazioneResi WHERE ReturnsID = ?";
        PreparedStatement statement = session.prepare(query);
        session.execute(statement.bind(id));
    }


    // Metodo per recuperare un ordine per ID
    public ArchiviazioneResi findResoByID(String id) {
        String query = "SELECT * FROM ArchiviazioneResi WHERE ReturnsID = ?";
        PreparedStatement statement = session.prepare(query);
        Row row = session.execute(statement.bind(id)).one();
        return row != null ? mapRowToReso(row) : null;
    }

    // Metodo per recuperare tutti gli ordini
    public List<ArchiviazioneResi> findAllOrdini() {
        String query = "SELECT * FROM ArchiviazioneResi";
        ResultSet resultSet = session.execute(SimpleStatement.builder(query).build());
        List<ArchiviazioneResi> reso = new ArrayList<>();
        for (Row row : resultSet) {
            reso.add(mapRowToReso(row));
        }
        return reso;
    }
}
