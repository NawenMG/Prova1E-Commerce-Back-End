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
import com.prova.e_commerce.security.security1.SecurityUtils;

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

    @Override
    public void insert(SalesMonitoring salesMonitoring) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

        writeApi.writePoint(
            Point.measurement("SalesMonitoring")
                .addTag("userId", SecurityUtils.getCurrentUsername())
                .addTag("prodotto", salesMonitoring.getProdotto())
                .addTag("categoriaProdotto", salesMonitoring.getCategoriaProdotto())
                .addTag("venditore", salesMonitoring.getVenditore())
                .addField("numeroOrdini", salesMonitoring.getNumeroOrdini())
                .addField("numeroUnitaVendute", salesMonitoring.getNumeroUnitaVendute())
                .addField("ricavo", salesMonitoring.getRicavo())
                .time(salesMonitoring.getTime(), WritePrecision.NS)
        );
    }

    @Override
    public void insertBatch(List<SalesMonitoring> salesMonitoringList) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        List<Point> points = new ArrayList<>();
        
        for (SalesMonitoring salesMonitoring : salesMonitoringList) {
            points.add(Point.measurement("SalesMonitoring")
                .addTag("userId", SecurityUtils.getCurrentUsername())
                .addTag("prodotto", salesMonitoring.getProdotto())
                .addTag("categoriaProdotto", salesMonitoring.getCategoriaProdotto())
                .addTag("venditore", salesMonitoring.getVenditore())
                .addField("numeroOrdini", salesMonitoring.getNumeroOrdini())
                .addField("numeroUnitaVendute", salesMonitoring.getNumeroUnitaVendute())
                .addField("ricavo", salesMonitoring.getRicavo())
                .time(salesMonitoring.getTime(), WritePrecision.NS)
            );
        }
        writeApi.writePoints(points);
    }

    @Override
    public void update(SalesMonitoring oldMonitoring, SalesMonitoring newMonitoring) {
        insert(newMonitoring);
    }

    @Override
    public void deleteByTag(String venditore) {
        String dropSeriesQuery = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r[\"venditore\"] == \"%s\")
                |> drop(columns: [\"venditore\"])
            """, bucket, venditore);

        QueryApi queryApi = influxDBClient.getQueryApi();
        queryApi.query(dropSeriesQuery, bucket);
    }

    @Override
    public List<SalesMonitoring> findByTimeRange(Instant startTime, Instant endTime) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: %s, stop: %s)
                |> filter(fn: (r) => r["_measurement"] == "SalesMonitoring")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket, startTime.toString(), endTime.toString()
        );

        return executeQuery(flux);
    }

    @Override
    public List<SalesMonitoring> findByProduct(String prodotto) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "SalesMonitoring" and r["prodotto"] == "%s")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket, prodotto
        );

        return executeQuery(flux);
    }

    @Override
    public List<SalesMonitoring> findByVendor(String venditore) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "SalesMonitoring" and r["venditore"] == "%s")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket, venditore
        );

        return executeQuery(flux);
    }

    @Override
    public List<SalesMonitoring> findByCategory(String categoriaProdotto) {
        String flux = String.format("""
            from(bucket: "%s")
                |> range(start: -30d)
                |> filter(fn: (r) => r["_measurement"] == "SalesMonitoring" and r["categoriaProdotto"] == "%s")
                |> pivot(rowKey:["_time"], 
                        columnKey: ["_field"], 
                        valueColumn: "_value")
            """,
            bucket, categoriaProdotto
        );

        return executeQuery(flux);
    }

    @Override
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

    private List<SalesMonitoring> executeQuery(String flux) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(flux, bucket);
        List<SalesMonitoring> results = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                SalesMonitoring salesMonitoring = new SalesMonitoring(
                    record.getValueByKey("prodotto") != null ? record.getValueByKey("prodotto").toString() : "",
                    record.getValueByKey("categoriaProdotto") != null ? record.getValueByKey("categoriaProdotto").toString() : "",
                    record.getValueByKey("venditore") != null ? record.getValueByKey("venditore").toString() : "",
                    record.getValueByKey("numeroOrdini") != null ? Integer.parseInt(record.getValueByKey("numeroOrdini").toString()) : 0,
                    record.getValueByKey("numeroUnitaVendute") != null ? Integer.parseInt(record.getValueByKey("numeroUnitaVendute").toString()) : 0,
                    record.getValueByKey("ricavo") != null ? Double.parseDouble(record.getValueByKey("ricavo").toString()) : 0.0,
                    record.getTime()
                );
                results.add(salesMonitoring);
            }
        }

        return results;
    }
}
