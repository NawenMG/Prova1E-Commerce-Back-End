package com.prova.e_commerce.dbRel.oracle.jdbc.controller.rest;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Categorie;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorie/rest")
public class CategorieControllerRest {
    @Autowired
    private CategorieService categorieService;

    /**
     * Endpoint per ottenere una lista di categorie in base a parametri di query dinamici.
     */
    @GetMapping("/query")
    public ResponseEntity<List<Categorie>> queryCategorie(@RequestBody ParamQuery paramQuery, @RequestBody Categorie categorie) {
        List<Categorie> categorieList = categorieService.queryCategorie(paramQuery, categorie);
        return new ResponseEntity<>(categorieList, HttpStatus.OK);
    }

    /**
     * Endpoint per inserire una nuova categoria.
     */
    @PostMapping("/inserisci")
    public ResponseEntity<String> inserisciCategoria(@RequestBody Categorie categorie) {
        String response = categorieService.inserisciCategoria(categorie);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint per aggiornare una categoria esistente.
     */
    @PutMapping("/aggiorna/{categoryID}")
    public ResponseEntity<String> aggiornaCategoria(@PathVariable int categoryID, @RequestBody Categorie categorie) {
        String response = categorieService.aggiornaCategoria(categoryID, categorie);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint per eliminare una categoria in base all'ID.
     */
    @DeleteMapping("/elimina/{categoryID}")
    public ResponseEntity<String> eliminaCategoria(@PathVariable int categoryID) {
        String response = categorieService.eliminaCategoria(categoryID);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint per salvare un numero specificato di categorie generate casualmente.
     */
    @PostMapping("/salva-casuali/{numero}")
    public ResponseEntity<String> salvaCategorieCasuali(@PathVariable int numero) {
        String response = categorieService.salvaCategorieCasuali(numero);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}