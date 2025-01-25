package com.prova.e_commerce.dbKey.controller.rest;

import com.prova.e_commerce.dbKey.model.SettingSite;
import com.prova.e_commerce.dbKey.service.SettingSiteService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settings")
public class SettingSiteController {

    @Autowired
    private SettingSiteService settingSiteService;

    /**
     * Recupera le impostazioni del sito per un determinato utente.
     * @param userId L'ID dell'utente
     * @return Le impostazioni dell'utente o un 404 se non esistono
     */
    @GetMapping("/get/{userId}")
    public ResponseEntity<SettingSite> getImpostazioni(@Valid @PathVariable String userId) {
        // Chiamata al service per trovare le impostazioni
        return settingSiteService.trovaImpostazioni(userId)
                .map(ResponseEntity::ok) // Se le impostazioni sono trovate
                .orElseGet(() -> ResponseEntity.notFound().build()); // Se non trovate, ritorna 404
    }

    /**
     * Salva o aggiorna le impostazioni del sito per un determinato utente.
     * @param userId L'ID dell'utente
     * @param settings Le nuove impostazioni da salvare
     * @return Risposta di successo o errore
     */
    @PostMapping("/post/{userId}")
    public ResponseEntity<String> salvaImpostazioni(@Valid @PathVariable String userId, @Valid @RequestBody SettingSite settings) {
        // Salva o aggiorna le impostazioni
        settingSiteService.salvaImpostazioni(userId, settings);
        return ResponseEntity.ok("Impostazioni salvate con successo");
    }

    /**
     * Resetta le impostazioni del sito per un determinato utente.
     * @param userId L'ID dell'utente per il quale resettare le impostazioni
     * @return Risposta di successo
     */
    @DeleteMapping("/reset/{userId}")
    public ResponseEntity<String> resetImpostazioni(@Valid @PathVariable String userId) {
        // Resetta le impostazioni
        settingSiteService.resetImpostazioni(userId);
        return ResponseEntity.ok("Impostazioni resettate con successo");
    }
}
