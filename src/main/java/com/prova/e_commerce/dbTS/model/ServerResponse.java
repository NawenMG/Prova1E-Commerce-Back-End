package com.prova.e_commerce.dbTS.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Instant;

@Measurement(name = "ServerResponse")
public class ServerResponse {

    // Tag: Server
    @NotBlank(message = "Il nome del server non può essere vuoto")
    @Column(tag = true)
    private String server;

    // Tag: Endpoint
    @NotBlank(message = "Il nome dell'endpoint non può essere vuoto")
    @Column(tag = true)
    private String endpoint;

    // Field: Tempo di risposta medio
    @NotNull(message = "Il tempo di risposta medio non può essere null")
    @Positive(message = "Il tempo di risposta medio deve essere positivo")
    @Column
    private Double responseTimeAverage;

    // Field: Numero di richieste
    @NotNull(message = "Il numero di richieste non può essere null")
    @PositiveOrZero(message = "Il numero di richieste deve essere maggiore o uguale a 0")
    @Column
    private Integer numeroDiRequest;

    // Field: Numero di errori
    @NotNull(message = "Il numero di errori non può essere null")
    @PositiveOrZero(message = "Il numero di errori deve essere maggiore o uguale a 0")
    @Column
    private Integer numeroDiErrori;

    // Timestamp: gestito automaticamente
    @NotNull(message = "Il timestamp non può essere null")
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
