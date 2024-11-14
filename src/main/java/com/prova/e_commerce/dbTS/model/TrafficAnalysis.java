package com.prova.e_commerce.dbTS.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import java.time.Instant;

@Measurement(name = "TrafficAnalysis")
public class TrafficAnalysis {

    // Tag: URL della pagina
    @Column(tag = true)
    private String urlPagina;

    // Field: Numero di visite
    @Column
    private Integer numeroDiVisite;

    // Field: Numero di visitatori unici
    @Column
    private Integer numeroDiVisitatoriUnici;

    // Field: Durata media delle visite (in secondi o minuti, a scelta)
    @Column
    private Double durataMediaVisite;

    // Timestamp: gestito automaticamente
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
