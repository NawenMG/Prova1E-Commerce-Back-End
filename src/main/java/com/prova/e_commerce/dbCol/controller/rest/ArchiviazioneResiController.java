package com.prova.e_commerce.dbCol.controller.rest;

import com.prova.e_commerce.dbCol.model.ArchiviazioneResi;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.service.ArchiviazioneResiService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resi")
public class ArchiviazioneResiController {

    @Autowired
    private ArchiviazioneResiService archiviazioneResiService;


    /**
     * Recupera un reso specifico tramite ID.
     * @param id L'ID del reso.
     * @return Il reso corrispondente.
     */
    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArchiviazioneResi> getResoByID(@Valid @PathVariable String id) {
        ArchiviazioneResi reso = archiviazioneResiService.getResoByID(id);
        return ResponseEntity.ok(reso);
    }

    /**
     * Esegue una query dinamica sui resi.
     * @param paramQuery Parametri della query.
     * @param reso Filtri per il reso.
     * @return Lista di resi filtrati.
     */
    @PostMapping("/query")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ArchiviazioneResi>> queryDinamica(
            @Valid @RequestBody ParamQueryCassandra paramQuery,
            @Valid @RequestBody ArchiviazioneResi reso) {
        List<ArchiviazioneResi> resi = archiviazioneResiService.queryDinamica(paramQuery, reso);
        return ResponseEntity.ok(resi);
    }

    /**
     * Salva un nuovo reso.
     * @param reso L'oggetto del reso da salvare.
     * @return Messaggio di conferma.
     */
    @PostMapping("/post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> saveReso(@Valid @RequestBody ArchiviazioneResi reso) {
        archiviazioneResiService.saveReso(reso);
        return ResponseEntity.ok("Reso salvato con successo.");
    }

    /**
     * Aggiorna un reso esistente.
     * @param id L'ID del reso da aggiornare.
     * @param reso L'oggetto del reso aggiornato.
     * @return Messaggio di conferma.
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateReso(@Valid @PathVariable String id, @Valid @RequestBody ArchiviazioneResi reso) {
        archiviazioneResiService.updateReso(id, reso);
        return ResponseEntity.ok("Reso aggiornato con successo.");
    }

    /**
     * Elimina un reso esistente.
     * @param id L'ID del reso da eliminare.
     * @return Messaggio di conferma.
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteReso(@Valid @PathVariable String id) {
        archiviazioneResiService.deleteReso(id);
        return ResponseEntity.ok("Reso eliminato con successo.");
    }
}
