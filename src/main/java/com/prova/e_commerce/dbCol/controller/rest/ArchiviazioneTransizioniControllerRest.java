package com.prova.e_commerce.dbCol.controller.rest;

import com.prova.e_commerce.dbCol.model.ArchiviazioneTransizioni;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.service.ArchiviazioneTransizioniService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/archiviazioneTransizioni")
public class ArchiviazioneTransizioniControllerRest {

    @Autowired
    private ArchiviazioneTransizioniService archiviazioneTransizioniService;

    /**
     * Recupera una transizione per ID.
     * Esempio di chiamata: GET /api/archiviazioneTransizioni/123
     *
     * @param id L'ID della transizione
     * @return La transizione se trovata, altrimenti 404 Not Found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ArchiviazioneTransizioni> getTransizione(@Valid @PathVariable String id) {
        ArchiviazioneTransizioni transizione = archiviazioneTransizioniService.findTransizioneById(id);
        if (transizione != null) {
            return ResponseEntity.ok(transizione);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Recupera tutte le transizioni.
     * Esempio di chiamata: GET /api/archiviazioneTransizioni/all
     *
     * @return Lista di transizioni
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('TRANSITIONUSER')")
    public List<ArchiviazioneTransizioni> getAllTransizioni() {
        return archiviazioneTransizioniService.findAllTransizioni();
    }

    /**
     * Esegue una query dinamica.
     * Esempio di chiamata: POST /api/archiviazioneTransizioni/query
     * con body JSON contenente ParamQueryCassandra e ArchiviazioneTransizioni.
     *
     * @param paramQuery  Parametri generici di ricerca
     * @param transizione Criteri della transizione (filtri)
     * @return Lista di transizioni che soddisfano la query
     */
    @PostMapping("/query")
    @PreAuthorize("hasAnyRole('USER', 'TRANSITIONUSER')")
    public List<ArchiviazioneTransizioni> queryDinamica(
            @Valid @RequestBody ParamQueryCassandra paramQuery,
            @Valid @RequestBody ArchiviazioneTransizioni transizione) {
        return archiviazioneTransizioniService.queryDinamica(paramQuery, transizione);
    }

    /**
     * Crea una nuova transizione.
     * Esempio di chiamata: POST /api/archiviazioneTransizioni/post
     * Necessita del ruolo ROLE_USER.
     *
     * @param transizione Oggetto da creare
     * @return 201 Created se tutto ok
     */
    @PostMapping("/post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> createTransizione(@Valid @RequestBody ArchiviazioneTransizioni transizione) {
        archiviazioneTransizioniService.saveTransizione(transizione);
        return ResponseEntity.status(201).build();  // 201 Created
    }

    /**
     * Elimina una transizione per ID.
     * Esempio di chiamata: DELETE /api/archiviazioneTransizioni/delete/123
     *
     * @param id ID della transizione da eliminare
     * @return 204 No Content se eliminazione effettuata
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTransizione(@Valid @PathVariable String id) {
        archiviazioneTransizioniService.deleteTransizione(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }

    // ================================
    // Esempi aggiuntivi (opzionali)
    // ================================

    /*
    // Aggiorna una transizione esistente
    @PutMapping("/put/{id}")
    public ResponseEntity<Void> updateTransizione(@PathVariable String id, 
                                                  @Valid @RequestBody ArchiviazioneTransizioni transizione) {
        // Non hai un metodo update nel service, ma potresti implementarlo:
        // archiviazioneTransizioniService.updateTransizione(id, transizione);
        return ResponseEntity.ok().build();  // 200 OK
    }

    // Invio a RabbitMQ
    @PostMapping("/sendToRabbitMQ")
    public ResponseEntity<Void> sendToRabbitMQ(@RequestBody ArchiviazioneTransizioni transizione) {
        archiviazioneTransizioniService.sendToRabbitMQ(transizione);
        return ResponseEntity.ok().build();
    }

    // Ricezione da RabbitMQ (polling)
    @GetMapping("/receiveFromRabbitMQ")
    public ResponseEntity<ArchiviazioneTransizioni> receiveFromRabbitMQ() {
        ArchiviazioneTransizioni received = archiviazioneTransizioniService.receiveFromRabbitMQ();
        if (received != null) {
            return ResponseEntity.ok(received);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Spedire al router
    @PostMapping("/sendToRouterQueue")
    public ResponseEntity<Void> sendToRouterQueue(@RequestBody ArchiviazioneTransizioni transizione) {
        archiviazioneTransizioniService.sendToRouterQueue(transizione);
        return ResponseEntity.ok().build();
    }

    // Ricevere dal router (polling)
    @GetMapping("/receiveFromRouterResponseQueue")
    public ResponseEntity<ArchiviazioneTransizioni> receiveFromRouterResponseQueue() {
        ArchiviazioneTransizioni received = archiviazioneTransizioniService.receiveFromRouterResponseQueue();
        if (received != null) {
            return ResponseEntity.ok(received);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    */
}
