package com.prova.e_commerce.dbTS.controller.rest;

import com.prova.e_commerce.dbTS.model.UserAnalysis;
import com.prova.e_commerce.dbTS.service.UserAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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

    // Endpoint per ottenere le analisi in un intervallo temporale
    @GetMapping("/time-range")
    public ResponseEntity<List<UserAnalysis>> findByTimeRange(
            @RequestParam Instant startTime, 
            @RequestParam Instant endTime) {
        List<UserAnalysis> result = userAnalysisService.findByTimeRange(startTime, endTime);
        return ResponseEntity.ok(result);
    }

    // Endpoint per ottenere le analisi per un utente specifico
    @GetMapping("/user/{utente}")
    public ResponseEntity<List<UserAnalysis>> findByUser(@PathVariable String utente) {
        List<UserAnalysis> result = userAnalysisService.findByUser(utente);
        return ResponseEntity.ok(result);
    }

    // Endpoint per ottenere le analisi per tipo di dispositivo
    @GetMapping("/device/{tipoDiDispositivo}")
    public ResponseEntity<List<UserAnalysis>> findByDeviceType(@PathVariable String tipoDiDispositivo) {
        List<UserAnalysis> result = userAnalysisService.findByDeviceType(tipoDiDispositivo);
        return ResponseEntity.ok(result);
    }

    // Endpoint per ottenere le analisi per tipo di azione
    @GetMapping("/action/{azione}")
    public ResponseEntity<List<UserAnalysis>> findByAction(@PathVariable String azione) {
        List<UserAnalysis> result = userAnalysisService.findByAction(azione);
        return ResponseEntity.ok(result);
    }

    // Endpoint per ottenere la durata media delle azioni per ogni utente
    @GetMapping("/average-duration")
    public ResponseEntity<Map<String, Double>> getAverageDurationByUser() {
        Map<String, Double> result = userAnalysisService.getAverageDurationByUser();
        return ResponseEntity.ok(result);
    }

    // Endpoint per ottenere analisi per utente, dispositivo e intervallo temporale
    @GetMapping("/user-device-time-range")
    public ResponseEntity<List<UserAnalysis>> findByUserDeviceAndTimeRange(
            @RequestParam String utente,
            @RequestParam String tipoDiDispositivo,
            @RequestParam Instant startTime,
            @RequestParam Instant endTime) {
        List<UserAnalysis> result = userAnalysisService.findByUserDeviceAndTimeRange(
                utente, tipoDiDispositivo, startTime, endTime);
        return ResponseEntity.ok(result);
    }


     // Endpoint per ottenere una lista di UserAnalysis generati casualmente
     @GetMapping("/generateUserAnalysis")
     public List<UserAnalysis> generateRandomUserAnalysis(
             @RequestParam(value = "count", defaultValue = "5") int count) {
         return userAnalysisService.generateRandomUserAnalysisList(count);
     }
}
