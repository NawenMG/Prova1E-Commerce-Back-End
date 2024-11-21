package com.prova.e_commerce.dbTS.controller.rest;

import com.prova.e_commerce.dbTS.model.ServerResponse;
import com.prova.e_commerce.dbTS.service.ServerResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

     // Endpoint per ottenere una lista di ServerResponse generati casualmente
     @GetMapping("/generateServerResponse")
     public List<ServerResponse> generateRandomServerResponse(
             @RequestParam(value = "count", defaultValue = "5") int count) {
         return serverResponseService.generateRandomServerResponseList(count);
     }
}
