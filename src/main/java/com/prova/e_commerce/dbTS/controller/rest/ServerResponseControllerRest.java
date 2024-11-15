package com.prova.e_commerce.dbTS.controller.rest;

import com.prova.e_commerce.dbTS.model.ServerResponse;
import com.prova.e_commerce.dbTS.service.ServerResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/server-responses")
public class ServerResponseControllerRest {

    @Autowired
    private ServerResponseService serverResponseService;

    // Endpoint per inserire una singola risposta del server
    @PostMapping("/add")
    public ResponseEntity<Void> insert(@RequestBody ServerResponse serverResponse) {
        serverResponseService.insert(serverResponse);
        return ResponseEntity.ok().build();
    }

    // Endpoint per inserire un batch di risposte del server
    @PostMapping("/addBatch")
    public ResponseEntity<Void> insertBatch(@RequestBody List<ServerResponse> serverResponseList) {
        serverResponseService.insertBatch(serverResponseList);
        return ResponseEntity.ok().build();
    }

    // Endpoint per aggiornare una risposta del server
    @PutMapping("/update")
    public ResponseEntity<Void> update(@RequestBody ServerResponse serverResponse) {
        serverResponseService.update(serverResponse, serverResponse); // Aggiungere logica per l'aggiornamento tra risposta vecchia e nuova
        return ResponseEntity.ok().build();
    }

    // Endpoint per eliminare risposte in base al tag del server
    @DeleteMapping("/delete/{server}")
    public ResponseEntity<Void> deleteByServer(@PathVariable String server) {
        serverResponseService.deleteByServer(server);
        return ResponseEntity.ok().build();
    }

    // Endpoint per ottenere le risposte in un intervallo temporale
    @GetMapping("/time-range")
    public ResponseEntity<List<ServerResponse>> findByTimeRange(
            @RequestParam Instant startTime,
            @RequestParam Instant endTime) {
        List<ServerResponse> result = serverResponseService.findByTimeRange(startTime, endTime);
        return ResponseEntity.ok(result);
    }

    // Endpoint per ottenere le risposte per un server specifico
    @GetMapping("/by-server/{server}")
    public ResponseEntity<List<ServerResponse>> findByServer(@PathVariable String server) {
        List<ServerResponse> result = serverResponseService.findByServer(server);
        return ResponseEntity.ok(result);
    }

    // Endpoint per ottenere le risposte per un endpoint specifico
    @GetMapping("/by-endpoint/{endpoint}")
    public ResponseEntity<List<ServerResponse>> findByEndpoint(@PathVariable String endpoint) {
        List<ServerResponse> result = serverResponseService.findByEndpoint(endpoint);
        return ResponseEntity.ok(result);
    }

    // Endpoint per ottenere il tempo medio di risposta per ogni server
    @GetMapping("/average-response-time")
    public ResponseEntity<Map<String, Double>> getAverageResponseTimeByServer() {
        Map<String, Double> result = serverResponseService.getAverageResponseTimeByServer();
        return ResponseEntity.ok(result);
    }

    // Endpoint per ottenere risposte combinate per server, endpoint e intervallo temporale
    @GetMapping("/server-endpoint-time-range")
    public ResponseEntity<List<ServerResponse>> findByServerEndpointAndTimeRange(
            @RequestParam String server,
            @RequestParam String endpoint,
            @RequestParam Instant startTime,
            @RequestParam Instant endTime) {
        List<ServerResponse> result = serverResponseService.findByServerEndpointAndTimeRange(
                server, endpoint, startTime, endTime);
        return ResponseEntity.ok(result);
    }

     // Endpoint per ottenere una lista di ServerResponse generati casualmente
     @GetMapping("/generateServerResponse")
     public List<ServerResponse> generateRandomServerResponse(
             @RequestParam(value = "count", defaultValue = "5") int count) {
         return serverResponseService.generateRandomServerResponseList(count);
     }
}
