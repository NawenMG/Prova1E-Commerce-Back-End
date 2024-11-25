package com.prova.e_commerce.dbTS.service;

import com.prova.e_commerce.dbTS.model.UserAnalysis;
import com.prova.e_commerce.dbTS.randomData.UserAnalysisFaker;
import com.prova.e_commerce.dbTS.repository.interfacce.UserAnalysisRep;
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
public class UserAnalysisService {

    @Autowired
    private UserAnalysisRep userAnalysisRepository;

    @Autowired
    private UserAnalysisFaker userAnalysisFaker;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Kafka producer

    private static final String KAFKA_TOPIC_USER_ANALYSIS_AGGIUNGI = "user-analysis-topic-aggiungi"; 
    private static final String KAFKA_TOPIC_USER_ANALYSIS_AGGIORNA = "user-analysis-topic-aggiorna"; 


    // Inserisce una singola analisi e invalida la cache
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void insert(UserAnalysis analysis) {
        userAnalysisRepository.insert(analysis);

        // Invia un messaggio Kafka per notificare l'inserimento dell'analisi utente
        kafkaTemplate.send(KAFKA_TOPIC_USER_ANALYSIS_AGGIUNGI, "Nuova analisi utente inserita per l'utente: " + analysis.getUtente());
    }

    // Inserisce un batch di analisi e invalida la cache
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void insertBatch(List<UserAnalysis> analysisList) {
        userAnalysisRepository.insertBatch(analysisList);

        // Invia un messaggio Kafka per notificare l'inserimento del batch di analisi
        kafkaTemplate.send(KAFKA_TOPIC_USER_ANALYSIS_AGGIUNGI, "Batch di analisi utente inserito. Numero di voci: " );
    }

    // Aggiorna una vecchia analisi con una nuova e invalida la cache
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void update(UserAnalysis oldAnalysis, UserAnalysis newAnalysis) {
        userAnalysisRepository.update(oldAnalysis, newAnalysis);

        // Invia un messaggio Kafka per notificare l'aggiornamento dell'analisi utente
        kafkaTemplate.send(KAFKA_TOPIC_USER_ANALYSIS_AGGIORNA, "Analisi utente aggiornata per l'utente: " + oldAnalysis.getUtente());
    }

    // Elimina analisi basate sul tag "utente" e invalida la cache
    @CacheEvict(value = {"caffeine", "redis"}, key = "#utente")
    public void deleteByTag(String utente) {
        userAnalysisRepository.deleteByTag(utente);
    }

    // Trova le analisi in un intervallo temporale (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "'timeRange:' + #startTime.toString() + ':' + #endTime.toString()")
    public List<UserAnalysis> findByTimeRange(Instant startTime, Instant endTime) {
        return userAnalysisRepository.findByTimeRange(startTime, endTime);
    }

    // Trova le analisi per utente specifico (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "#utente")
    public List<UserAnalysis> findByUser(String utente) {
        return userAnalysisRepository.findByUser(utente);
    }

    // Trova le analisi per tipo di dispositivo (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "#tipoDiDispositivo")
    public List<UserAnalysis> findByDeviceType(String tipoDiDispositivo) {
        return userAnalysisRepository.findByDeviceType(tipoDiDispositivo);
    }

    // Trova le analisi per tipo di azione (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "#azione")
    public List<UserAnalysis> findByAction(String azione) {
        return userAnalysisRepository.findByAction(azione);
    }

    // Ottieni la durata media delle azioni per utente (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "'averageDurationByUser'")
    public Map<String, Double> getAverageDurationByUser() {
        return userAnalysisRepository.getAverageDurationByUser();
    }

    // Esegui una query combinata per utente, dispositivo e intervallo temporale (con caching)
    @Cacheable(value = {"caffeine", "redis"}, key = "'userDeviceTimeRange:' + #utente + ':' + #tipoDiDispositivo + ':' + #startTime.toString() + ':' + #endTime.toString()")
    public List<UserAnalysis> findByUserDeviceAndTimeRange(
            String utente, String tipoDiDispositivo, Instant startTime, Instant endTime) {
        return userAnalysisRepository.findByUserDeviceAndTimeRange(utente, tipoDiDispositivo, startTime, endTime);
    }

    // Metodo per generare una lista di UserAnalysis casuali
    public List<UserAnalysis> generateRandomUserAnalysisList(int count) {
        List<UserAnalysis> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(userAnalysisFaker.generateRandomUserAnalysis());
        }
        return list;
    }
}
