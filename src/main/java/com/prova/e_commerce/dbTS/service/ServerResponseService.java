package com.prova.e_commerce.dbTS.service;

import com.prova.e_commerce.dbTS.model.ServerResponse;
import com.prova.e_commerce.dbTS.randomData.ServerResponseFaker;
import com.prova.e_commerce.dbTS.repository.interfacce.ServerResponseRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
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

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Kafka producer

    private static final String KAFKA_TOPIC_SERVER_RESPONSE_AGGIUNGI = "server-response-topic-aggiungi"; 
    private static final String KAFKA_TOPIC_SERVER_RESPONSE_AGGIORNA = "server-response-topic-aggiorna"; 


    // Inserisce una singola risposta del server e invalida la cache
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void insert(ServerResponse serverResponse) {
        serverResponseRep.insert(serverResponse);
        
        // Invia un messaggio Kafka per notificare l'inserimento della risposta del server
        kafkaTemplate.send(KAFKA_TOPIC_SERVER_RESPONSE_AGGIUNGI, "Nuova risposta del server inserita: " + serverResponse.getServer());
    }

    // Inserisce un batch di risposte del server e invalida la cache
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void insertBatch(List<ServerResponse> serverResponseList) {
        serverResponseRep.insertBatch(serverResponseList);
        
        // Invia un messaggio Kafka per notificare l'inserimento del batch di risposte
        kafkaTemplate.send(KAFKA_TOPIC_SERVER_RESPONSE_AGGIUNGI, "Batch di risposte del server inserito. Numero di voci: " + serverResponseList.size());
    }

    // Aggiorna una risposta del server (scrivendo un nuovo punto) e invalida la cache
    @CacheEvict(value = {"caffeine", "redis"}, key = "#oldResponse.id")
    public void update(ServerResponse oldResponse, ServerResponse newResponse) {
        serverResponseRep.update(oldResponse, newResponse);
        
        // Invia un messaggio Kafka per notificare l'aggiornamento della risposta del server
        kafkaTemplate.send(KAFKA_TOPIC_SERVER_RESPONSE_AGGIORNA, "Risposta del server aggiornata. ID: " + oldResponse.getServer() + " -> " + newResponse.getServer());
    }

    // Elimina tutte le risposte di un server specifico e invalida la cache
    @CacheEvict(value = {"caffeine", "redis"}, key = "#server")
    public void deleteByServer(String server) {
        serverResponseRep.deleteByServer(server);
    }

    // Recupera tutte le risposte del server in un intervallo temporale (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "'timeRange:' + #startTime.toString() + ':' + #endTime.toString()")
    public List<ServerResponse> findByTimeRange(Instant startTime, Instant endTime) {
        return serverResponseRep.findByTimeRange(startTime, endTime);
    }

    // Recupera tutte le risposte di un server specifico (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "#server")
    public List<ServerResponse> findByServer(String server) {
        return serverResponseRep.findByServer(server);
    }

    // Recupera tutte le risposte di un endpoint specifico (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "#endpoint")
    public List<ServerResponse> findByEndpoint(String endpoint) {
        return serverResponseRep.findByEndpoint(endpoint);
    }

    // Recupera il tempo medio di risposta per ogni server (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "'averageResponseTimeByServer'")
    public Map<String, Double> getAverageResponseTimeByServer() {
        return serverResponseRep.getAverageResponseTimeByServer();
    }

    // Recupera risposte combinate per server, endpoint e intervallo temporale (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "'server:' + #server + ':endpoint:' + #endpoint + ':timeRange:' + #startTime.toString() + ':' + #endTime.toString()")
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
