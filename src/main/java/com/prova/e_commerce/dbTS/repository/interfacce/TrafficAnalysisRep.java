package com.prova.e_commerce.dbTS.repository.interfacce;

import com.prova.e_commerce.dbTS.model.TrafficAnalysis;
import java.time.Instant;
import java.util.List;

public interface TrafficAnalysisRep {

    // Inserisce una singola analisi
    void insert(TrafficAnalysis analysis);

    // Inserisce un batch di analisi
    void insertBatch(List<TrafficAnalysis> analysisList);

    // Elimina i record per una specifica URL della pagina
    void deleteByUrl(String urlPagina);

    // Trova analisi per un intervallo temporale
    List<TrafficAnalysis> findByTimeRange(Instant startTime, Instant endTime);

    // Trova analisi per URL specifica
    List<TrafficAnalysis> findByUrl(String urlPagina);

    // Ottieni le visite medie per URL
    List<TrafficAnalysis> getAverageVisitsByUrl();
}
