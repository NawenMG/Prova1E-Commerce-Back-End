package com.prova.e_commerce.dbCol.service;

import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneTransizioniRep;
import com.prova.e_commerce.security.security1.SecurityUtils;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.model.ArchiviazioneTransizioni;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchiviazioneTransizioniService {

    private static final Logger logger = LoggerFactory.getLogger(ArchiviazioneTransizioniService.class);
    private static final String TOPIC_TRANSIZIONI_SAVE = "transizioni-topic-save";
    private static final String TOPIC_TRANSIZIONI_UPDATE = "transizioni-topic-update";

    @Autowired
    private ArchiviazioneTransizioniRep archiviazioneTransizioniRep;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    public ArchiviazioneTransizioniService(MeterRegistry meterRegistry) {
        // Inizializza metriche personalizzate
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.transizione.save.count");
        meterRegistry.counter("service.transizione.update.count");
        meterRegistry.counter("service.transizione.query.count");
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #transizione.toString()")
    public List<ArchiviazioneTransizioni> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneTransizioni transizione) {
        logger.info("Executing dynamic query for transizioni");
        Span span = tracer.spanBuilder("queryDinamica").startSpan();
        try {
            meterRegistry.counter("service.transizione.query.count").increment();
            return archiviazioneTransizioniRep.queryDinamica(paramQuery, transizione);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#id")
    public ArchiviazioneTransizioni findTransizioneById(String id) {
        logger.info("Finding transizione by ID: {}", id);
        Span span = tracer.spanBuilder("findTransizioneById").startSpan();
        try {
            return archiviazioneTransizioniRep.findTransizioneById(id);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "'findAllTransizioni'")
    public List<ArchiviazioneTransizioni> findAllTransizioni() {
        logger.info("Fetching all transizioni");
        Span span = tracer.spanBuilder("findAllTransizioni").startSpan();
        try {
            return archiviazioneTransizioniRep.findAllTransizioni();
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void saveTransizione(ArchiviazioneTransizioni transizione) {
        logger.info("Saving new transizione: {}", transizione);
        Span span = tracer.spanBuilder("saveTransizione").startSpan();
        try {
            meterRegistry.counter("service.transizione.save.count").increment();
            archiviazioneTransizioniRep.saveTransizione(transizione,SecurityUtils.getCurrentUsername());
            kafkaTemplate.send(TOPIC_TRANSIZIONI_SAVE, "TransizioneCreata", transizione);
        } finally {
            span.end();
        }
    }

    /* @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void updateTransizione(String id, ArchiviazioneTransizioni transizione) {
        logger.info("Updating transizione with ID: {}", id);
        Span span = tracer.spanBuilder("updateTransizione").startSpan();
        try {
            meterRegistry.counter("service.transizione.update.count").increment();
            archiviazioneTransizioniRep.updateTransizione(id, transizione);
            kafkaTemplate.send(TOPIC_TRANSIZIONI_UPDATE, "TransizioneAggiornata", transizione);
        } finally {
            span.end();
        }
    } */

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void deleteTransizione(String id) {
        logger.info("Deleting transizione with ID: {}", id);
        Span span = tracer.spanBuilder("deleteTransizione").startSpan();
        try {
            archiviazioneTransizioniRep.deleteTransizione(id);
        } finally {
            span.end();
        }
    }
}
