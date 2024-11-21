package com.prova.e_commerce.dbTS.controller.graphql;

import com.prova.e_commerce.dbTS.model.TrafficAnalysis;
import com.prova.e_commerce.dbTS.service.TrafficAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class TrafficAnalysisResolverGraphql {

    @Autowired
    private TrafficAnalysisService trafficAnalysisService;

    // Query per trovare le analisi in un intervallo temporale
    @QueryMapping
    public List<TrafficAnalysis> findByTimeRange(Instant startTime, Instant endTime) {
        return trafficAnalysisService.findByTimeRange(startTime, endTime);
    }

    // Query per trovare le analisi per una URL specifica
    @QueryMapping
    public List<TrafficAnalysis> findByUrl(String urlPagina) {
        return trafficAnalysisService.findByUrl(urlPagina);
    }

    // Query per ottenere le visite medie per URL
    @QueryMapping
    public List<TrafficAnalysis> getAverageVisitsByUrl() {
        return trafficAnalysisService.getAverageVisitsByUrl();
    }
}
