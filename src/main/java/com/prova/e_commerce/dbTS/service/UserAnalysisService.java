package com.prova.e_commerce.dbTS.service;

import com.prova.e_commerce.dbTS.model.UserAnalysis;
import com.prova.e_commerce.dbTS.randomData.UserAnalysisFaker;
import com.prova.e_commerce.dbTS.repository.interfacce.UserAnalysisRep;

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
public class UserAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(UserAnalysisService.class);

    @Autowired
    private UserAnalysisRep userAnalysisRepository;

    @Autowired
    private UserAnalysisFaker userAnalysisFaker;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    private static final String KAFKA_TOPIC_USER_ANALYSIS_AGGIUNGI = "user-analysis-topic-aggiungi";
    private static final String KAFKA_TOPIC_USER_ANALYSIS_AGGIORNA = "user-analysis-topic-aggiorna";

    public UserAnalysisService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.useranalysis.insert.count");
        meterRegistry.counter("service.useranalysis.query.count");
        meterRegistry.counter("service.useranalysis.delete.count");
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void insert(UserAnalysis analysis) {
        logger.info("Inserimento analisi utente: {}", analysis.getUtente());
        Span span = tracer.spanBuilder("insertUserAnalysis").startSpan();
        try {
            meterRegistry.counter("service.useranalysis.insert.count").increment();
            userAnalysisRepository.insert(analysis);
            kafkaTemplate.send(KAFKA_TOPIC_USER_ANALYSIS_AGGIUNGI, "Nuova analisi utente inserita per l'utente: " + analysis.getUtente());
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void insertBatch(List<UserAnalysis> analysisList) {
        logger.info("Inserimento batch di analisi utente. Numero di voci: {}", analysisList.size());
        Span span = tracer.spanBuilder("insertBatchUserAnalysis").startSpan();
        try {
            meterRegistry.counter("service.useranalysis.insert.count").increment(analysisList.size());
            userAnalysisRepository.insertBatch(analysisList);
            kafkaTemplate.send(KAFKA_TOPIC_USER_ANALYSIS_AGGIUNGI, "Batch di analisi utente inserito. Numero di voci: " + analysisList.size());
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void update(UserAnalysis oldAnalysis, UserAnalysis newAnalysis) {
        logger.info("Aggiornamento analisi utente: {} -> {}", oldAnalysis.getUtente(), newAnalysis.getUtente());
        Span span = tracer.spanBuilder("updateUserAnalysis").startSpan();
        try {
            meterRegistry.counter("service.useranalysis.update.count").increment();
            userAnalysisRepository.update(oldAnalysis, newAnalysis);
            kafkaTemplate.send(KAFKA_TOPIC_USER_ANALYSIS_AGGIORNA, "Analisi utente aggiornata per l'utente: " + oldAnalysis.getUtente());
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#utente")
    public void deleteByTag(String utente) {
        logger.info("Eliminazione analisi utente per tag: {}", utente);
        Span span = tracer.spanBuilder("deleteUserAnalysisByTag").startSpan();
        try {
            meterRegistry.counter("service.useranalysis.delete.count").increment();
            userAnalysisRepository.deleteByTag(utente);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "'timeRange:' + #startTime.toString() + ':' + #endTime.toString()")
    public List<UserAnalysis> findByTimeRange(Instant startTime, Instant endTime) {
        logger.info("Ricerca analisi utente nell'intervallo temporale: {} - {}", startTime, endTime);
        Span span = tracer.spanBuilder("findUserAnalysisByTimeRange").startSpan();
        try {
            meterRegistry.counter("service.useranalysis.query.count").increment();
            return userAnalysisRepository.findByTimeRange(startTime, endTime);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#utente")
    public List<UserAnalysis> findByUser(String utente) {
        logger.info("Ricerca analisi utente per utente: {}", utente);
        Span span = tracer.spanBuilder("findUserAnalysisByUser").startSpan();
        try {
            meterRegistry.counter("service.useranalysis.query.count").increment();
            return userAnalysisRepository.findByUser(utente);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#tipoDiDispositivo")
    public List<UserAnalysis> findByDeviceType(String tipoDiDispositivo) {
        logger.info("Ricerca analisi utente per tipo di dispositivo: {}", tipoDiDispositivo);
        Span span = tracer.spanBuilder("findUserAnalysisByDeviceType").startSpan();
        try {
            meterRegistry.counter("service.useranalysis.query.count").increment();
            return userAnalysisRepository.findByDeviceType(tipoDiDispositivo);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#azione")
    public List<UserAnalysis> findByAction(String azione) {
        logger.info("Ricerca analisi utente per azione: {}", azione);
        Span span = tracer.spanBuilder("findUserAnalysisByAction").startSpan();
        try {
            meterRegistry.counter("service.useranalysis.query.count").increment();
            return userAnalysisRepository.findByAction(azione);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "'averageDurationByUser'")
    public Map<String, Double> getAverageDurationByUser() {
        logger.info("Calcolo durata media per utente");
        Span span = tracer.spanBuilder("getAverageDurationByUser").startSpan();
        try {
            meterRegistry.counter("service.useranalysis.query.count").increment();
            return userAnalysisRepository.getAverageDurationByUser();
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "'userDeviceTimeRange:' + #utente + ':' + #tipoDiDispositivo + ':' + #startTime.toString() + ':' + #endTime.toString()")
    public List<UserAnalysis> findByUserDeviceAndTimeRange(
            String utente, String tipoDiDispositivo, Instant startTime, Instant endTime) {
        logger.info("Ricerca combinata per utente, dispositivo e intervallo temporale: {} - {} - {} - {}", utente, tipoDiDispositivo, startTime, endTime);
        Span span = tracer.spanBuilder("findByUserDeviceAndTimeRange").startSpan();
        try {
            meterRegistry.counter("service.useranalysis.query.count").increment();
            return userAnalysisRepository.findByUserDeviceAndTimeRange(utente, tipoDiDispositivo, startTime, endTime);
        } finally {
            span.end();
        }
    }

    public List<UserAnalysis> generateRandomUserAnalysisList(int count) {
        logger.info("Generazione lista random di UserAnalysis. Count: {}", count);
        Span span = tracer.spanBuilder("generateRandomUserAnalysisList").startSpan();
        try {
            meterRegistry.counter("service.useranalysis.random.generate.count").increment(count);
            List<UserAnalysis> list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                list.add(userAnalysisFaker.generateRandomUserAnalysis());
            }
            return list;
        } finally {
            span.end();
        }
    }
}
