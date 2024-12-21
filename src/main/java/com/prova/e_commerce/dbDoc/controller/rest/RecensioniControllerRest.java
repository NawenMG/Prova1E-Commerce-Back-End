package com.prova.e_commerce.dbDoc.controller.rest;

import com.prova.e_commerce.dbDoc.entity.Recensioni;
import com.prova.e_commerce.dbDoc.parametri.ParamQueryDbDoc;
import com.prova.e_commerce.dbDoc.service.RecensioniService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/* import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream; */
import java.util.List;

@RestController
@RequestMapping("/api/recensioni")
public class RecensioniControllerRest {

    @Autowired
    private RecensioniService recensioniService;

    // Endpoint per ottenere recensioni per un determinato productId
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Recensioni>> getRecensioniByProductId(@PathVariable String productId) {
        List<Recensioni> recensioni = recensioniService.findByProductId(productId);
        if (recensioni.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(recensioni, HttpStatus.OK);
    }

    // Endpoint per ottenere recensioni per un determinato userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recensioni>> getRecensioniByUserId(@PathVariable String userId) {
        List<Recensioni> recensioni = recensioniService.findByUserId(userId);
        if (recensioni.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(recensioni, HttpStatus.OK);
    }

    // Endpoint per ottenere recensioni con criteri dinamici
    @PostMapping("/query")
    public ResponseEntity<List<Recensioni>> queryRecensioni(@RequestBody ParamQueryDbDoc paramQueryDbDoc, @Valid @RequestBody Recensioni recensioni) {
        List<Recensioni> recensioniList = recensioniService.queryDynamic(paramQueryDbDoc, recensioni);
        return new ResponseEntity<>(recensioniList, HttpStatus.OK);
    }

    // Endpoint per creare una nuova recensione
    @PostMapping
    public ResponseEntity<Recensioni> createRecensione(@RequestBody Recensioni recensione) {
        Recensioni savedRecensione = recensioniService.saveRecensione(recensione);
        return new ResponseEntity<>(savedRecensione, HttpStatus.CREATED);
    }

    // Endpoint per aggiornare una recensione esistente
    @PutMapping("/{id}")
    public ResponseEntity<Recensioni> updateRecensione(@PathVariable String id, @Valid @RequestBody Recensioni recensione) {
        Recensioni updatedRecensione = recensioniService.updateRecensione(id, recensione);
        if (updatedRecensione == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedRecensione, HttpStatus.OK);
    }

    // Endpoint per eliminare una recensione
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecensione(@PathVariable String id) {
        boolean deleted = recensioniService.deleteRecensione(id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /* // Endpoint per caricare un file multimediale associato a una recensione (immagine o video)
    @PostMapping("/{recensioneId}/file")
    public ResponseEntity<String> uploadFile(@PathVariable String recensioneId, @RequestParam("file") MultipartFile file, @RequestParam("tipoFile") String tipoFile) {
        try {
            String fileUrl = recensioniService.caricaFileRecensione(recensioneId, file, tipoFile);
            return new ResponseEntity<>(fileUrl, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint per scaricare un file multimediale associato a una recensione (immagine o video)
    @GetMapping("/{recensioneId}/file")
    public ResponseEntity<InputStream> downloadFile(@PathVariable String recensioneId, @RequestParam("tipoFile") String tipoFile) {
        try {
            InputStream fileData = recensioniService.scaricaFileRecensione(recensioneId, tipoFile);
            return new ResponseEntity<>(fileData, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint per eliminare un file multimediale associato a una recensione (immagine o video)
    @DeleteMapping("/{recensioneId}/file")
    public ResponseEntity<Void> deleteFile(@PathVariable String recensioneId, @RequestParam("tipoFile") String tipoFile) {
        try {
            recensioniService.eliminaFileRecensione(recensioneId, tipoFile);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } */
}
