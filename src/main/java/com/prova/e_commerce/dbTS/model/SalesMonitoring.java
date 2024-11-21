package com.prova.e_commerce.dbTS.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import java.time.Instant;

@Measurement(name = "SalesMonitoring")
public class SalesMonitoring {

    // Tag: Prodotto
    @Column(tag = true)
    private String prodotto;

    // Tag: Categoria prodotto
    @Column(tag = true)
    private String categoriaProdotto;

    // Tag: Venditore
    @Column(tag = true)
    private String venditore;

    // Field: Numero di ordini
    @Column
    private Integer numeroOrdini;

    // Field: Numero di unità vendute
    @Column
    private Integer numeroUnitaVendute;

    // Field: Ricavo
    @Column
    private Double ricavo;

    // Timestamp: gestito automaticamente
    @Column(timestamp = true)
    private Instant time;

    // Costruttore
    public SalesMonitoring(String prodotto, String categoriaProdotto, String venditore, Integer numeroOrdini, Integer numeroUnitaVendute, Double ricavo, Instant time) {
        this.prodotto = prodotto;
        this.categoriaProdotto = categoriaProdotto;
        this.venditore = venditore;
        this.numeroOrdini = numeroOrdini;
        this.numeroUnitaVendute = numeroUnitaVendute;
        this.ricavo = ricavo;
        this.time = time;
    }

    // Getters e Setters
    public String getProdotto() {
        return prodotto;
    }

    public void setProdotto(String prodotto) {
        this.prodotto = prodotto;
    }

    public String getCategoriaProdotto() {
        return categoriaProdotto;
    }

    public void setCategoriaProdotto(String categoriaProdotto) {
        this.categoriaProdotto = categoriaProdotto;
    }

    public String getVenditore() {
        return venditore;
    }

    public void setVenditore(String venditore) {
        this.venditore = venditore;
    }

    public Integer getNumeroOrdini() {
        return numeroOrdini;
    }

    public void setNumeroOrdini(Integer numeroOrdini) {
        this.numeroOrdini = numeroOrdini;
    }

    public Integer getNumeroUnitaVendute() {
        return numeroUnitaVendute;
    }

    public void setNumeroUnitaVendute(Integer numeroUnitaVendute) {
        this.numeroUnitaVendute = numeroUnitaVendute;
    }

    public Double getRicavo() {
        return ricavo;
    }

    public void setRicavo(Double ricavo) {
        this.ricavo = ricavo;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
