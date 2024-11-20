package com.prova.e_commerce.dbRel.oracle.jdbc.controller.rest;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Resi;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.ResiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resi/rest")
public class ResiControllerRest {
    @Autowired
    private ResiService resiService;

    /**
     * Endpoint per eseguire una query avanzata sui resi in base a parametri dinamici.
     */
    @GetMapping("/query")
    public ResponseEntity<List<Resi>> queryResi(@RequestBody ParamQuery paramQuery, @RequestBody Resi resi) {
        List<Resi> resiList = resiService.queryResi(paramQuery, resi);
        return new ResponseEntity<>(resiList, HttpStatus.OK);
    }

    /**
     * Endpoint per inserire un nuovo reso nel database.
     */
    @PostMapping("/inserisci")
    public ResponseEntity<String> inserisciReso(@RequestBody Resi resi) {
        String response = resiService.inserisciReso(resi);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint per aggiornare un reso esistente.
     */
    @PutMapping("/aggiorna/{returnID}")
    public ResponseEntity<String> aggiornaReso(@PathVariable String returnID, @RequestBody Resi resi) {
        String response = resiService.aggiornaReso(returnID, resi);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint per eliminare un reso in base all'ID.
     */
    @DeleteMapping("/elimina/{returnID}")
    public ResponseEntity<String> eliminaReso(@PathVariable String returnID) {
        String response = resiService.eliminaReso(returnID);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint per generare un numero specificato di resi casuali e salvarli nel database.
     */
    @PostMapping("/salva-casuali/{numero}")
    public ResponseEntity<String> salvaResiCasuali(@PathVariable int numero) {
        String response = resiService.salvaResiCasuali(numero);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
