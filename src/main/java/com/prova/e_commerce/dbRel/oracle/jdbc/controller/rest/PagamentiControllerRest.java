package com.prova.e_commerce.dbRel.oracle.jdbc.controller.rest;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.PagamentiService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamenti/rest")
public class PagamentiControllerRest {
    @Autowired
    private PagamentiService pagamentiService;

    /**
     * Endpoint per ottenere una lista di pagamenti in base a parametri di query dinamici.
     */
    @GetMapping("/query")
    public ResponseEntity<List<Pagamenti>> queryPagamenti(@RequestBody ParamQuery paramQuery, @Valid @RequestBody Pagamenti pagamenti) {
        List<Pagamenti> pagamentiList = pagamentiService.queryPagamenti(paramQuery, pagamenti);
        return new ResponseEntity<>(pagamentiList, HttpStatus.OK);
    }

    /**
     * Endpoint per inserire un nuovo pagamento.
     */
    @PostMapping("/inserisci")
    public ResponseEntity<String> inserisciPagamento( @Valid @RequestBody Pagamenti pagamenti) {
        String response = pagamentiService.inserisciPagamento(pagamenti);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint per aggiornare un pagamento esistente.
     */
    @PutMapping("/aggiorna/{paymentID}")
    public ResponseEntity<String> aggiornaPagamento(@PathVariable String paymentID, @Valid @RequestBody Pagamenti pagamenti) {
        String response = pagamentiService.aggiornaPagamento(paymentID, pagamenti);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint per eliminare un pagamento in base all'ID.
     */
    @DeleteMapping("/elimina/{paymentID}")
    public ResponseEntity<String> eliminaPagamento(@PathVariable String paymentID) {
        String response = pagamentiService.eliminaPagamento(paymentID);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint per salvare un numero specificato di pagamenti generati casualmente.
     */
    @PostMapping("/salva-casuali/{numero}")
    public ResponseEntity<String> salvaPagamentiCasuali(@PathVariable int numero) {
        String response = pagamentiService.salvaPagamentiCasuali(numero);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
