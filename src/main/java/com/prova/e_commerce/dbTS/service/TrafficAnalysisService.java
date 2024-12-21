package com.prova.e_commerce.dbTS.service;

import com.prova.e_commerce.dbTS.model.TrafficAnalysis;
import com.prova.e_commerce.dbTS.randomData.TrafficAnalysisFaker;
import com.prova.e_commerce.dbTS.repository.interfacce.TrafficAnalysisRep;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TrafficAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(TrafficAnalysisService.class);

    @Autowired
    private TrafficAnalysisRep trafficAnalysisRep;

    @Autowired
    private TrafficAnalysisFaker trafficAnalysisFaker;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    private static final String KAFKA_TOPIC_TRAFFIC_ANALYSIS_AGGIUNGI = "traffic-analysis-topic-aggiungi";

    public TrafficAnalysisService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.trafficanalysis.insert.count");
        meterRegistry.counter("service.trafficanalysis.query.count");
        meterRegistry.counter("service.trafficanalysis.delete.count");
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void insert(TrafficAnalysis analysis) {
        logger.info("Inserimento analisi del traffico: {}", analysis.getUrlPagina());
        Span span = tracer.spanBuilder("insertTrafficAnalysis").startSpan();
        try {
            meterRegistry.counter("service.trafficanalysis.insert.count").increment();
            trafficAnalysisRep.insert(analysis);
            kafkaTemplate.send(KAFKA_TOPIC_TRAFFIC_ANALYSIS_AGGIUNGI, "Nuova analisi del traffico inserita: " + analysis.getUrlPagina());
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void insertBatch(List<TrafficAnalysis> analysisList) {
        logger.info("Inserimento batch di analisi del traffico. Numero di voci: {}", analysisList.size());
        Span span = tracer.spanBuilder("insertBatchTrafficAnalysis").startSpan();
        try {
            meterRegistry.counter("service.trafficanalysis.insert.count").increment(analysisList.size());
            trafficAnalysisRep.insertBatch(analysisList);
            kafkaTemplate.send(KAFKA_TOPIC_TRAFFIC_ANALYSIS_AGGIUNGI, "Batch di analisi del traffico inserito. Numero di voci: " + analysisList.size());
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#urlPagina")
    public void deleteByUrl(String urlPagina) {
        logger.info("Eliminazione analisi del traffico per URL: {}", urlPagina);
        Span span = tracer.spanBuilder("deleteTrafficAnalysisByUrl").startSpan();
        try {
            meterRegistry.counter("service.trafficanalysis.delete.count").increment();
            trafficAnalysisRep.deleteByUrl(urlPagina);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "'timeRange:' + #startTime.toString() + ':' + #endTime.toString()")
    public List<TrafficAnalysis> findByTimeRange(Instant startTime, Instant endTime) {
        logger.info("Ricerca analisi del traffico nell'intervallo temporale: {} - {}", startTime, endTime);
        Span span = tracer.spanBuilder("findTrafficAnalysisByTimeRange").startSpan();
        try {
            meterRegistry.counter("service.trafficanalysis.query.count").increment();
            return trafficAnalysisRep.findByTimeRange(startTime, endTime);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#urlPagina")
    public List<TrafficAnalysis> findByUrl(String urlPagina) {
        logger.info("Ricerca analisi del traffico per URL: {}", urlPagina);
        Span span = tracer.spanBuilder("findTrafficAnalysisByUrl").startSpan();
        try {
            meterRegistry.counter("service.trafficanalysis.query.count").increment();
            return trafficAnalysisRep.findByUrl(urlPagina);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "'averageVisitsByUrl'")
    public List<TrafficAnalysis> getAverageVisitsByUrl() {
        logger.info("Calcolo visite medie per URL");
        Span span = tracer.spanBuilder("getAverageVisitsByUrl").startSpan();
        try {
            meterRegistry.counter("service.trafficanalysis.query.count").increment();
            return trafficAnalysisRep.getAverageVisitsByUrl();
        } finally {
            span.end();
        }
    }

    public List<TrafficAnalysis> generateRandomTrafficAnalysisList(int count) {
        logger.info("Generazione lista random di TrafficAnalysis. Count: {}", count);
        Span span = tracer.spanBuilder("generateRandomTrafficAnalysisList").startSpan();
        try {
            meterRegistry.counter("service.trafficanalysis.random.generate.count").increment(count);
            List<TrafficAnalysis> list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                list.add(trafficAnalysisFaker.generateRandomTrafficAnalysis());
            }
            return list;
        } finally {
            span.end();
        }
    }
}
