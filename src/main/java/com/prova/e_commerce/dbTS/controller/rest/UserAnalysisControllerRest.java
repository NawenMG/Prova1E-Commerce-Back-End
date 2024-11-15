package com.prova.e_commerce.dbTS.controller.rest;

import com.prova.e_commerce.dbTS.model.UserAnalysis;
import com.prova.e_commerce.dbTS.service.UserAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-analyses")
public class UserAnalysisControllerRest {

    @Autowired
    private UserAnalysisService userAnalysisService;

    // Endpoint per inserire una singola analisi
    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody UserAnalysis analysis) {
        userAnalysisService.insert(analysis);
        return ResponseEntity.ok().build();
    }

    // Endpoint per inserire un batch di analisi
    @PostMapping("/batch")
    public ResponseEntity<Void> insertBatch(@RequestBody List<UserAnalysis> analysisList) {
        userAnalysisService.insertBatch(analysisList);
        return ResponseEntity.ok().build();
    }

    // Endpoint per aggiornare un'analisi
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody UserAnalysis userAnalysis) {
        userAnalysisService.update(userAnalysis, userAnalysis); // Aggiungere logica per l'aggiornamento tra analisi vecchia e nuova
        return ResponseEntity.ok().build();
    }

    // Endpoint per eliminare analisi in base al tag utente
    @DeleteMapping("/by-tag/{utente}")
    public ResponseEntity<Void> deleteByTag(@PathVariable String utente) {
        userAnalysisService.deleteByTag(utente);
        return ResponseEntity.ok().build();
    }

     // Endpoint per ottenere una lista di UserAnalysis generati casualmente
     @GetMapping("/generateUserAnalysis")
     public List<UserAnalysis> generateRandomUserAnalysis(
             @RequestParam(value = "count", defaultValue = "5") int count) {
         return userAnalysisService.generateRandomUserAnalysisList(count);
     }
}
