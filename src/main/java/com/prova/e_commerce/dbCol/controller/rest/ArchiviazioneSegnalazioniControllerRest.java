package com.prova.e_commerce.dbCol.controller.rest;

import com.prova.e_commerce.dbCol.model.ArchiviazioneSegnalazioni;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.service.ArchiviazioneSegnalazioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/archiviazioneSegnalazioni")
public class ArchiviazioneSegnalazioniControllerRest {

    @Autowired
    private ArchiviazioneSegnalazioniService archiviazioneSegnalazioniService;

    // Endpoint per recuperare tutte le segnalazioni
    @GetMapping
    public ResponseEntity<List<ArchiviazioneSegnalazioni>> getAllSegnalazioni() {
        List<ArchiviazioneSegnalazioni> segnalazioni = archiviazioneSegnalazioniService.findAllSegnalazioni();
        return new ResponseEntity<>(segnalazioni, HttpStatus.OK);
    }

    // Endpoint per recuperare una segnalazione per ID
    @GetMapping("/{id}")
    public ResponseEntity<ArchiviazioneSegnalazioni> getSegnalazioneById(@PathVariable String id) {
        ArchiviazioneSegnalazioni segnalazione = archiviazioneSegnalazioniService.findSegnalazioneById(id);
        if (segnalazione != null) {
            return new ResponseEntity<>(segnalazione, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint per creare una nuova segnalazione
    @PostMapping
    public ResponseEntity<Void> createSegnalazione(@RequestBody ArchiviazioneSegnalazioni segnalazione) {
        archiviazioneSegnalazioniService.saveSegnalazione(segnalazione);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Endpoint per aggiornare una segnalazione esistente
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSegnalazione(@PathVariable String id, @RequestBody ArchiviazioneSegnalazioni segnalazione) {
        archiviazioneSegnalazioniService.updateSegnalazione(id, segnalazione);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Endpoint per eliminare una segnalazione
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSegnalazione(@PathVariable String id) {
        archiviazioneSegnalazioniService.deleteSegnalazione(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint per eseguire una query dinamica
    @PostMapping("/query")
    public ResponseEntity<List<ArchiviazioneSegnalazioni>> queryDinamica(@RequestBody ParamQueryCassandra paramQuery,
                                                                          @RequestBody ArchiviazioneSegnalazioni segnalazione) {
        List<ArchiviazioneSegnalazioni> segnalazioni = archiviazioneSegnalazioniService.queryDinamica(paramQuery, segnalazione);
        return new ResponseEntity<>(segnalazioni, HttpStatus.OK);
    }
}
