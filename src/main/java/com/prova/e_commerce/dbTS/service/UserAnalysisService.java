package com.prova.e_commerce.dbTS.service;

import com.prova.e_commerce.dbTS.model.UserAnalysis;
import com.prova.e_commerce.dbTS.randomData.UserAnalysisFaker;
import com.prova.e_commerce.dbTS.repository.interfacce.UserAnalysisRep;
import org.springframework.beans.factory.annotation.Autowired;
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
    
        // Metodo per inserire una singola analisi
        public void insert(UserAnalysis analysis) {
            userAnalysisRepository.insert(analysis);
        }
    
        // Metodo per inserire un batch di analisi
        public void insertBatch(List<UserAnalysis> analysisList) {
            userAnalysisRepository.insertBatch(analysisList);
        }
    
        // Metodo per aggiornare una vecchia analisi con una nuova
        public void update(UserAnalysis oldAnalysis, UserAnalysis newAnalysis) {
            userAnalysisRepository.update(oldAnalysis, newAnalysis);
        }
    
        // Metodo per eliminare analisi basate sul tag "utente"
        public void deleteByTag(String utente) {
            userAnalysisRepository.deleteByTag(utente);
        }
    
        // Metodo per ottenere le analisi in un intervallo temporale
        public List<UserAnalysis> findByTimeRange(Instant startTime, Instant endTime) {
            return userAnalysisRepository.findByTimeRange(startTime, endTime);
        }
    
        // Metodo per trovare analisi per un utente specifico
        public List<UserAnalysis> findByUser(String utente) {
            return userAnalysisRepository.findByUser(utente);
        }
    
        // Metodo per trovare analisi per tipo di dispositivo
        public List<UserAnalysis> findByDeviceType(String tipoDiDispositivo) {
            return userAnalysisRepository.findByDeviceType(tipoDiDispositivo);
        }
    
        // Metodo per trovare analisi per tipo di azione
        public List<UserAnalysis> findByAction(String azione) {
            return userAnalysisRepository.findByAction(azione);
        }
    
        // Metodo per ottenere la durata media delle azioni per utente
        public Map<String, Double> getAverageDurationByUser() {
            return userAnalysisRepository.getAverageDurationByUser();
        }
    
        // Metodo per eseguire una query combinata per utente, dispositivo e intervallo temporale
        public List<UserAnalysis> findByUserDeviceAndTimeRange(
                String utente,
                String tipoDiDispositivo,
                Instant startTime,
                Instant endTime) {
            return userAnalysisRepository.findByUserDeviceAndTimeRange(
                    utente, tipoDiDispositivo, startTime, endTime);
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
