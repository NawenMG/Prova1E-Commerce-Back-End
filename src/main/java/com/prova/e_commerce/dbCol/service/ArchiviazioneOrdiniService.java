package com.prova.e_commerce.dbCol.service;

import com.prova.e_commerce.dbCol.model.ArchiviazioneOrdini;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneOrdiniRep;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

@Service
public class ArchiviazioneOrdiniService {

    private static final Logger logger = LoggerFactory.getLogger(ArchiviazioneOrdiniService.class);
    private static final String TOPIC_ORDINI_SAVE = "ordini-topic-save";
    private static final String TOPIC_ORDINI_UPDATE = "ordini-topic-update";

    @Autowired
    private ArchiviazioneOrdiniRep archiviazioneOrdiniRep;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    public ArchiviazioneOrdiniService(MeterRegistry meterRegistry) {
        // Metriche personalizzate
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.ordine.save.count");
        meterRegistry.counter("service.ordine.update.count");
        meterRegistry.counter("service.ordine.query.count");
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #ordine.toString()")
    public List<ArchiviazioneOrdini> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneOrdini ordine) {
        logger.info("Executing dynamic query for orders");
        Span span = tracer.spanBuilder("queryDinamica").startSpan();
        try {
            meterRegistry.counter("service.ordine.query.count").increment();
            return archiviazioneOrdiniRep.queryDinamica(paramQuery, ordine);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#id")
    public ArchiviazioneOrdini findOrdineById(String id) {
        logger.info("Finding order by ID: {}", id);
        Span span = tracer.spanBuilder("findOrdineById").startSpan();
        try {
            return archiviazioneOrdiniRep.findOrdineById(id);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "'findAll'")
    public List<ArchiviazioneOrdini> findAllOrdini() {
        logger.info("Fetching all orders");
        Span span = tracer.spanBuilder("findAllOrdini").startSpan();
        try {
            return archiviazioneOrdiniRep.findAllOrdini();
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void saveOrdine(ArchiviazioneOrdini ordine) {
        logger.info("Saving new order: {}", ordine);
        Span span = tracer.spanBuilder("saveOrdine").startSpan();
        try {
            meterRegistry.counter("service.ordine.save.count").increment();
            archiviazioneOrdiniRep.saveOrdine(ordine);
            kafkaTemplate.send(TOPIC_ORDINI_SAVE, "OrdineCreato", ordine);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void updateOrdine(String id, ArchiviazioneOrdini ordine) {
        logger.info("Updating order with ID: {}", id);
        Span span = tracer.spanBuilder("updateOrdine").startSpan();
        try {
            meterRegistry.counter("service.ordine.update.count").increment();
            archiviazioneOrdiniRep.updateOrdine(id, ordine);
            kafkaTemplate.send(TOPIC_ORDINI_UPDATE, "OrdineAggiornato", ordine);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void deleteOrdine(String id) {
        logger.info("Deleting order with ID: {}", id);
        Span span = tracer.spanBuilder("deleteOrdine").startSpan();
        try {
            archiviazioneOrdiniRep.deleteOrdine(id);
        } finally {
            span.end();
        }
    }
}
