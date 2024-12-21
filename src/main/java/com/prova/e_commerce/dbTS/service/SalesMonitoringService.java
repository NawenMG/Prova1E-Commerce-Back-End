package com.prova.e_commerce.dbTS.service;

import com.prova.e_commerce.dbTS.model.SalesMonitoring;
import com.prova.e_commerce.dbTS.randomData.SalesMonitoringFaker;
import com.prova.e_commerce.dbTS.repository.interfacce.SalesMonitoringRep;

import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SalesMonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(SalesMonitoringService.class);

    @Autowired
    private SalesMonitoringRep salesMonitoringRepository;

    @Autowired
    private SalesMonitoringFaker salesMonitoringFaker;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    private static final String KAFKA_TOPIC_SALES_AGGIUNGI = "sales-monitoring-topic-aggiungi";
    private static final String KAFKA_TOPIC_SALES_AGGIORNA = "sales-monitoring-topic-aggiorna";

    public SalesMonitoringService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.sales.insert.count");
        meterRegistry.counter("service.sales.update.count");
        meterRegistry.counter("service.sales.delete.count");
        meterRegistry.counter("service.sales.query.count");
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void insert(SalesMonitoring salesMonitoring) {
        logger.info("Inserimento monitoraggio vendite: {}", salesMonitoring.getProdotto());
        Span span = tracer.spanBuilder("insertSalesMonitoring").startSpan();
        try {
            meterRegistry.counter("service.sales.insert.count").increment();
            salesMonitoringRepository.insert(salesMonitoring);
            kafkaTemplate.send(KAFKA_TOPIC_SALES_AGGIUNGI, "Nuovo monitoraggio vendite inserito: " + salesMonitoring.getProdotto());
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void insertBatch(List<SalesMonitoring> salesMonitoringList) {
        logger.info("Inserimento batch di monitoraggi vendite. Numero di voci: {}", salesMonitoringList.size());
        Span span = tracer.spanBuilder("insertBatchSalesMonitoring").startSpan();
        try {
            meterRegistry.counter("service.sales.insert.count").increment(salesMonitoringList.size());
            salesMonitoringRepository.insertBatch(salesMonitoringList);
            kafkaTemplate.send(KAFKA_TOPIC_SALES_AGGIUNGI, "Batch di monitoraggi vendite inserito. Numero di voci: " + salesMonitoringList.size());
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#oldSalesMonitoring.id")
    public void update(SalesMonitoring oldSalesMonitoring, SalesMonitoring newSalesMonitoring) {
        logger.info("Aggiornamento monitoraggio vendite: {} -> {}", oldSalesMonitoring.getProdotto(), newSalesMonitoring.getProdotto());
        Span span = tracer.spanBuilder("updateSalesMonitoring").startSpan();
        try {
            meterRegistry.counter("service.sales.update.count").increment();
            salesMonitoringRepository.update(oldSalesMonitoring, newSalesMonitoring);
            kafkaTemplate.send(KAFKA_TOPIC_SALES_AGGIORNA, "Monitoraggio vendite aggiornato: " + oldSalesMonitoring.getProdotto() + " -> " + newSalesMonitoring.getProdotto());
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#venditore")
    public void deleteByTag(String venditore) {
        logger.info("Eliminazione monitoraggi vendite per venditore: {}", venditore);
        Span span = tracer.spanBuilder("deleteSalesByVendor").startSpan();
        try {
            meterRegistry.counter("service.sales.delete.count").increment();
            salesMonitoringRepository.deleteByTag(venditore);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "'timeRange:' + #startTime.toString() + ':' + #endTime.toString()")
    public List<SalesMonitoring> findByTimeRange(Instant startTime, Instant endTime) {
        logger.info("Ricerca monitoraggi vendite nell'intervallo di tempo: {} - {}", startTime, endTime);
        Span span = tracer.spanBuilder("findSalesByTimeRange").startSpan();
        try {
            meterRegistry.counter("service.sales.query.count").increment();
            return salesMonitoringRepository.findByTimeRange(startTime, endTime);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#prodotto")
    public List<SalesMonitoring> findByProduct(String prodotto) {
        logger.info("Ricerca monitoraggi vendite per prodotto: {}", prodotto);
        Span span = tracer.spanBuilder("findSalesByProduct").startSpan();
        try {
            meterRegistry.counter("service.sales.query.count").increment();
            return salesMonitoringRepository.findByProduct(prodotto);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#venditore")
    public List<SalesMonitoring> findByVendor(String venditore) {
        logger.info("Ricerca monitoraggi vendite per venditore: {}", venditore);
        Span span = tracer.spanBuilder("findSalesByVendor").startSpan();
        try {
            meterRegistry.counter("service.sales.query.count").increment();
            return salesMonitoringRepository.findByVendor(venditore);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#categoriaProdotto")
    public List<SalesMonitoring> findByCategory(String categoriaProdotto) {
        logger.info("Ricerca monitoraggi vendite per categoria prodotto: {}", categoriaProdotto);
        Span span = tracer.spanBuilder("findSalesByCategory").startSpan();
        try {
            meterRegistry.counter("service.sales.query.count").increment();
            return salesMonitoringRepository.findByCategory(categoriaProdotto);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "'averageRevenueByProduct'")
    public Map<String, Double> getAverageRevenueByProduct() {
        logger.info("Calcolo ricavo medio per prodotto");
        Span span = tracer.spanBuilder("getAverageRevenueByProduct").startSpan();
        try {
            meterRegistry.counter("service.sales.query.count").increment();
            return salesMonitoringRepository.getAverageRevenueByProduct();
        } finally {
            span.end();
        }
    }

    public List<SalesMonitoring> generateRandomSalesMonitoringList(int count) {
        logger.info("Generazione lista random di SalesMonitoring. Count: {}", count);
        Span span = tracer.spanBuilder("generateRandomSalesMonitoringList").startSpan();
        try {
            meterRegistry.counter("service.sales.random.generate.count").increment(count);
            List<SalesMonitoring> list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                list.add(salesMonitoringFaker.generateRandomSalesMonitoring());
            }
            return list;
        } finally {
            span.end();
        }
    }
}
