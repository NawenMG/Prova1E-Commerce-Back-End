package com.prova.e_commerce.dbRel.oracle.jdbc.controller.rest;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Ordini;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.OrdiniService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordini/rest")
public class OrdiniControllerRest {
    @Autowired
    private OrdiniService ordiniService;

    /**
     * Endpoint per ottenere una lista di ordini in base a parametri di query dinamici.
     */
    @GetMapping("/query")
    public ResponseEntity<List<Ordini>> queryOrdini(@RequestBody ParamQuery paramQuery, @Valid @RequestBody Ordini ordini) {
        List<Ordini> ordiniList = ordiniService.queryOrdini(paramQuery, ordini);
        return new ResponseEntity<>(ordiniList, HttpStatus.OK);
    }

    /**
     * Endpoint per inserire un nuovo ordine.
     */
    @PostMapping("/inserisci")
    public ResponseEntity<String> inserisciOrdine(@Valid @RequestBody Ordini ordini) {
        String response = ordiniService.inserisciOrdine(ordini);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint per aggiornare un ordine esistente.
     */
    @PutMapping("/aggiorna/{orderID}")
    public ResponseEntity<String> aggiornaOrdine(@PathVariable String orderID, @Valid @RequestBody Ordini ordini) {
        String response = ordiniService.aggiornaOrdine(orderID, ordini);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint per eliminare un ordine in base all'ID.
     */
    @DeleteMapping("/elimina/{orderID}")
    public ResponseEntity<String> eliminaOrdine(@PathVariable String orderID) {
        String response = ordiniService.eliminaOrdine(orderID);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint per salvare un numero specificato di ordini generati casualmente.
     */
    @PostMapping("/salva-casuali/{numero}")
    public ResponseEntity<String> salvaOrdiniCasuali(@PathVariable int numero) {
        String response = ordiniService.salvaOrdiniCasuali(numero);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
