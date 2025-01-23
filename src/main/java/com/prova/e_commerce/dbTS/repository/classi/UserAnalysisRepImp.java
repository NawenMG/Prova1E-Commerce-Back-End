package com.prova.e_commerce.dbTS.repository.classi;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.prova.e_commerce.dbTS.model.UserAnalysis;
import com.prova.e_commerce.dbTS.repository.interfacce.UserAnalysisRep;
import com.prova.e_commerce.security.security1.SecurityUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserAnalysisRepImp implements UserAnalysisRep {

    private final InfluxDBClient influxDBClient;
    private final String bucket;

    public UserAnalysisRepImp(InfluxDBClient influxDBClient,
                              @Value("${influxdb.bucket}") String bucket) {
        this.influxDBClient = influxDBClient;
        this.bucket = bucket;
    }

    // Inserisci singola analisi
    public void insert(UserAnalysis analysis) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

        // Creazione di un punto per InfluxDB
        writeApi.writePoint(
            Point.measurement("UserAnalysis")
                .addTag("userId", SecurityUtils.getCurrentUsername())
                .addTag("utente", analysis.getUtente())
                .addTag("tipoDiDispositivo", analysis.getTipoDiDispositivo())
                .addTag("azione", analysis.getAzione())
                .addField("durataAzione", analysis.getDurataAzione())
                .time(analysis.getTime(), WritePrecision.NS)
        );
    }

    // Inserisci un batch di analisi
    public void insertBatch(List<UserAnalysis> analysisList) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        
        for (UserAnalysis analysis : analysisList) {
            writeApi.writePoint(
                Point.measurement("UserAnalysis")
                    .addTag("userId", SecurityUtils.getCurrentUsername())
                    .addTag("utente", analysis.getUtente())
                    .addTag("tipoDiDispositivo", analysis.getTipoDiDispositivo())
                    .addTag("azione", analysis.getAzione())
                    .addField("durataAzione", analysis.getDurataAzione())
                    .time(analysis.getTime(), WritePrecision.NS)
            );
        }
    }

    // Metodo per aggiornare un record (scrittura di un nuovo punto, visto che InfluxDB non supporta aggiornamenti tradizionali)
    public void update(UserAnalysis oldAnalysis, UserAnalysis newAnalysis) {
        insert(newAnalysis); // In realtà, scriviamo un nuovo punto
    }

    // Elimina i record per un utente specifico
    public void deleteByTag(String utente) {
        // Query per eliminare tutte le serie che hanno il tag "utente" con il valore specificato
        String dropSeriesQuery = String.format("""
            DROP SERIES FROM "UserAnalysis"
            WHERE "utente" = '%s'
        """, utente);

        // Esegui la query di eliminazione
        QueryApi queryApi = influxDBClient.getQueryApi();
        queryApi.query(dropSeriesQuery, bucket);
    }

    // Query per un intervallo temporale
    public List<UserAnalysis> findByTimeRange(Instant startTime, Instant endTime) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: %s, stop: %s)
                |> filter(fn: (r) => r["_measurement"] == "UserAnalysis")
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

    // Query per un utente specifico
    public List<UserAnalysis> findByUser(String utente) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "UserAnalysis" and r["utente"] == "%s")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket,
            utente
        );

        return executeQuery(flux);
    }

    // Query per tipo di dispositivo
    public List<UserAnalysis> findByDeviceType(String tipoDiDispositivo) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "UserAnalysis" and r["tipoDiDispositivo"] == "%s")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket,
            tipoDiDispositivo
        );

        return executeQuery(flux);
    }

    // Query per tipo di azione
    public List<UserAnalysis> findByAction(String azione) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "UserAnalysis" and r["azione"] == "%s")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket,
            azione
        );

        return executeQuery(flux);
    }

    // Query per durata media delle azioni per utente
    public Map<String, Double> getAverageDurationByUser() {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "UserAnalysis")
                |> group(columns: ["utente"])
                |> mean(column: "durataAzione")
            """,
            bucket
        );

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(flux, bucket);
        
        Map<String, Double> results = new HashMap<>();
        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                String utente = record.getValueByKey("utente").toString();
                Double avgDuration = (Double) record.getValue();
                results.put(utente, avgDuration);
            }
        }
        
        return results;
    }

    // Query combinata per utente, dispositivo e intervallo temporale
    public List<UserAnalysis> findByUserDeviceAndTimeRange(
            String utente, 
            String tipoDiDispositivo, 
            Instant startTime, 
            Instant endTime) {
        
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: %s, stop: %s)
                |> filter(fn: (r) => r["_measurement"] == "UserAnalysis" 
                    and r["utente"] == "%s" 
                    and r["tipoDiDispositivo"] == "%s")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket,
            startTime.toString(),
            endTime.toString(),
            utente,
            tipoDiDispositivo
        );

        return executeQuery(flux);
    }

    // Metodo helper per eseguire le query e mappare i risultati
    private List<UserAnalysis> executeQuery(String flux) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(flux, bucket);
        List<UserAnalysis> results = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                String utente = record.getValueByKey("utente") != null ? record.getValueByKey("utente").toString() : "";
                String tipoDiDispositivo = record.getValueByKey("tipoDiDispositivo") != null ? record.getValueByKey("tipoDiDispositivo").toString() : "";
                String azione = record.getValueByKey("azione") != null ? record.getValueByKey("azione").toString() : "";
                Double durataAzione = record.getValueByKey("durataAzione") != null ? (Double) record.getValueByKey("durataAzione") : 0.0;

                results.add(new UserAnalysis(utente, tipoDiDispositivo, azione, durataAzione, record.getTime()));
            }
        }

        return results;
    }
}
