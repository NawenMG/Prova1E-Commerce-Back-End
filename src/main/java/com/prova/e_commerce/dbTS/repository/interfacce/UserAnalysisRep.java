package com.prova.e_commerce.dbTS.repository.interfacce;

import com.prova.e_commerce.dbTS.model.UserAnalysis;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface UserAnalysisRep {

    // Metodo per inserire una singola analisi
    void insert(UserAnalysis analysis);

    // Metodo per inserire un batch di analisi
    void insertBatch(List<UserAnalysis> analysisList);

    // Metodo per aggiornare una vecchia analisi con una nuova
    void update(UserAnalysis oldAnalysis, UserAnalysis newAnalysis);

    // Metodo per eliminare dati basati sul tag "utente"
    void deleteByTag(String utente);

    // Metodo per eseguire una query basata su un intervallo temporale
    List<UserAnalysis> findByTimeRange(Instant startTime, Instant endTime);

    // Metodo per trovare analisi per utente specifico
    List<UserAnalysis> findByUser(String utente);

    // Metodo per trovare analisi per tipo di dispositivo
    List<UserAnalysis> findByDeviceType(String tipoDiDispositivo);

    // Metodo per trovare analisi per tipo di azione
    List<UserAnalysis> findByAction(String azione);

    // Metodo per ottenere la durata media delle azioni per ogni utente
    Map<String, Double> getAverageDurationByUser();

    // Metodo per eseguire una query combinata per utente, dispositivo e intervallo temporale
    List<UserAnalysis> findByUserDeviceAndTimeRange(
            String utente,
            String tipoDiDispositivo,
            Instant startTime,
            Instant endTime);
}
