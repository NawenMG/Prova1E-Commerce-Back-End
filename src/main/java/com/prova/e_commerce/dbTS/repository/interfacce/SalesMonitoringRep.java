package com.prova.e_commerce.dbTS.repository.interfacce;

import com.prova.e_commerce.dbTS.model.SalesMonitoring;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface SalesMonitoringRep {

    // Inserisce un singolo monitoraggio delle vendite
    void insert(SalesMonitoring salesMonitoring);

    // Inserisce un batch di monitoraggi delle vendite
    void insertBatch(List<SalesMonitoring> salesMonitoringList);

    // Aggiorna un record (scrittura di un nuovo punto, poiché InfluxDB non supporta gli aggiornamenti tradizionali)
    void update(SalesMonitoring oldMonitoring, SalesMonitoring newMonitoring);

    // Elimina i record per un venditore specifico
    void deleteByTag(String venditore);

    // Recupera i record per un intervallo temporale
    List<SalesMonitoring> findByTimeRange(Instant startTime, Instant endTime);

    // Recupera i record per un prodotto specifico
    List<SalesMonitoring> findByProduct(String prodotto);

    // Recupera i record per un venditore specifico
    List<SalesMonitoring> findByVendor(String venditore);

    // Recupera i record per una categoria di prodotto specifica
    List<SalesMonitoring> findByCategory(String categoriaProdotto);

    // Recupera il ricavo medio per prodotto
    Map<String, Double> getAverageRevenueByProduct();

}
