package com.prova.e_commerce.dbCol.service;

import com.prova.e_commerce.dbCol.model.ArchiviazioneResi;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneResiRep;
import com.prova.e_commerce.security.security1.SecurityUtils;
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
public class ArchiviazioneResiService {

    private static final Logger logger = LoggerFactory.getLogger(ArchiviazioneResiService.class);
    private static final String TOPIC_RESI_SAVE = "resi-topic-save";
    private static final String TOPIC_RESI_UPDATE = "resi-topic-update";

    @Autowired
    private ArchiviazioneResiRep archiviazioneResiRep;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    public ArchiviazioneResiService(MeterRegistry meterRegistry) {
        // Inizializza metriche personalizzate
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.resi.save.count");
        meterRegistry.counter("service.resi.update.count");
        meterRegistry.counter("service.resi.query.count");
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #reso.toString()")
    public List<ArchiviazioneResi> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneResi reso) {
        logger.info("Executing dynamic query for resi");
        Span span = tracer.spanBuilder("queryDinamica").startSpan();
        try {
            meterRegistry.counter("service.resi.query.count").increment();
            return archiviazioneResiRep.queryDinamica(paramQuery, reso);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#id")
    public ArchiviazioneResi getResoByID(String id) {
        logger.info("Finding reso by ID: {}", id);
        Span span = tracer.spanBuilder("getResoByID").startSpan();
        try {
            return archiviazioneResiRep.findResoByID(id);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void saveReso(ArchiviazioneResi reso) {
        logger.info("Saving new reso: {}", reso);
        Span span = tracer.spanBuilder("saveReso").startSpan();
        try {
            meterRegistry.counter("service.resi.save.count").increment();
            archiviazioneResiRep.saveReso(reso, SecurityUtils.getCurrentUsername());
            kafkaTemplate.send(TOPIC_RESI_SAVE, "ResoCreato", reso);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void updateReso(String id, ArchiviazioneResi reso) {
        logger.info("Updating reso with ID: {}", id);
        Span span = tracer.spanBuilder("updateReso").startSpan();
        try {
            meterRegistry.counter("service.resi.update.count").increment();
            archiviazioneResiRep.updateReso(id, reso);
            kafkaTemplate.send(TOPIC_RESI_UPDATE, "ResoAggiornato", reso);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void deleteReso(String id) {
        logger.info("Deleting reso with ID: {}", id);
        Span span = tracer.spanBuilder("deleteReso").startSpan();
        try {
            archiviazioneResiRep.deleteReso(id);
        } finally {
            span.end();
        }
    }
}
