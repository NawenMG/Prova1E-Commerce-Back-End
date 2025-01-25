package com.prova.e_commerce.dbG.controller.rest;

import com.prova.e_commerce.dbG.service.ServiceGraphDB;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('USERAI')")
@RequestMapping("/api/graphdb")
public class ControllerRestGraphDB {

    @Autowired
    private ServiceGraphDB serviceGraphDB;

    @PostMapping("/post/visita")
    public ResponseEntity<Void> visitaProdotto(@Valid @RequestParam Long utenteId, @Valid @RequestParam Long prodottoId) {
        serviceGraphDB.visitaProdotto(utenteId, prodottoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/post/acquisto")
    public ResponseEntity<Void> acquistoProdotto(@Valid @RequestParam Long utenteId, @Valid @RequestParam Long prodottoId) {
        serviceGraphDB.acquistoProdotto(utenteId, prodottoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/post/categoria")
    public ResponseEntity<Void> appartenenzaCategoria(@Valid @RequestParam Long prodottoId, @Valid @RequestParam String categoriaNome) {
        serviceGraphDB.appartenenzaCategoria(prodottoId, categoriaNome);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/post/provenienza")
    public ResponseEntity<Void> provenienzaGeografica(@Valid @RequestParam Long utenteId, @Valid @RequestParam Long prodottoId) {
        serviceGraphDB.provenienzaGeografica(utenteId, prodottoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/visite")
    public ResponseEntity<List<String>> getVisiteUtente(@Valid @RequestParam Long utenteId) {
        List<String> visite = serviceGraphDB.getVisiteUtente(utenteId);
        return ResponseEntity.ok(visite);
    }

    @GetMapping("/get/acquisti")
    public ResponseEntity<List<String>> getAcquistiUtente(@Valid @RequestParam Long utenteId) {
        List<String> acquisti = serviceGraphDB.getAcquistiUtente(utenteId);
        return ResponseEntity.ok(acquisti);
    }

    @GetMapping("/get/categorie")
    public ResponseEntity<List<String>> getCategorieProdotto(@Valid @RequestParam Long prodottoId) {
        List<String> categorie = serviceGraphDB.getCategorieProdotto(prodottoId);
        return ResponseEntity.ok(categorie);
    }

    @GetMapping("/get/provenienze")
    public ResponseEntity<List<String>> getProvenienzeUtente(@Valid @RequestParam Long utenteId) {
        List<String> provenienze = serviceGraphDB.getProvenienzeUtente(utenteId);
        return ResponseEntity.ok(provenienze);
    }
}
