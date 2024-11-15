package com.prova.e_commerce.dbTS.controller.rest;

import com.prova.e_commerce.dbTS.model.TrafficAnalysis;
import com.prova.e_commerce.dbTS.service.TrafficAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/traffic-analysis")
public class TrafficAnalysisControllerRest {

    @Autowired
    private TrafficAnalysisService trafficAnalysisService;

    // Endpoint per inserire una singola analisi del traffico
    @PostMapping("/insert")
    public ResponseEntity<String> insertTrafficAnalysis(@RequestBody TrafficAnalysis analysis) {
        trafficAnalysisService.insert(analysis);
        return new ResponseEntity<>("Traffic analysis inserted successfully", HttpStatus.CREATED);
    }

    // Endpoint per inserire un batch di analisi del traffico
    @PostMapping("/insert-batch")
    public ResponseEntity<String> insertBatchTrafficAnalysis(@RequestBody List<TrafficAnalysis> analysisList) {
        trafficAnalysisService.insertBatch(analysisList);
        return new ResponseEntity<>("Batch traffic analysis inserted successfully", HttpStatus.CREATED);
    }

    // Endpoint per eliminare le analisi per una URL specifica
    @DeleteMapping("/delete/{url}")
    public ResponseEntity<String> deleteByUrl(@PathVariable("url") String urlPagina) {
        trafficAnalysisService.deleteByUrl(urlPagina);
        return new ResponseEntity<>("Traffic analysis for URL deleted successfully", HttpStatus.OK);
    }

    // Endpoint per recuperare le analisi in un intervallo di tempo
    @GetMapping("/find-by-time-range")
    public ResponseEntity<List<TrafficAnalysis>> findByTimeRange(
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {
        
        Instant start = Instant.parse(startTime);
        Instant end = Instant.parse(endTime);
        
        List<TrafficAnalysis> analyses = trafficAnalysisService.findByTimeRange(start, end);
        return new ResponseEntity<>(analyses, HttpStatus.OK);
    }

    // Endpoint per recuperare le analisi per una URL specifica
    @GetMapping("/find-by-url")
    public ResponseEntity<List<TrafficAnalysis>> findByUrl(@RequestParam("url") String urlPagina) {
        List<TrafficAnalysis> analyses = trafficAnalysisService.findByUrl(urlPagina);
        return new ResponseEntity<>(analyses, HttpStatus.OK);
    }

    // Endpoint per recuperare le visite medie per URL
    @GetMapping("/average-visits")
    public ResponseEntity<List<TrafficAnalysis>> getAverageVisitsByUrl() {
        List<TrafficAnalysis> analyses = trafficAnalysisService.getAverageVisitsByUrl();
        return new ResponseEntity<>(analyses, HttpStatus.OK);
    }


     // Endpoint per ottenere una lista di TrafficAnalysis generati casualmente
     @GetMapping("/generateTrafficAnalysis")
     public List<TrafficAnalysis> generateRandomTrafficAnalysis(
             @RequestParam(value = "count", defaultValue = "5") int count) {
         return trafficAnalysisService.generateRandomTrafficAnalysisList(count);
     }
}
