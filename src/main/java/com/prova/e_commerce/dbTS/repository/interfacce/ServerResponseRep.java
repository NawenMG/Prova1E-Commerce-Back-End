package com.prova.e_commerce.dbTS.repository.interfacce;

import com.prova.e_commerce.dbTS.model.ServerResponse;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface ServerResponseRep {

    // Inserisce una singola risposta del server
    void insert(ServerResponse serverResponse);

    // Inserisce un batch di risposte del server
    void insertBatch(List<ServerResponse> serverResponseList);

    // Metodo per aggiornare una risposta (scrittura di un nuovo punto, visto che InfluxDB non supporta aggiornamenti tradizionali)
    void update(ServerResponse oldResponse, ServerResponse newResponse);

    // Elimina i record per un server specifico
    void deleteByServer(String server);

    // Query per ottenere risposte in un intervallo temporale
    List<ServerResponse> findByTimeRange(Instant startTime, Instant endTime);

    // Query per ottenere risposte per un server specifico
    List<ServerResponse> findByServer(String server);

    // Query per ottenere risposte per un endpoint specifico
    List<ServerResponse> findByEndpoint(String endpoint);

    // Query per ottenere il tempo medio di risposta per ogni server
    Map<String, Double> getAverageResponseTimeByServer();

    // Query combinata per server, endpoint e intervallo temporale
    List<ServerResponse> findByServerEndpointAndTimeRange(
            String server, 
            String endpoint, 
            Instant startTime, 
            Instant endTime);
}
