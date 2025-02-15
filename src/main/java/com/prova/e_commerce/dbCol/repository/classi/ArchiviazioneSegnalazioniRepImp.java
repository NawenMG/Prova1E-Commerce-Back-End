package com.prova.e_commerce.dbCol.repository.classi;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneSegnalazioniRep;
import com.prova.e_commerce.security.security1.SecurityUtils;
import com.prova.e_commerce.dbCol.model.ArchiviazioneSegnalazioni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class ArchiviazioneSegnalazioniRepImp implements ArchiviazioneSegnalazioniRep {

    @Autowired
    private CqlSession session;

    public List<ArchiviazioneSegnalazioni> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneSegnalazioni segnalazione) {
        StringBuilder cql = new StringBuilder("SELECT ");

        // Distinct (Cassandra supporta DISTINCT solo su partizioni intere)
        if (paramQuery.isDistinct()) {
            cql.append("DISTINCT ");
        }

        // Selezione dinamica delle colonne in base agli attributi dell'oggetto 'ArchiviazioneSegnalazioni'
        if (segnalazione.getId() != null) {
            cql.append("ID, ");
        }
        if (segnalazione.getUtente() != null) {
            cql.append("Utente, ");
        }
        if (segnalazione.getRiferimento() != null) {
            cql.append("Riferimento, ");
        }
        if (segnalazione.getData() != null) {
            cql.append("Data, ");
        }
        if (segnalazione.getTitolo() != null) {
            cql.append("Titolo, ");
        }
        if (segnalazione.getDescrizione() != null) {
            cql.append("Descrizione, ");
        }
        if (segnalazione.getFileMultimediali() != null) {
            cql.append("File_multimediali, ");
        }
        if (paramQuery.isAll()) {
            cql.append("* ");
        }

        // Rimuovi l'ultima virgola, se presente
        if (cql.charAt(cql.length() - 2) == ',') {
            cql.deleteCharAt(cql.length() - 2);
        }

        cql.append("FROM ArchiviazioneSegnalazioni ");

        // Clausole WHERE
        cql.append(paramQuery.buildWhereClause());

        // Controllo del ruolo e aggiunta di filtri
        Set<String> roles = SecurityUtils.getCurrentUserRoles();
        if (roles.contains("Admin")) {
            // Nessun filtro aggiuntivo: accesso a tutte le segnalazioni
        } else if (roles.contains("User")) {
            // Filtro per Utente basato sullo username corrente
            String currentUser = SecurityUtils.getCurrentUsername();
            cql.append(" AND Utente = '").append(currentUser).append("' ");
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
        List<ArchiviazioneSegnalazioni> segnalazioni = new ArrayList<>();
        for (Row row : resultSet) {
            segnalazioni.add(mapRowToSegnalazione(row));
        }

        return segnalazioni;
    }

    // Mappatura della riga a un oggetto ArchiviazioneSegnalazioni
    private ArchiviazioneSegnalazioni mapRowToSegnalazione(Row row) {
        ArchiviazioneSegnalazioni segnalazione = new ArchiviazioneSegnalazioni();
        segnalazione.setId(row.getString("ID"));
        segnalazione.setUtente(row.getString("Utente"));
        segnalazione.setRiferimento(row.getString("Riferimento"));
        segnalazione.setData(row.getInstant("Data"));
        segnalazione.setTitolo(row.getString("Titolo"));
        segnalazione.setDescrizione(row.getString("Descrizione"));

        // Gestione del campo BLOB
        ByteBuffer fileMultimedialiBuffer = row.getByteBuffer("File_multimediali");
        if (fileMultimedialiBuffer != null) {
            byte[] fileMultimediali = new byte[fileMultimedialiBuffer.remaining()];
            fileMultimedialiBuffer.get(fileMultimediali);
            segnalazione.setFileMultimediali(fileMultimediali);
        }

        return segnalazione;
    }
    // Salvataggio di una nuova segnalazione
    public void saveSegnalazione(ArchiviazioneSegnalazioni segnalazione, String UserName) {
        String query = "INSERT INTO ArchiviazioneSegnalazioni (ID, Utente, Riferimento, Data, Titolo, Descrizione, File_multimediali) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = session.prepare(query);
        
        // Convertire il file multimediale in ByteBuffer per il salvataggio
        ByteBuffer fileMultimedialiBuffer = null;
        if (segnalazione.getFileMultimediali() != null) {
            fileMultimedialiBuffer = ByteBuffer.wrap(segnalazione.getFileMultimediali());
        }

        session.execute(statement.bind(
            segnalazione.getId(),
            UserName,
            segnalazione.getRiferimento(),
            segnalazione.getData(),
            segnalazione.getTitolo(),
            segnalazione.getDescrizione(),
            fileMultimedialiBuffer // Passaggio del BLOB come ByteBuffer
        ));
    }

    /* // Aggiornamento di una segnalazione esistente
    public void updateSegnalazione(String id, ArchiviazioneSegnalazioni segnalazione) {
        String query = "UPDATE ArchiviazioneSegnalazioni SET Utente = ?, Riferimento = ?, Data = ?, Titolo = ?, " +
                       "Descrizione = ?, File_multimediali = ? WHERE ID = ?";
        PreparedStatement statement = session.prepare(query);
        
        // Convertire il file multimediale in ByteBuffer per l'aggiornamento
        ByteBuffer fileMultimedialiBuffer = null;
        if (segnalazione.getFileMultimediali() != null) {
            fileMultimedialiBuffer = ByteBuffer.wrap(segnalazione.getFileMultimediali());
        }

        session.execute(statement.bind(
            segnalazione.getUtente(),
            segnalazione.getRiferimento(),
            segnalazione.getData(),
            segnalazione.getTitolo(),
            segnalazione.getDescrizione(),
            fileMultimedialiBuffer, // Passaggio del BLOB come ByteBuffer
            id
        ));
    } */

    // Eliminazione di una segnalazione
    public void deleteSegnalazione(String id) {
        String query = "DELETE FROM ArchiviazioneSegnalazioni WHERE ID = ?";
        PreparedStatement statement = session.prepare(query);
        session.execute(statement.bind(id));
    }

    // Ricerca di una segnalazione per ID
    public ArchiviazioneSegnalazioni findSegnalazioneById(String id) {
        String query = "SELECT * FROM ArchiviazioneSegnalazioni WHERE ID = ?";
        PreparedStatement statement = session.prepare(query);
        Row row = session.execute(statement.bind(id)).one();
        return row != null ? mapRowToSegnalazione(row) : null;
    }

    // Recupero di tutte le segnalazioni
    public List<ArchiviazioneSegnalazioni> findAllSegnalazioni() {
        String query = "SELECT * FROM ArchiviazioneSegnalazioni";
        ResultSet resultSet = session.execute(SimpleStatement.builder(query).build());
        List<ArchiviazioneSegnalazioni> segnalazioni = new ArrayList<>();
        for (Row row : resultSet) {
            segnalazioni.add(mapRowToSegnalazione(row));
        }
        return segnalazioni;
    }
}
