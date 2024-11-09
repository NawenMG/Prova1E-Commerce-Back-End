package com.prova.e_commerce.dbRel.oracle.jdbc.controller.rest;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Prodotti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.ProdottiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prodotti/rest")
public class ProdottiControllerRest {
    @Autowired
    private ProdottiService prodottiService;

    /**
     * Endpoint per eseguire una query avanzata sui prodotti in base a parametri dinamici.
     */
    @GetMapping("/query")
    public ResponseEntity<List<Prodotti>> queryProdotti(@RequestBody ParamQuery paramQuery, @RequestBody Prodotti prodotti) {
        List<Prodotti> prodottiList = prodottiService.queryProdotti(paramQuery, prodotti);
        return new ResponseEntity<>(prodottiList, HttpStatus.OK);
    }

    /**
     * Endpoint per inserire un nuovo prodotto nel database.
     */
    @PostMapping("/inserisci")
    public ResponseEntity<String> inserisciProdotto(@RequestBody Prodotti prodotti) {
        String response = prodottiService.inserisciProdotto(prodotti);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint per aggiornare un prodotto esistente.
     */
    @PutMapping("/aggiorna/{productID}")
    public ResponseEntity<String> aggiornaProdotto(@PathVariable int productID, @RequestBody Prodotti prodotti) {
        String response = prodottiService.aggiornaProdotto(productID, prodotti);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint per eliminare un prodotto in base all'ID.
     */
    @DeleteMapping("/elimina/{productID}")
    public ResponseEntity<String> eliminaProdotto(@PathVariable int productID) {
        String response = prodottiService.eliminaProdotto(productID);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint per generare un numero specificato di prodotti casuali e salvarli nel database.
     */
    @PostMapping("/salva-casuali/{numero}")
    public ResponseEntity<String> salvaProdottiCasuali(@PathVariable int numero) {
        String response = prodottiService.salvaProdottiCasuali(numero);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
