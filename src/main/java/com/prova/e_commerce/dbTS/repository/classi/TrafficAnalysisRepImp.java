package com.prova.e_commerce.dbTS.repository.classi;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.prova.e_commerce.dbTS.model.TrafficAnalysis;
import com.prova.e_commerce.dbTS.repository.interfacce.TrafficAnalysisRep;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TrafficAnalysisRepImp implements TrafficAnalysisRep {

    private final InfluxDBClient influxDBClient;
    private final String bucket;

    public TrafficAnalysisRepImp(InfluxDBClient influxDBClient,
                                 @Value("${influxdb.bucket}") String bucket) {
        this.influxDBClient = influxDBClient;
        this.bucket = bucket;
    }

    // Inserisci singola analisi
    public void insert(TrafficAnalysis analysis) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

        // Creazione di un punto per InfluxDB
        writeApi.writePoint(
            Point.measurement("TrafficAnalysis")
                .addTag("urlPagina", analysis.getUrlPagina())
                .addField("numeroDiVisite", analysis.getNumeroDiVisite())
                .addField("numeroDiVisitatoriUnici", analysis.getNumeroDiVisitatoriUnici())
                .addField("durataMediaVisite", analysis.getDurataMediaVisite())
                .time(analysis.getTime(), WritePrecision.NS)
        );
    }

    // Inserisci un batch di analisi
    public void insertBatch(List<TrafficAnalysis> analysisList) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        
        for (TrafficAnalysis analysis : analysisList) {
            writeApi.writePoint(
                Point.measurement("TrafficAnalysis")
                    .addTag("urlPagina", analysis.getUrlPagina())
                    .addField("numeroDiVisite", analysis.getNumeroDiVisite())
                    .addField("numeroDiVisitatoriUnici", analysis.getNumeroDiVisitatoriUnici())
                    .addField("durataMediaVisite", analysis.getDurataMediaVisite())
                    .time(analysis.getTime(), WritePrecision.NS)
            );
        }
    }

    // Elimina i record per una specifica URL della pagina
    public void deleteByUrl(String urlPagina) {
        // Query per eliminare tutte le serie che hanno il tag "urlPagina" con il valore specificato
        String dropSeriesQuery = String.format("""
            DROP SERIES FROM "TrafficAnalysis"
            WHERE "urlPagina" = '%s'
        """, urlPagina);

        // Esegui la query di eliminazione
        QueryApi queryApi = influxDBClient.getQueryApi();
        queryApi.query(dropSeriesQuery, bucket);
    }

    // Query per un intervallo temporale
    public List<TrafficAnalysis> findByTimeRange(Instant startTime, Instant endTime) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: %s, stop: %s)
                |> filter(fn: (r) => r["_measurement"] == "TrafficAnalysis")
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

    // Query per URL specifica
    public List<TrafficAnalysis> findByUrl(String urlPagina) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "TrafficAnalysis" and r["urlPagina"] == "%s")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket,
            urlPagina
        );

        return executeQuery(flux);
    }

    // Query per trovare il numero medio di visite per URL
    public List<TrafficAnalysis> getAverageVisitsByUrl() {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "TrafficAnalysis")
                |> group(columns: ["urlPagina"])
                |> mean(column: "numeroDiVisite")
            """,
            bucket
        );

        return executeQuery(flux);
    }

    // Metodo helper per eseguire le query e mappare i risultati
    private List<TrafficAnalysis> executeQuery(String flux) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(flux, bucket);
        List<TrafficAnalysis> results = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                String urlPagina = record.getValueByKey("urlPagina") != null ? record.getValueByKey("urlPagina").toString() : "";
                Integer numeroDiVisite = record.getValueByKey("numeroDiVisite") != null ? (Integer) record.getValue() : 0;
                Integer numeroDiVisitatoriUnici = record.getValueByKey("numeroDiVisitatoriUnici") != null ? (Integer) record.getValue() : 0;
                Double durataMediaVisite = record.getValueByKey("durataMediaVisite") != null ? (Double) record.getValue() : 0.0;

                results.add(new TrafficAnalysis(urlPagina, numeroDiVisite, numeroDiVisitatoriUnici, durataMediaVisite, record.getTime()));
            }
        }

        return results;
    }
}
