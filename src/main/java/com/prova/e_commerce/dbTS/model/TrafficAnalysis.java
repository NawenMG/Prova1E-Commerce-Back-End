package com.prova.e_commerce.dbTS.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Instant;

@Measurement(name = "TrafficAnalysis")
public class TrafficAnalysis {

    // Tag: URL della pagina
    @NotBlank(message = "L'URL della pagina non può essere vuoto")
    @Column(tag = true)
    private String urlPagina;

    // Field: Numero di visite
    @NotNull(message = "Il numero di visite non può essere null")
    @PositiveOrZero(message = "Il numero di visite deve essere maggiore o uguale a 0")
    @Column
    private Integer numeroDiVisite;

    // Field: Numero di visitatori unici
    @NotNull(message = "Il numero di visitatori unici non può essere null")
    @PositiveOrZero(message = "Il numero di visitatori unici deve essere maggiore o uguale a 0")
    @Column
    private Integer numeroDiVisitatoriUnici;

    // Field: Durata media delle visite (in secondi o minuti)
    @NotNull(message = "La durata media delle visite non può essere null")
    @Positive(message = "La durata media delle visite deve essere un valore positivo")
    @Column
    private Double durataMediaVisite;

    // Timestamp: gestito automaticamente
    @NotNull(message = "Il timestamp non può essere null")
    @Column(timestamp = true)
    private Instant time;

    // Costruttore
    public TrafficAnalysis(String urlPagina, Integer numeroDiVisite, Integer numeroDiVisitatoriUnici, Double durataMediaVisite, Instant time) {
        this.urlPagina = urlPagina;
        this.numeroDiVisite = numeroDiVisite;
        this.numeroDiVisitatoriUnici = numeroDiVisitatoriUnici;
        this.durataMediaVisite = durataMediaVisite;
        this.time = time;
    }

    // Getters e Setters
    public String getUrlPagina() {
        return urlPagina;
    }

    public void setUrlPagina(String urlPagina) {
        this.urlPagina = urlPagina;
    }

    public Integer getNumeroDiVisite() {
        return numeroDiVisite;
    }

    public void setNumeroDiVisite(Integer numeroDiVisite) {
        this.numeroDiVisite = numeroDiVisite;
    }

    public Integer getNumeroDiVisitatoriUnici() {
        return numeroDiVisitatoriUnici;
    }

    public void setNumeroDiVisitatoriUnici(Integer numeroDiVisitatoriUnici) {
        this.numeroDiVisitatoriUnici = numeroDiVisitatoriUnici;
    }

    public Double getDurataMediaVisite() {
        return durataMediaVisite;
    }

    public void setDurataMediaVisite(Double durataMediaVisite) {
        this.durataMediaVisite = durataMediaVisite;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
