package com.prova.e_commerce.dbCol.controller.rest;

import com.prova.e_commerce.dbCol.model.ArchiviazioneSegnalazioni;
import com.prova.e_commerce.dbCol.service.ArchiviazioneSegnalazioniService;

import jakarta.validation.Valid;

import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
/* import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream; */
import java.util.List;

@RestController
@RequestMapping("/api/segnalazioni")
public class ArchiviazioneSegnalazioniControllerRest {

    @Autowired
    private ArchiviazioneSegnalazioniService archiviazioneSegnalazioniService;

    /**
     * Metodo per eseguire una query dinamica sulle segnalazioni.
     */
    @PostMapping("/query")
    public ResponseEntity<List<ArchiviazioneSegnalazioni>> queryDinamica(@RequestBody ParamQueryCassandra paramQuery,
                                                                         @Valid  @RequestBody ArchiviazioneSegnalazioni segnalazione) {
        List<ArchiviazioneSegnalazioni> result = archiviazioneSegnalazioniService.queryDinamica(paramQuery, segnalazione);
        return ResponseEntity.ok(result);
    }

    /**
     * Metodo per ottenere tutte le segnalazioni.
     */
    @GetMapping
    public ResponseEntity<List<ArchiviazioneSegnalazioni>> getAllSegnalazioni() {
        List<ArchiviazioneSegnalazioni> segnalazioni = archiviazioneSegnalazioniService.findAllSegnalazioni();
        return ResponseEntity.ok(segnalazioni);
    }

    /**
     * Metodo per ottenere una segnalazione in base al suo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArchiviazioneSegnalazioni> getSegnalazioneById(@PathVariable String id) {
        ArchiviazioneSegnalazioni segnalazione = archiviazioneSegnalazioniService.findSegnalazioneById(id);
        if (segnalazione == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(segnalazione);
    }

    /**
     * Metodo per creare una nuova segnalazione.
     */
    @PostMapping("/post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> createSegnalazione(@Valid @RequestBody ArchiviazioneSegnalazioni segnalazione) {
        archiviazioneSegnalazioniService.saveSegnalazione(segnalazione);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Metodo per aggiornare una segnalazione esistente.
     */
    /* @PutMapping("/put/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateSegnalazione(@PathVariable String id,
                                                  @Valid @RequestBody ArchiviazioneSegnalazioni segnalazione) {
        archiviazioneSegnalazioniService.updateSegnalazione(id, segnalazione);
        return ResponseEntity.ok().build();
    } */

    /**
     * Metodo per eliminare una segnalazione.
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSegnalazione(@PathVariable String id) {
        archiviazioneSegnalazioniService.deleteSegnalazione(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Metodo per caricare un file multimediale su S3 e associarlo a una segnalazione.
     */
    /* @PostMapping("/{id}/file")
    public ResponseEntity<String> uploadFile(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = archiviazioneSegnalazioniService.caricaFileSegnalazione(id, file);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante il caricamento del file: " + e.getMessage());
        }
    } */

    /**
     * Metodo per scaricare il file multimediale associato a una segnalazione.
     */
    /* @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {
        try {
            InputStream fileInputStream = archiviazioneSegnalazioniService.scaricaFileSegnalazione(id);
            byte[] fileContent = fileInputStream.readAllBytes();
            return ResponseEntity.ok(fileContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Ritorna un errore in caso di problemi nel download
        }
    } */

    /**
     * Metodo per eliminare il file multimediale associato a una segnalazione.
     */
    /* @DeleteMapping("/{id}/file")
    public ResponseEntity<Void> deleteFile(@PathVariable String id) {
        archiviazioneSegnalazioniService.eliminaFileSegnalazione(id);
        return ResponseEntity.noContent().build();
    } */
}
