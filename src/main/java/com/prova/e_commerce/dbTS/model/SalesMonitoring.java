package com.prova.e_commerce.dbTS.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

@Measurement(name = "SalesMonitoring")
public class SalesMonitoring {

    @NotNull(message = "L'ID utente non può essere null")
    @Column(tag = true)
    private String userId;

    // Tag: Prodotto
    @NotBlank(message = "Il nome del prodotto non può essere vuoto")
    @Column(tag = true)
    private String prodotto;

    // Tag: Categoria prodotto
    @NotBlank(message = "La categoria del prodotto non può essere vuota")
    @Column(tag = true)
    private String categoriaProdotto;

    // Tag: Venditore
    @NotBlank(message = "Il venditore non può essere vuoto")
    @Column(tag = true)
    private String venditore;

    // Field: Numero di ordini
    @NotNull(message = "Il numero di ordini non può essere null")
    @Min(value = 0, message = "Il numero di ordini deve essere maggiore o uguale a 0")
    @Column
    private Integer numeroOrdini;

    // Field: Numero di unità vendute
    @NotNull(message = "Il numero di unità vendute non può essere null")
    @Min(value = 0, message = "Il numero di unità vendute deve essere maggiore o uguale a 0")
    @Column
    private Integer numeroUnitaVendute;

    // Field: Ricavo
    @NotNull(message = "Il ricavo non può essere null")
    @Positive(message = "Il ricavo deve essere positivo")
    @Column
    private Double ricavo;

    // Timestamp: gestito automaticamente
    @NotNull(message = "Il timestamp non può essere null")
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
    // Getters e Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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
