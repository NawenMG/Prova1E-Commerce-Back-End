package com.prova.e_commerce.dbTS.service;

import com.prova.e_commerce.dbTS.model.ServerResponse;
import com.prova.e_commerce.dbTS.randomData.ServerResponseFaker;
import com.prova.e_commerce.dbTS.repository.interfacce.ServerResponseRep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ServerResponseService {

    @Autowired
    private ServerResponseRep serverResponseRep;

    @Autowired
    private ServerResponseFaker serverResponseFaker;
    
        // Inserisce una singola risposta del server
        public void insert(ServerResponse serverResponse) {
            serverResponseRep.insert(serverResponse);
        }
    
        // Inserisce un batch di risposte del server
        public void insertBatch(List<ServerResponse> serverResponseList) {
            serverResponseRep.insertBatch(serverResponseList);
        }
    
        // Aggiorna una risposta del server (scrivendo un nuovo punto)
        public void update(ServerResponse oldResponse, ServerResponse newResponse) {
            serverResponseRep.update(oldResponse, newResponse);
        }
    
        // Elimina tutte le risposte di un server specifico
        public void deleteByServer(String server) {
            serverResponseRep.deleteByServer(server);
        }
    
        // Recupera tutte le risposte del server in un intervallo temporale
        public List<ServerResponse> findByTimeRange(Instant startTime, Instant endTime) {
            return serverResponseRep.findByTimeRange(startTime, endTime);
        }
    
        // Recupera tutte le risposte di un server specifico
        public List<ServerResponse> findByServer(String server) {
            return serverResponseRep.findByServer(server);
        }
    
        // Recupera tutte le risposte di un endpoint specifico
        public List<ServerResponse> findByEndpoint(String endpoint) {
            return serverResponseRep.findByEndpoint(endpoint);
        }
    
        // Recupera il tempo medio di risposta per ogni server
        public Map<String, Double> getAverageResponseTimeByServer() {
            return serverResponseRep.getAverageResponseTimeByServer();
        }
    
        // Recupera risposte combinate per server, endpoint e intervallo temporale
        public List<ServerResponse> findByServerEndpointAndTimeRange(
                String server, String endpoint, Instant startTime, Instant endTime) {
            return serverResponseRep.findByServerEndpointAndTimeRange(server, endpoint, startTime, endTime);
        }
    
        // Metodo per generare una lista di ServerResponse casuali
        public List<ServerResponse> generateRandomServerResponseList(int count) {
            List<ServerResponse> list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                list.add(serverResponseFaker.generateRandomServerResponse());
        } 
        return list;
    }
}
