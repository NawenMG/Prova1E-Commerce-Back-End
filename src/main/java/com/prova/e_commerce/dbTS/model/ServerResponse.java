package com.prova.e_commerce.dbTS.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import java.time.Instant;

@Measurement(name = "ServerResponse")
public class ServerResponse {

    // Tag: Server
    @Column(tag = true)
    private String server;

    // Tag: Endpoint
    @Column(tag = true)
    private String endpoint;

    // Field: Tempo di risposta medio
    @Column
    private Double responseTimeAverage;

    // Field: Numero di richieste
    @Column
    private Integer numeroDiRequest;

    // Field: Numero di errori
    @Column
    private Integer numeroDiErrori;

    // Timestamp: gestito automaticamente
    @Column(timestamp = true)
    private Instant time;

    // Costruttore
    public ServerResponse(String server, String endpoint, Double responseTimeAverage, Integer numeroDiRequest, Integer numeroDiErrori, Instant time) {
        this.server = server;
        this.endpoint = endpoint;
        this.responseTimeAverage = responseTimeAverage;
        this.numeroDiRequest = numeroDiRequest;
        this.numeroDiErrori = numeroDiErrori;
        this.time = time;
    }

    // Getters e Setters
    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Double getResponseTimeAverage() {
        return responseTimeAverage;
    }

    public void setResponseTimeAverage(Double responseTimeAverage) {
        this.responseTimeAverage = responseTimeAverage;
    }

    public Integer getNumeroDiRequest() {
        return numeroDiRequest;
    }

    public void setNumeroDiRequest(Integer numeroDiRequest) {
        this.numeroDiRequest = numeroDiRequest;
    }

    public Integer getNumeroDiErrori() {
        return numeroDiErrori;
    }

    public void setNumeroDiErrori(Integer numeroDiErrori) {
        this.numeroDiErrori = numeroDiErrori;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
