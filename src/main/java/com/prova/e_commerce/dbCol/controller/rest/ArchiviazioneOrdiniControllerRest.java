package com.prova.e_commerce.dbCol.controller.rest;

import com.prova.e_commerce.dbCol.model.ArchiviazioneOrdini;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.service.ArchiviazioneOrdiniService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/archiviazioneOrdini")

public class ArchiviazioneOrdiniControllerRest {

    @Autowired
    private ArchiviazioneOrdiniService archiviazioneOrdiniService;

    // Recupera un ordine per ID
    @GetMapping("/{id}")
    public ResponseEntity<ArchiviazioneOrdini> getOrdineById(@PathVariable String id) {
        ArchiviazioneOrdini ordine = archiviazioneOrdiniService.findOrdineById(id);
        if (ordine != null) {
            return ResponseEntity.ok(ordine);  // Restituisce 200 OK se trovato
        } else {
            return ResponseEntity.notFound().build();  // Restituisce 404 Not Found se l'ordine non esiste
        }
    }

    // Recupera tutti gli ordini
    @GetMapping
    public ResponseEntity<List<ArchiviazioneOrdini>> getAllOrdini() {
        List<ArchiviazioneOrdini> ordini = archiviazioneOrdiniService.findAllOrdini();
        return ResponseEntity.ok(ordini);  // Restituisce 200 OK con la lista degli ordini
    }

    // Crea un nuovo ordine
    @PostMapping("/post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> createOrdine(@Valid @RequestBody ArchiviazioneOrdini ordine) {
        archiviazioneOrdiniService.saveOrdine(ordine);
        return ResponseEntity.status(HttpStatus.CREATED).build();  // Restituisce 201 Created
    }

    /* // Aggiorna un ordine esistente
    @PutMapping("/put/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateOrdine(@PathVariable String id, @Valid @RequestBody ArchiviazioneOrdini ordine) {
        archiviazioneOrdiniService.updateOrdine(id, ordine);
        return ResponseEntity.ok().build();  // Restituisce 200 OK se l'ordine è stato aggiornato
    } */

    // Elimina un ordine per ID
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteOrdine(@PathVariable String id) {
        archiviazioneOrdiniService.deleteOrdine(id);
        return ResponseEntity.noContent().build();  // Restituisce 204 No Content se l'ordine è stato eliminato
    }

    // Esegui una query dinamica sugli ordini
    @PostMapping("/query")
    public ResponseEntity<List<ArchiviazioneOrdini>> queryDinamica(@RequestBody ParamQueryCassandra paramQuery,
                                                                  @Valid @RequestBody ArchiviazioneOrdini ordine) {
        List<ArchiviazioneOrdini> ordini = archiviazioneOrdiniService.queryDinamica(paramQuery, ordine);
        return ResponseEntity.ok(ordini);  // Restituisce 200 OK con il risultato della query
    }
}
