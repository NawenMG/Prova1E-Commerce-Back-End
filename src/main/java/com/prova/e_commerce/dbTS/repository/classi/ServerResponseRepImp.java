package com.prova.e_commerce.dbTS.repository.classi;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.prova.e_commerce.dbTS.model.ServerResponse;
import com.prova.e_commerce.dbTS.repository.interfacce.ServerResponseRep;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ServerResponseRepImp implements ServerResponseRep {

    private final InfluxDBClient influxDBClient;
    private final String bucket;

    public ServerResponseRepImp(InfluxDBClient influxDBClient,
                                 @Value("${influxdb.bucket}") String bucket) {
        this.influxDBClient = influxDBClient;
        this.bucket = bucket;
    }

    // Inserisci singola risposta del server
    public void insert(ServerResponse serverResponse) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

        // Creazione di un punto per InfluxDB
        writeApi.writePoint(
            Point.measurement("ServerResponse")
                .addTag("server", serverResponse.getServer())
                .addTag("endpoint", serverResponse.getEndpoint())
                .addField("responseTimeAverage", serverResponse.getResponseTimeAverage())
                .addField("numeroDiRequest", serverResponse.getNumeroDiRequest())
                .addField("numeroDiErrori", serverResponse.getNumeroDiErrori())
                .time(serverResponse.getTime(), WritePrecision.NS)
        );
    }

    // Inserisci un batch di risposte del server
    public void insertBatch(List<ServerResponse> serverResponseList) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        
        for (ServerResponse serverResponse : serverResponseList) {
            writeApi.writePoint(
                Point.measurement("ServerResponse")
                    .addTag("server", serverResponse.getServer())
                    .addTag("endpoint", serverResponse.getEndpoint())
                    .addField("responseTimeAverage", serverResponse.getResponseTimeAverage())
                    .addField("numeroDiRequest", serverResponse.getNumeroDiRequest())
                    .addField("numeroDiErrori", serverResponse.getNumeroDiErrori())
                    .time(serverResponse.getTime(), WritePrecision.NS)
            );
        }
    }

    // Metodo per aggiornare un record (scrittura di un nuovo punto, visto che InfluxDB non supporta aggiornamenti tradizionali)
    public void update(ServerResponse oldResponse, ServerResponse newResponse) {
        insert(newResponse); // In realtà, scriviamo un nuovo punto
    }

    // Elimina i record per un server specifico
    public void deleteByServer(String server) {
        // Query per eliminare tutte le serie che hanno il tag "server" con il valore specificato
        String dropSeriesQuery = String.format("""
            DROP SERIES FROM "ServerResponse"
            WHERE "server" = '%s'
        """, server);

        // Esegui la query di eliminazione
        QueryApi queryApi = influxDBClient.getQueryApi();
        queryApi.query(dropSeriesQuery, bucket);
    }

    // Query per un intervallo temporale
    public List<ServerResponse> findByTimeRange(Instant startTime, Instant endTime) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: %s, stop: %s)
                |> filter(fn: (r) => r["_measurement"] == "ServerResponse")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket,
            startTime.toString(),
            endTime.toString()
        );

        return executeQuery(flux);
    }

    // Query per un server specifico
    public List<ServerResponse> findByServer(String server) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "ServerResponse" and r["server"] == "%s")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket,
            server
        );

        return executeQuery(flux);
    }

    // Query per un endpoint specifico
    public List<ServerResponse> findByEndpoint(String endpoint) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "ServerResponse" and r["endpoint"] == "%s")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket,
            endpoint
        );

        return executeQuery(flux);
    }

    // Query per il tempo medio di risposta per server
    public Map<String, Double> getAverageResponseTimeByServer() {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "ServerResponse")
                |> group(columns: ["server"])
                |> mean(column: "responseTimeAverage")
            """,
            bucket
        );

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(flux, bucket);
        
        Map<String, Double> results = new HashMap<>();
        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                String server = record.getValueByKey("server").toString();
                Double avgResponseTime = (Double) record.getValue();
                results.put(server, avgResponseTime);
            }
        }
        
        return results;
    }

    // Query combinata per server, endpoint e intervallo temporale
    public List<ServerResponse> findByServerEndpointAndTimeRange(
            String server, 
            String endpoint, 
            Instant startTime, 
            Instant endTime) {
        
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: %s, stop: %s)
                |> filter(fn: (r) => r["_measurement"] == "ServerResponse" 
                    and r["server"] == "%s" 
                    and r["endpoint"] == "%s")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket,
            startTime.toString(),
            endTime.toString(),
            server,
            endpoint
        );

        return executeQuery(flux);
    }

    // Metodo helper per eseguire le query e mappare i risultati
    private List<ServerResponse> executeQuery(String flux) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(flux, bucket);
        List<ServerResponse> results = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                String server = record.getValueByKey("server") != null ? record.getValueByKey("server").toString() : "";
                String endpoint = record.getValueByKey("endpoint") != null ? record.getValueByKey("endpoint").toString() : "";
                Double responseTimeAverage = record.getValueByKey("responseTimeAverage") != null ? (Double) record.getValueByKey("responseTimeAverage") : 0.0;
                Integer numeroDiRequest = record.getValueByKey("numeroDiRequest") != null ? (Integer) record.getValueByKey("numeroDiRequest") : 0;
                Integer numeroDiErrori = record.getValueByKey("numeroDiErrori") != null ? (Integer) record.getValueByKey("numeroDiErrori") : 0;

                results.add(new ServerResponse(server, endpoint, responseTimeAverage, numeroDiRequest, numeroDiErrori, record.getTime()));
            }
        }

        return results;
    }
}
