package com.prova.e_commerce.dbCol.controller.rest;

import com.prova.e_commerce.dbCol.model.ArchiviazioneTransizioni;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.service.ArchiviazioneTransizioniService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/archiviazioneTransizioni")
public class ArchiviazioneTransizioniControllerRest {

    @Autowired
    private ArchiviazioneTransizioniService archiviazioneTransizioniService;

    // Recupera una transizione per ID
    @GetMapping("/{id}")
    public ResponseEntity<ArchiviazioneTransizioni> getTransizione(@PathVariable String id) {
        ArchiviazioneTransizioni transizione = archiviazioneTransizioniService.findTransizioneById(id);
        if (transizione != null) {
            return ResponseEntity.ok(transizione);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Recupera tutte le transizioni
    @GetMapping("/all")
    public List<ArchiviazioneTransizioni> getAllTransizioni() {
        return archiviazioneTransizioniService.findAllTransizioni();
    }

    // Query dinamica per recuperare transizioni con parametri personalizzati
    @PostMapping("/query")
    public List<ArchiviazioneTransizioni> queryDinamica(@RequestBody ParamQueryCassandra paramQuery, 
                                                        @Valid @RequestBody ArchiviazioneTransizioni transizione) {
        return archiviazioneTransizioniService.queryDinamica(paramQuery, transizione);
    }

    // Crea una nuova transizione
    @PostMapping
    public ResponseEntity<Void> createTransizione(@Valid @RequestBody ArchiviazioneTransizioni transizione) {
        archiviazioneTransizioniService.saveTransizione(transizione);
        return ResponseEntity.status(201).build();  // 201 Created
    }

    // Aggiorna una transizione esistente
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTransizione(@PathVariable String id, @Valid @RequestBody ArchiviazioneTransizioni transizione) {
        archiviazioneTransizioniService.updateTransizione(id, transizione);
        return ResponseEntity.ok().build();  // 200 OK
    }

    // Elimina una transizione per ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransizione(@PathVariable String id) {
        archiviazioneTransizioniService.deleteTransizione(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
