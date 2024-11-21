package com.prova.e_commerce.dbTS.repository.classi;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.prova.e_commerce.dbTS.model.SalesMonitoring;
import com.prova.e_commerce.dbTS.repository.interfacce.SalesMonitoringRep;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SalesMonitoringRepImp implements SalesMonitoringRep {

    private final InfluxDBClient influxDBClient;
    private final String bucket;

    public SalesMonitoringRepImp(InfluxDBClient influxDBClient,
                                  @Value("${influxdb.bucket}") String bucket) {
        this.influxDBClient = influxDBClient;
        this.bucket = bucket;
    }

    // Inserisci singolo monitoraggio vendite
    public void insert(SalesMonitoring salesMonitoring) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

        // Creazione di un punto per InfluxDB
        writeApi.writePoint(
            Point.measurement("SalesMonitoring")
                .addTag("prodotto", salesMonitoring.getProdotto())
                .addTag("categoriaProdotto", salesMonitoring.getCategoriaProdotto())
                .addTag("venditore", salesMonitoring.getVenditore())
                .addField("numeroOrdini", salesMonitoring.getNumeroOrdini())
                .addField("numeroUnitaVendute", salesMonitoring.getNumeroUnitaVendute())
                .addField("ricavo", salesMonitoring.getRicavo())
                .time(salesMonitoring.getTime(), WritePrecision.NS)
        );
    }

    // Inserisci un batch di monitoraggi vendite
    public void insertBatch(List<SalesMonitoring> salesMonitoringList) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        
        for (SalesMonitoring salesMonitoring : salesMonitoringList) {
            writeApi.writePoint(
                Point.measurement("SalesMonitoring")
                    .addTag("prodotto", salesMonitoring.getProdotto())
                    .addTag("categoriaProdotto", salesMonitoring.getCategoriaProdotto())
                    .addTag("venditore", salesMonitoring.getVenditore())
                    .addField("numeroOrdini", salesMonitoring.getNumeroOrdini())
                    .addField("numeroUnitaVendute", salesMonitoring.getNumeroUnitaVendute())
                    .addField("ricavo", salesMonitoring.getRicavo())
                    .time(salesMonitoring.getTime(), WritePrecision.NS)
            );
        }
    }

    // Metodo per aggiornare un record (scrittura di un nuovo punto, visto che InfluxDB non supporta aggiornamenti tradizionali)
    public void update(SalesMonitoring oldMonitoring, SalesMonitoring newMonitoring) {
        insert(newMonitoring); // In realtà, scriviamo un nuovo punto
    }

    // Elimina i record per un venditore specifico
    public void deleteByTag(String venditore) {
        // Query per eliminare tutte le serie che hanno il tag "venditore" con il valore specificato
        String dropSeriesQuery = String.format("""
            DROP SERIES FROM "SalesMonitoring"
            WHERE "venditore" = '%s'
        """, venditore);

        // Esegui la query di eliminazione
        QueryApi queryApi = influxDBClient.getQueryApi();
        queryApi.query(dropSeriesQuery, bucket);
    }

    // Query per un intervallo temporale
    public List<SalesMonitoring> findByTimeRange(Instant startTime, Instant endTime) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: %s, stop: %s)
                |> filter(fn: (r) => r["_measurement"] == "SalesMonitoring")
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

    // Query per prodotto specifico
    public List<SalesMonitoring> findByProduct(String prodotto) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "SalesMonitoring" and r["prodotto"] == "%s")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket,
            prodotto
        );

        return executeQuery(flux);
    }

    // Query per venditore
    public List<SalesMonitoring> findByVendor(String venditore) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "SalesMonitoring" and r["venditore"] == "%s")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket,
            venditore
        );

        return executeQuery(flux);
    }

    // Query per categoria prodotto
    public List<SalesMonitoring> findByCategory(String categoriaProdotto) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "SalesMonitoring" and r["categoriaProdotto"] == "%s")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket,
            categoriaProdotto
        );

        return executeQuery(flux);
    }

    // Query per ricavo medio per prodotto
    public Map<String, Double> getAverageRevenueByProduct() {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "SalesMonitoring")
                |> group(columns: ["prodotto"])
                |> mean(column: "ricavo")
            """,
            bucket
        );

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(flux, bucket);
        
        Map<String, Double> results = new HashMap<>();
        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                String prodotto = record.getValueByKey("prodotto").toString();
                Double avgRevenue = (Double) record.getValue();
                results.put(prodotto, avgRevenue);
            }
        }
        
        return results;
    }

    // Metodo helper per eseguire le query e mappare i risultati
    private List<SalesMonitoring> executeQuery(String flux) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(flux, bucket);
        List<SalesMonitoring> results = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                String prodotto = record.getValueByKey("prodotto") != null ? record.getValueByKey("prodotto").toString() : "";
                String categoriaProdotto = record.getValueByKey("categoriaProdotto") != null ? record.getValueByKey("categoriaProdotto").toString() : "";
                String venditore = record.getValueByKey("venditore") != null ? record.getValueByKey("venditore").toString() : "";
                Integer numeroOrdini = record.getValueByKey("numeroOrdini") != null ? (Integer) record.getValueByKey("numeroOrdini") : 0;
                Integer numeroUnitaVendute = record.getValueByKey("numeroUnitaVendute") != null ? (Integer) record.getValueByKey("numeroUnitaVendute") : 0;
                Double ricavo = record.getValueByKey("ricavo") != null ? (Double) record.getValueByKey("ricavo") : 0.0;

                results.add(new SalesMonitoring(prodotto, categoriaProdotto, venditore, numeroOrdini, numeroUnitaVendute, ricavo, record.getTime()));
            }
        }

        return results;
    }
}
