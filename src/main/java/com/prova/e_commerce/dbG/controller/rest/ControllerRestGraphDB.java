package com.prova.e_commerce.dbG.controller.rest;

import com.prova.e_commerce.dbG.service.ServiceGraphDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/graphdb")
public class ControllerRestGraphDB {

    @Autowired
    private ServiceGraphDB serviceGraphDB;

    @PostMapping("/visita")
    public ResponseEntity<Void> visitaProdotto(@RequestParam Long utenteId, @RequestParam Long prodottoId) {
        serviceGraphDB.visitaProdotto(utenteId, prodottoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/acquisto")
    public ResponseEntity<Void> acquistoProdotto(@RequestParam Long utenteId, @RequestParam Long prodottoId) {
        serviceGraphDB.acquistoProdotto(utenteId, prodottoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/categoria")
    public ResponseEntity<Void> appartenenzaCategoria(@RequestParam Long prodottoId, @RequestParam String categoriaNome) {
        serviceGraphDB.appartenenzaCategoria(prodottoId, categoriaNome);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/provenienza")
    public ResponseEntity<Void> provenienzaGeografica(@RequestParam Long utenteId, @RequestParam Long prodottoId) {
        serviceGraphDB.provenienzaGeografica(utenteId, prodottoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/visite")
    public ResponseEntity<List<String>> getVisiteUtente(@RequestParam Long utenteId) {
        List<String> visite = serviceGraphDB.getVisiteUtente(utenteId);
        return ResponseEntity.ok(visite);
    }

    @GetMapping("/acquisti")
    public ResponseEntity<List<String>> getAcquistiUtente(@RequestParam Long utenteId) {
        List<String> acquisti = serviceGraphDB.getAcquistiUtente(utenteId);
        return ResponseEntity.ok(acquisti);
    }

    @GetMapping("/categorie")
    public ResponseEntity<List<String>> getCategorieProdotto(@RequestParam Long prodottoId) {
        List<String> categorie = serviceGraphDB.getCategorieProdotto(prodottoId);
        return ResponseEntity.ok(categorie);
    }

    @GetMapping("/provenienze")
    public ResponseEntity<List<String>> getProvenienzeUtente(@RequestParam Long utenteId) {
        List<String> provenienze = serviceGraphDB.getProvenienzeUtente(utenteId);
        return ResponseEntity.ok(provenienze);
    }
}
