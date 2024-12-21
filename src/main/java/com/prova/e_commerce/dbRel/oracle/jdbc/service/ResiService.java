package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Resi;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.ResiRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ResiService {

    private static final Logger logger = LoggerFactory.getLogger(ResiService.class);

    @Autowired
    private ResiRep resiRep;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    private static final String KAFKA_TOPIC_RESI_AGGIUNGI = "resi-topic-aggiungi";
    private static final String KAFKA_TOPIC_RESI_AGGIORNA = "resi-topic-aggiorna";

    public ResiService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.resi.query.count");
        meterRegistry.counter("service.resi.inserisci.count");
        meterRegistry.counter("service.resi.aggiorna.count");
        meterRegistry.counter("service.resi.elimina.count");
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #resi.toString()")
    public List<Resi> queryResi(ParamQuery paramQuery, Resi resi) {
        logger.info("Esecuzione query avanzata sui resi: paramQuery={}, resi={}", paramQuery, resi);
        Span span = tracer.spanBuilder("queryResi").startSpan();
        try {
            meterRegistry.counter("service.resi.query.count").increment();
            return resiRep.query(paramQuery, resi);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String inserisciReso(Resi resi) {
        logger.info("Inserimento nuovo reso: resi={}", resi);
        Span span = tracer.spanBuilder("inserisciReso").startSpan();
        try {
            meterRegistry.counter("service.resi.inserisci.count").increment();
            kafkaTemplate.send(KAFKA_TOPIC_RESI_AGGIUNGI, "Nuovo reso inserito: " + resi.getReturnsID());
            return resiRep.insertReturn(resi);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String aggiornaReso(String returnID, Resi resi) {
        logger.info("Aggiornamento reso: returnID={}, resi={}", returnID, resi);
        Span span = tracer.spanBuilder("aggiornaReso").startSpan();
        try {
            meterRegistry.counter("service.resi.aggiorna.count").increment();
            kafkaTemplate.send(KAFKA_TOPIC_RESI_AGGIORNA, "Reso aggiornato: " + returnID);
            return resiRep.updateReturn(returnID, resi);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String eliminaReso(String returnID) {
        logger.info("Eliminazione reso: returnID={}", returnID);
        Span span = tracer.spanBuilder("eliminaReso").startSpan();
        try {
            meterRegistry.counter("service.resi.elimina.count").increment();
            return resiRep.deleteReturn(returnID);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String salvaResiCasuali(int numero) {
        logger.info("Salvataggio resi casuali: numero={}", numero);
        Span span = tracer.spanBuilder("salvaResiCasuali").startSpan();
        try {
            meterRegistry.counter("service.resi.salva.casuali.count").increment();
            return resiRep.saveAll(numero);
        } finally {
            span.end();
        }
    }
}
