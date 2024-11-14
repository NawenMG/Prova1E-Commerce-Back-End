package com.prova.e_commerce.dbTS.service;

import com.prova.e_commerce.dbTS.model.SalesMonitoring;
import com.prova.e_commerce.dbTS.repository.interfacce.SalesMonitoringRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class SalesMonitoringService {

    @Autowired
    private SalesMonitoringRep salesMonitoringRepository;

    // Inserisci un singolo monitoraggio delle vendite
    public void insert(SalesMonitoring salesMonitoring) {
        salesMonitoringRepository.insert(salesMonitoring);
    }

    // Inserisci un batch di monitoraggi delle vendite
    public void insertBatch(List<SalesMonitoring> salesMonitoringList) {
        salesMonitoringRepository.insertBatch(salesMonitoringList);
    }

    // Metodo per aggiornare un monitoraggio delle vendite
    public void update(SalesMonitoring oldSalesMonitoring, SalesMonitoring newSalesMonitoring) {
        salesMonitoringRepository.update(oldSalesMonitoring, newSalesMonitoring);
    }

    // Metodo per eliminare i monitoraggi delle vendite di un venditore
    public void deleteByTag(String venditore) {
        salesMonitoringRepository.deleteByTag(venditore);
    }

    // Metodo per trovare i monitoraggi delle vendite in un intervallo di tempo
    public List<SalesMonitoring> findByTimeRange(Instant startTime, Instant endTime) {
        return salesMonitoringRepository.findByTimeRange(startTime, endTime);
    }

    // Metodo per trovare i monitoraggi delle vendite di un prodotto specifico
    public List<SalesMonitoring> findByProduct(String prodotto) {
        return salesMonitoringRepository.findByProduct(prodotto);
    }

    // Metodo per trovare i monitoraggi delle vendite di un venditore specifico
    public List<SalesMonitoring> findByVendor(String venditore) {
        return salesMonitoringRepository.findByVendor(venditore);
    }

    // Metodo per trovare i monitoraggi delle vendite per una categoria di prodotto
    public List<SalesMonitoring> findByCategory(String categoriaProdotto) {
        return salesMonitoringRepository.findByCategory(categoriaProdotto);
    }

    // Metodo per ottenere il ricavo medio per prodotto
    public Map<String, Double> getAverageRevenueByProduct() {
        return salesMonitoringRepository.getAverageRevenueByProduct();
    }
}
