package com.prova.e_commerce.dbDoc.controller.rest;

import com.prova.e_commerce.dbDoc.entity.SchedeProdotti;
import com.prova.e_commerce.dbDoc.service.SchedeProdottiService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/schede-prodotti")
public class SchedeProdottiControllerRest {
    
    @Autowired
    private SchedeProdottiService schedeProdottiService;

    // Endpoint per trovare prodotti per nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<SchedeProdotti>> getByNome(@PathVariable String nome) {
        List<SchedeProdotti> prodotti = schedeProdottiService.findByNome(nome);
        return ResponseEntity.ok(prodotti);
    }

    // Endpoint per trovare prodotti con prezzo inferiore a una certa soglia
    @GetMapping("/prezzo-meno-di/{prezzo}")
    public ResponseEntity<List<SchedeProdotti>> getByPrezzoLessThan(@PathVariable BigDecimal prezzo) {
        List<SchedeProdotti> prodotti = schedeProdottiService.findByPrezzoLessThan(prezzo);
        return ResponseEntity.ok(prodotti);
    }

    // Endpoint per inserire un nuovo prodotto
    @PostMapping
    public ResponseEntity<SchedeProdotti> createSchedaProdotto(@Valid @RequestBody SchedeProdotti prodotto) {
        SchedeProdotti nuovoProdotto = schedeProdottiService.insert(prodotto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuovoProdotto);
    }

     @GetMapping("/random/{count}")
    public List<SchedeProdotti> getRandomRecensioni(@PathVariable int count) {
        return schedeProdottiService.generateRandomSchedeProdotti(count);
    }

    // Endpoint per aggiornare un prodotto esistente
    @PutMapping("/{id}")
    public ResponseEntity<SchedeProdotti> updateSchedaProdotto(
            @PathVariable String id,
            @Valid @RequestBody SchedeProdotti prodotto) {
        SchedeProdotti prodottoAggiornato = schedeProdottiService.update(id, prodotto);
        return ResponseEntity.ok(prodottoAggiornato);
    }

    // Endpoint per eliminare un prodotto per ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedaProdotto(@PathVariable String id) {
        schedeProdottiService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
