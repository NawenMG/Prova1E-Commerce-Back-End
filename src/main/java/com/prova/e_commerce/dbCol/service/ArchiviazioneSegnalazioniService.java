package com.prova.e_commerce.dbCol.service;

import com.prova.e_commerce.dbCol.model.ArchiviazioneSegnalazioni;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneSegnalazioniRep;
import com.prova.e_commerce.security.security1.SecurityUtils;
import com.prova.e_commerce.storage.OzoneService;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchiviazioneSegnalazioniService {

    private static final Logger logger = LoggerFactory.getLogger(ArchiviazioneSegnalazioniService.class);
    private static final String TOPIC_SEGNALAZIONI_SAVE = "segnalazioni-topic-save";
    //private static final String TOPIC_SEGNALAZIONI_UPDATE = "segnalazioni-topic-update";

    @Autowired
    private ArchiviazioneSegnalazioniRep archiviazioneSegnalazioniRep;

    @Autowired
    private OzoneService ozoneService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    public ArchiviazioneSegnalazioniService(MeterRegistry meterRegistry) {
        // Inizializza metriche personalizzate
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.segnalazione.save.count");
        meterRegistry.counter("service.segnalazione.update.count");
        meterRegistry.counter("service.segnalazione.query.count");
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #segnalazione.toString()")
    public List<ArchiviazioneSegnalazioni> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneSegnalazioni segnalazione) {
        logger.info("Executing dynamic query for segnalazioni");
        Span span = tracer.spanBuilder("queryDinamica").startSpan();
        try {
            meterRegistry.counter("service.segnalazione.query.count").increment();
            return archiviazioneSegnalazioniRep.queryDinamica(paramQuery, segnalazione);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#id")
    public ArchiviazioneSegnalazioni findSegnalazioneById(String id) {
        logger.info("Finding segnalazione by ID: {}", id);
        Span span = tracer.spanBuilder("findSegnalazioneById").startSpan();
        try {
            return archiviazioneSegnalazioniRep.findSegnalazioneById(id);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "'findAllSegnalazioni'")
    public List<ArchiviazioneSegnalazioni> findAllSegnalazioni() {
        logger.info("Fetching all segnalazioni");
        Span span = tracer.spanBuilder("findAllSegnalazioni").startSpan();
        try {
            return archiviazioneSegnalazioniRep.findAllSegnalazioni();
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void saveSegnalazione(ArchiviazioneSegnalazioni segnalazione) {
        logger.info("Saving new segnalazione: {}", segnalazione);
        Span span = tracer.spanBuilder("saveSegnalazione").startSpan();
        try {
            meterRegistry.counter("service.segnalazione.save.count").increment();
            archiviazioneSegnalazioniRep.saveSegnalazione(segnalazione, SecurityUtils.getCurrentUsername());
            kafkaTemplate.send(TOPIC_SEGNALAZIONI_SAVE, "SegnalazioneCreata", segnalazione);
        } finally {
            span.end();
        }
    }

    /* @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void updateSegnalazione(String id, ArchiviazioneSegnalazioni segnalazione) {
        logger.info("Updating segnalazione with ID: {}", id);
        Span span = tracer.spanBuilder("updateSegnalazione").startSpan();
        try {
            meterRegistry.counter("service.segnalazione.update.count").increment();
            archiviazioneSegnalazioniRep.updateSegnalazione(id, segnalazione);
            kafkaTemplate.send(TOPIC_SEGNALAZIONI_UPDATE, "SegnalazioneAggiornata", segnalazione);
        } finally {
            span.end();
        }
    } */

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void deleteSegnalazione(String id) {
        logger.info("Deleting segnalazione with ID: {}", id);
        Span span = tracer.spanBuilder("deleteSegnalazione").startSpan();
        try {
            archiviazioneSegnalazioniRep.deleteSegnalazione(id);
        } finally {
            span.end();
        }
    }
}
