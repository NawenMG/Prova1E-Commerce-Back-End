package com.prova.e_commerce.dbKey.controller.rest;

import com.prova.e_commerce.dbKey.model.Cronologia;
import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;
import com.prova.e_commerce.dbKey.service.CronologiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cronologia")
public class CronologiaController {

    @Autowired
    private CronologiaService cronologiaService;

    /**
     * Aggiungi nuovi prodotti alla cronologia dell'utente.
     *
     * @param userId L'ID dell'utente
     * @param prodotti La lista di prodotti da aggiungere
     * @return Risposta con status 200 OK
     */
    @PostMapping("/aggiungi/{userId}")
    public ResponseEntity<Void> aggiungiProdottiCronologia(
            @PathVariable String userId, @RequestBody List<Prodotto> prodotti) {
        cronologiaService.aggiungiDatiCronologici(userId, prodotti);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Recupera la cronologia dell'utente specificato.
     *
     * @param userId L'ID dell'utente
     * @return La cronologia dell'utente, se esiste
     */
    @GetMapping("/visualizza/{userId}")
    public ResponseEntity<Cronologia> visualizzaCronologia(@PathVariable String userId) {
        Optional<Cronologia> cronologiaOpt = cronologiaService.visualizzaCronologia(userId);

        return cronologiaOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Elimina un singolo prodotto dalla cronologia dell'utente.
     *
     * @param userId L'ID dell'utente
     * @param productId L'ID del prodotto da eliminare
     * @return Risposta con status 200 OK se prodotto eliminato, 404 se non trovato
     */
    @DeleteMapping("/elimina/{userId}/{productId}")
    public ResponseEntity<Void> eliminaSingolaRicerca(
            @PathVariable String userId, @PathVariable String productId) {
        cronologiaService.eliminaSingolaRicerca(userId, productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Elimina tutte le ricerche (prodotti) dalla cronologia dell'utente.
     *
     * @param userId L'ID dell'utente
     * @return Risposta con status 200 OK
     */
    @DeleteMapping("/eliminaTutte/{userId}")
    public ResponseEntity<Void> eliminaTutteLeRicerche(@PathVariable String userId) {
        cronologiaService.eliminaTutteLeRicerche(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}