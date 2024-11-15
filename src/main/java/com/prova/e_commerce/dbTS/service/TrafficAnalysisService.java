package com.prova.e_commerce.dbTS.service;

import com.prova.e_commerce.dbTS.model.TrafficAnalysis;
import com.prova.e_commerce.dbTS.randomData.TrafficAnalysisFaker;
import com.prova.e_commerce.dbTS.repository.interfacce.TrafficAnalysisRep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrafficAnalysisService {

    @Autowired
    private TrafficAnalysisRep trafficAnalysisRep;

    @Autowired
    private TrafficAnalysisFaker trafficAnalysisFaker;
    
        // Inserisce una singola analisi del traffico
        public void insert(TrafficAnalysis analysis) {
            trafficAnalysisRep.insert(analysis);
        }
    
        // Inserisce un batch di analisi del traffico
        public void insertBatch(List<TrafficAnalysis> analysisList) {
            trafficAnalysisRep.insertBatch(analysisList);
        }
    
        // Elimina le analisi per una specifica URL
        public void deleteByUrl(String urlPagina) {
            trafficAnalysisRep.deleteByUrl(urlPagina);
        }
    
        // Trova le analisi in un intervallo temporale
        public List<TrafficAnalysis> findByTimeRange(Instant startTime, Instant endTime) {
            return trafficAnalysisRep.findByTimeRange(startTime, endTime);
        }
    
        // Trova le analisi per URL specifica
        public List<TrafficAnalysis> findByUrl(String urlPagina) {
            return trafficAnalysisRep.findByUrl(urlPagina);
        }
    
        // Ottieni le visite medie per URL
        public List<TrafficAnalysis> getAverageVisitsByUrl() {
            return trafficAnalysisRep.getAverageVisitsByUrl();
        }
    
        // Metodo per generare una lista di TrafficAnalysis casuali
        public List<TrafficAnalysis> generateRandomTrafficAnalysisList(int count) {
            List<TrafficAnalysis> list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                list.add(trafficAnalysisFaker.generateRandomTrafficAnalysis());
        }
        return list;
    }
}
