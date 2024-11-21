package com.prova.e_commerce.dbTS.controller.graphql;

import com.prova.e_commerce.dbTS.model.UserAnalysis;
import com.prova.e_commerce.dbTS.service.UserAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class UserAnalysisResolverGraphql {

    @Autowired
    private UserAnalysisService userAnalysisService;

    @QueryMapping
    public List<UserAnalysis> findByTimeRange(Instant startTime, Instant endTime) {
        return userAnalysisService.findByTimeRange(startTime, endTime);
    }

    @QueryMapping
    public List<UserAnalysis> findByUser(String utente) {
        return userAnalysisService.findByUser(utente);
    }

    @QueryMapping
    public List<UserAnalysis> findByDeviceType(String tipoDiDispositivo) {
        return userAnalysisService.findByDeviceType(tipoDiDispositivo);
    }

    @QueryMapping
    public List<UserAnalysis> findByAction(String azione) {
        return userAnalysisService.findByAction(azione);
    }

    @QueryMapping
    public Map<String, Double> getAverageDurationByUser() {
        return userAnalysisService.getAverageDurationByUser();
    }

    @QueryMapping
    public List<UserAnalysis> findByUserDeviceAndTimeRange(
            String utente,
            String tipoDiDispositivo,
            Instant startTime,
            Instant endTime) {
        return userAnalysisService.findByUserDeviceAndTimeRange(utente, tipoDiDispositivo, startTime, endTime);
    }
}
