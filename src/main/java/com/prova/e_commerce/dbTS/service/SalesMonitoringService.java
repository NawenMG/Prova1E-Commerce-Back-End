package com.prova.e_commerce.dbTS.service;

import com.prova.e_commerce.dbTS.model.SalesMonitoring;
import com.prova.e_commerce.dbTS.randomData.SalesMonitoringFaker;
import com.prova.e_commerce.dbTS.repository.interfacce.SalesMonitoringRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SalesMonitoringService {

    @Autowired
    private SalesMonitoringRep salesMonitoringRepository;

    @Autowired
    private SalesMonitoringFaker salesMonitoringFaker;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Kafka producer

    private static final String KAFKA_TOPIC_SALES_AGGIUNGI = "sales-monitoring-topic-aggiungi"; 
    private static final String KAFKA_TOPIC_SALES_AGGIORNA = "sales-monitoring-topic-aggiorna";

    // Inserisci un singolo monitoraggio delle vendite
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void insert(SalesMonitoring salesMonitoring) {
        salesMonitoringRepository.insert(salesMonitoring);
        
        // Invia un messaggio Kafka per notificare l'inserimento del monitoraggio delle vendite
        kafkaTemplate.send(KAFKA_TOPIC_SALES_AGGIUNGI, "Nuovo monitoraggio vendite inserito: " + salesMonitoring.getProdotto());
    }

    // Inserisci un batch di monitoraggi delle vendite
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void insertBatch(List<SalesMonitoring> salesMonitoringList) {
        salesMonitoringRepository.insertBatch(salesMonitoringList);
        
        // Invia un messaggio Kafka per notificare l'inserimento del batch di monitoraggi
        kafkaTemplate.send(KAFKA_TOPIC_SALES_AGGIUNGI, "Batch di monitoraggi vendite inserito. Numero di voci: " + salesMonitoringList.size());
    }

    // Metodo per aggiornare un monitoraggio delle vendite
    @CacheEvict(value = {"caffeine", "redis"}, key = "#oldSalesMonitoring.id")
    public void update(SalesMonitoring oldSalesMonitoring, SalesMonitoring newSalesMonitoring) {
        salesMonitoringRepository.update(oldSalesMonitoring, newSalesMonitoring);
        
        // Invia un messaggio Kafka per notificare l'aggiornamento del monitoraggio delle vendite
        kafkaTemplate.send(KAFKA_TOPIC_SALES_AGGIORNA, "Monitoraggio vendite aggiornato. ID: " + oldSalesMonitoring.getProdotto() + " -> " + newSalesMonitoring.getProdotto());
    }

    // Metodo per eliminare i monitoraggi delle vendite di un venditore
    @CacheEvict(value = {"caffeine", "redis"}, key = "#venditore")
    public void deleteByTag(String venditore) {
        salesMonitoringRepository.deleteByTag(venditore);
    }

    // Metodo per trovare i monitoraggi delle vendite in un intervallo di tempo (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "'timeRange:' + #startTime.toString() + ':' + #endTime.toString()")
    public List<SalesMonitoring> findByTimeRange(Instant startTime, Instant endTime) {
        return salesMonitoringRepository.findByTimeRange(startTime, endTime);
    }

    // Metodo per trovare i monitoraggi delle vendite di un prodotto specifico (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "#prodotto")
    public List<SalesMonitoring> findByProduct(String prodotto) {
        return salesMonitoringRepository.findByProduct(prodotto);
    }

    // Metodo per trovare i monitoraggi delle vendite di un venditore specifico (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "#venditore")
    public List<SalesMonitoring> findByVendor(String venditore) {
        return salesMonitoringRepository.findByVendor(venditore);
    }

    // Metodo per trovare i monitoraggi delle vendite per una categoria di prodotto (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "#categoriaProdotto")
    public List<SalesMonitoring> findByCategory(String categoriaProdotto) {
        return salesMonitoringRepository.findByCategory(categoriaProdotto);
    }

    // Metodo per ottenere il ricavo medio per prodotto (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "'averageRevenueByProduct'")
    public Map<String, Double> getAverageRevenueByProduct() {
        return salesMonitoringRepository.getAverageRevenueByProduct();
    }

    // Metodo per generare una lista di SalesMonitoring casuali
    public List<SalesMonitoring> generateRandomSalesMonitoringList(int count) {
        List<SalesMonitoring> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(salesMonitoringFaker.generateRandomSalesMonitoring());
        }
        return list;
    }
}
