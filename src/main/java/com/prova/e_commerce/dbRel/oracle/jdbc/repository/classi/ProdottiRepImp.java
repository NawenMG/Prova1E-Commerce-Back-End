/* package com.prova.e_commerce.dbRel.oracle.jdbc.repository.classi;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.prova.e_commerce.dbRel.oracle.jdbc.randomData.ProdottiFaker;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Prodotti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.ProdottiRep;
import com.prova.e_commerce.storage.S3Service;

@Repository
public class ProdottiRepImp implements ProdottiRep {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private S3Service s3Service;

    // Query
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
        sql.append("Immagine, "); // Salva il riferimento a S3
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
    if (paramQuery.getAll()) {
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

        // Recupera l'URL dell'immagine salvata nel database
        String s3Key = rs.getString("Immagine");
        if (s3Key != null && !s3Key.isEmpty()) {
            // Converte la chiave in un URL accessibile
            String imageUrl = "https://" + s3Service.getBucketName() + ".s3.amazonaws.com/" + s3Key;
            p.setDescrizione(imageUrl);
        } else {
            p.setDescrizione(null); // Nessuna immagine associata
        }

        p.setAmountAvailable(rs.getInt("Quantità_Disponibile"));
        p.setCategoria(rs.getString("Categoria"));
        p.setDataDiInserimento(rs.getDate("Data_di_inserimento").toLocalDate());
        return p;
    });
}


    public String saveAll(int number) {
    String sql = "INSERT INTO Prodotti (ID, Nome, Prezzo, Descrizione, Immagine, Quantità_Disponibile, Categoria, Data_di_inserimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    ProdottiFaker prodottiFaker = new ProdottiFaker();

    for (int i = 0; i < number; i++) {
        // Generate a fake product
        Prodotti prodotti = prodottiFaker.generateFakeProduct(number);

        // Upload the image to S3 and get the S3 key
        String s3Key = null;
        if (prodotti.getImmagine() != null) {
            try {
                String folder = "prodotti"; // Folder for product images in S3
                MultipartFile imageFile = convertToMultipartFile(prodotti.getImmagine(), "image_" + prodotti.getProductId() + ".jpg");
                s3Key = s3Service.uploadFile(folder, imageFile);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error uploading file to S3: " + e.getMessage();
            }
        }

        // Save the product to the database with the S3 key as the image reference
        jdbcTemplate.update(sql,
                prodotti.getProductId(),
                prodotti.getNome(),
                prodotti.getPrezzo(),
                prodotti.getDescrizione(),
                s3Key, // Save the S3 key instead of the raw image
                prodotti.getAmountAvailable(),
                prodotti.getCategoria(),
                prodotti.getDataDiInserimento()
        );
    }
    return "Dati generati con successo";
}


// Metodo di conversione dei file
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
                return "image/jpeg";
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




    // Insert
public String insertProduct(Prodotti prodotti) {
    // URL o chiave del file su S3
    String imageS3Key = null;

    // Controlla se esiste un'immagine associata al prodotto
    if (prodotti.getImmagine() != null && prodotti.getImmagine().length > 0) {
        // Converte l'immagine in un MultipartFile per il caricamento su S3
        MultipartFile multipartFile = convertToMultipartFile(prodotti.getImmagine(), prodotti.getNome() + "_image");

        try {
            // Carica l'immagine su S3 e ottiene il percorso
            imageS3Key = s3Service.uploadFile("prodotti", multipartFile);
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il caricamento dell'immagine su S3", e);
        }
    }

    // Query SQL per inserire i dati del prodotto
    String sql = "INSERT INTO Prodotti (ID, Nome, Prezzo, Descrizione, Immagine, Quantità_Disponibile, Categoria, Data_di_inserimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    // Esegui l'inserimento nel database
    jdbcTemplate.update(sql,
        prodotti.getProductId(),
        prodotti.getNome(),
        prodotti.getPrezzo(),
        prodotti.getDescrizione(),
        imageS3Key, // Salva la chiave o URL dell'immagine da S3
        prodotti.getAmountAvailable(),
        prodotti.getCategoria(),
        prodotti.getDataDiInserimento()
    );

    return "Prodotto inserito con successo";
}


    // Update
public String updateProduct(int productID, Prodotti prodotti) {
    // URL o chiave del file su S3
    String imageS3Key = null;

    // Controlla se esiste un'immagine associata al prodotto
    if (prodotti.getImmagine() != null && prodotti.getImmagine().length > 0) {
        // Converte l'immagine in un MultipartFile per il caricamento su S3
        MultipartFile multipartFile = convertToMultipartFile(prodotti.getImmagine(), prodotti.getNome() + "_image");

        try {
            // Carica l'immagine su S3 e ottiene il percorso
            imageS3Key = s3Service.uploadFile("prodotti", multipartFile);
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il caricamento dell'immagine su S3", e);
        }
    }

    // Se l'immagine non è stata fornita, lascia invariato il valore nel database
    String sql = "UPDATE Prodotti SET Nome = ?, Prezzo = ?, Descrizione = ?, Immagine = COALESCE(?, Immagine), Quantità_Disponibile = ?, Categoria = ?, Data_di_inserimento = ? WHERE ID = ?";

    // Esegui l'aggiornamento nel database
    jdbcTemplate.update(sql,
        prodotti.getNome(),
        prodotti.getPrezzo(),
        prodotti.getDescrizione(),
        imageS3Key, // Aggiorna la chiave o URL dell'immagine se fornita
        prodotti.getAmountAvailable(),
        prodotti.getCategoria(),
        prodotti.getDataDiInserimento(),
        productID
    );

    return "Prodotto aggiornato con successo";
}


    // Delete
public String deleteProduct(int productID) {
    // Prima di eliminare il prodotto, otteniamo l'immagine associata dal database
    String sqlSelectImage = "SELECT Immagine FROM Prodotti WHERE ID = ?";
    String imageS3Key = jdbcTemplate.queryForObject(sqlSelectImage, String.class, productID);

    // Se esiste un'immagine associata al prodotto, la eliminiamo da S3
    if (imageS3Key != null && !imageS3Key.isEmpty()) {
        try {
            // Elimina il file dal bucket S3
            s3Service.deleteFile(imageS3Key);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la rimozione dell'immagine dal bucket S3", e);
        }
    }

    // Procedi con la cancellazione del prodotto dal database
    String sqlDeleteProduct = "DELETE FROM Prodotti WHERE ID = ?";
    jdbcTemplate.update(sqlDeleteProduct, productID);

    return "Prodotto e immagine eliminati con successo";
}

}
 */

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
            p.setImmagine(rs.getString("Immagine"));
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
            // Genera una categoria fittizia
            Prodotti prodotti = prodottiFaker.generateFakeProduct(number);
    
            // Salva la categoria nel database
            jdbcTemplate.update(sql, prodotti);
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
        String sql = "UPDATE Prodotti SET Nome = ?, Prezzo = ?, Descrizione = ?, Immagine = ?, Quantità_Disponibile = ?, Categoria = ?, Posizione = ?  WHERE ID = ?";
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