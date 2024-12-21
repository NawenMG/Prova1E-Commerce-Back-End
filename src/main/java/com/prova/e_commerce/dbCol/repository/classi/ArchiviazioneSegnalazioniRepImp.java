/* package com.prova.e_commerce.dbCol.repository.classi;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneSegnalazioniRep;
import com.prova.e_commerce.storage.S3Service;
import com.prova.e_commerce.dbCol.model.ArchiviazioneSegnalazioni;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ArchiviazioneSegnalazioniRepImp implements ArchiviazioneSegnalazioniRep {

    @Autowired
    private CqlSession session;

    @Autowired
    private S3Service s3Service;

    // Query dinamica per ArchiviazioneSegnalazioni
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

    // Assumendo che la colonna "File_multimediali" contenga un array di byte (dati binari)
    ByteBuffer fileMultimedialiBuffer = row.getByteBuffer("File_multimediali");

    if (fileMultimedialiBuffer != null && fileMultimedialiBuffer.remaining() > 0) {
        byte[] fileMultimediali = new byte[fileMultimedialiBuffer.remaining()]; // Otteniamo solo i byte validi
        fileMultimedialiBuffer.get(fileMultimediali); // Copia i byte nel nostro array
        segnalazione.setFileMultimediali(fileMultimediali);
    } else {
        segnalazione.setFileMultimediali(null); // Nessun file multimediale
    }

    return segnalazione;
}



    // Metodo per inserire una segnalazione
    public void saveSegnalazione(ArchiviazioneSegnalazioni segnalazione) {
        // Se esiste un file multimediale, caricalo su S3
        String fileS3Key = null;
        if (segnalazione.getFileMultimediali() != null && segnalazione.getFileMultimediali().length > 0) {
            MultipartFile multipartFile = convertToMultipartFile(segnalazione.getFileMultimediali(), segnalazione.getTitolo() + "_file");

            try {
                // Carica il file su S3 e ottieni il percorso
                fileS3Key = s3Service.uploadFile("segnalazioni", multipartFile);
            } catch (IOException e) {
                throw new RuntimeException("Errore durante il caricamento del file su S3", e);
            }
        }

        // Query SQL per inserire i dati della segnalazione
        String cql = "INSERT INTO ArchiviazioneSegnalazioni (ID, Utente, Riferimento, Data, Titolo, Descrizione, File_multimediali) VALUES (?, ?, ?, ?, ?, ?, ?)";
        session.execute(SimpleStatement.builder(cql)
            .addPositionalValue(segnalazione.getId())
            .addPositionalValue(segnalazione.getUtente())
            .addPositionalValue(segnalazione.getRiferimento())
            .addPositionalValue(segnalazione.getData())
            .addPositionalValue(segnalazione.getTitolo())
            .addPositionalValue(segnalazione.getDescrizione())
            .addPositionalValue(fileS3Key) // Salva la chiave del file S3
            .build());

    }

     // Metodo per aggiornare una segnalazione
     public void updateSegnalazione(String id, ArchiviazioneSegnalazioni segnalazione) {
        // Se esiste un file multimediale, caricalo su S3
        String fileS3Key = null;
        if (segnalazione.getFileMultimediali() != null && segnalazione.getFileMultimediali().length > 0) {
            MultipartFile multipartFile = convertToMultipartFile(segnalazione.getFileMultimediali(), segnalazione.getTitolo() + "_file");

            try {
                // Carica il file su S3 e ottieni il percorso
                fileS3Key = s3Service.uploadFile("segnalazioni", multipartFile);
            } catch (IOException e) {
                throw new RuntimeException("Errore durante il caricamento del file su S3", e);
            }
        }

        // Query SQL per aggiornare i dati della segnalazione
        String cql = "UPDATE ArchiviazioneSegnalazioni SET Utente = ?, Riferimento = ?, Data = ?, Titolo = ?, Descrizione = ?, File_multimediali = ? WHERE ID = ?";
        session.execute(SimpleStatement.builder(cql)
            .addPositionalValue(segnalazione.getUtente())
            .addPositionalValue(segnalazione.getRiferimento())
            .addPositionalValue(segnalazione.getData())
            .addPositionalValue(segnalazione.getTitolo())
            .addPositionalValue(segnalazione.getDescrizione())
            .addPositionalValue(fileS3Key) // Aggiorna la chiave del file S3
            .addPositionalValue(id)
            .build());

    }

    // Metodo per eliminare una segnalazione
    public void deleteSegnalazione(String id) {
        // Prima di eliminare, otteniamo il file associato
        String cqlSelectFile = "SELECT File_multimediali FROM ArchiviazioneSegnalazioni WHERE ID = ?";
        String fileS3Key = session.execute(SimpleStatement.builder(cqlSelectFile)
            .addPositionalValue(id)
            .build()).one().getString("File_multimediali");

        // Se esiste un file associato, elimina il file su S3
        if (fileS3Key != null && !fileS3Key.isEmpty()) {
            s3Service.deleteFile(fileS3Key);
        }

        // Procedi con l'eliminazione della segnalazione
        String cqlDelete = "DELETE FROM ArchiviazioneSegnalazioni WHERE ID = ?";
        session.execute(SimpleStatement.builder(cqlDelete)
            .addPositionalValue(id)
            .build());

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

    // Metodo per la conversione dell'immagine in MultipartFile
    private MultipartFile convertToMultipartFile(byte[] imageBytes, String fileName) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return fileName;
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                return "application/octet-stream";  // Modifica secondo il tipo di file
            }

            @Override
            public boolean isEmpty() {
                return imageBytes == null || imageBytes.length == 0;
            }

            @Override
            public long getSize() {
                return imageBytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return imageBytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(imageBytes);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                try (OutputStream os = new FileOutputStream(dest)) {
                    os.write(imageBytes);
                }
            }
        };
    }
}


 */

 package com.prova.e_commerce.dbCol.repository.classi;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneSegnalazioniRep;
import com.prova.e_commerce.dbCol.model.ArchiviazioneSegnalazioni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ArchiviazioneSegnalazioniRepImp implements ArchiviazioneSegnalazioniRep {

    @Autowired
    private CqlSession session;

    // Query dinamica per ArchiviazioneSegnalazioni
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
    public void saveSegnalazione(ArchiviazioneSegnalazioni segnalazione) {
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
            segnalazione.getUtente(),
            segnalazione.getRiferimento(),
            segnalazione.getData(),
            segnalazione.getTitolo(),
            segnalazione.getDescrizione(),
            fileMultimedialiBuffer // Passaggio del BLOB come ByteBuffer
        ));
    }

    // Aggiornamento di una segnalazione esistente
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
    }

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
