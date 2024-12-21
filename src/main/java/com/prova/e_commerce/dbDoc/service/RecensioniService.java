package com.prova.e_commerce.dbDoc.service;

import com.prova.e_commerce.dbDoc.entity.Recensioni;
import com.prova.e_commerce.dbDoc.parametri.ParamQueryDbDoc;
import com.prova.e_commerce.dbDoc.randomData.RecensioniFaker;
import com.prova.e_commerce.dbDoc.repository.interfacce.RecensioniRep;
import com.prova.e_commerce.dbDoc.repository.interfacce.RecensioniRepCustom;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class RecensioniService {

    private static final Logger logger = LoggerFactory.getLogger(RecensioniService.class);
    private static final String TOPIC_RECENSIONI_SAVE = "recensioni-topic-save";
    private static final String TOPIC_RECENSIONI_UPDATE = "recensioni-topic-update";

    @Autowired
    private RecensioniRep recensioniRep;

    @Autowired
    private RecensioniRepCustom recensioniRepCustom;

    @Autowired
    private RecensioniFaker recensioniFaker;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    public RecensioniService(MeterRegistry meterRegistry) {
        // Inizializza metriche personalizzate
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.recensioni.save.count");
        meterRegistry.counter("service.recensioni.update.count");
        meterRegistry.counter("service.recensioni.query.count");
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQueryDbDoc.toString() + #recensioni.toString()")
    public List<Recensioni> queryDynamic(ParamQueryDbDoc paramQueryDbDoc, Recensioni recensioni) {
        logger.info("Executing dynamic query for recensioni");
        Span span = tracer.spanBuilder("queryDynamic").startSpan();
        try {
            meterRegistry.counter("service.recensioni.query.count").increment();
            return recensioniRepCustom.query(paramQueryDbDoc, recensioni);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#productId")
    public List<Recensioni> findByProductId(String productId) {
        logger.info("Finding recensioni by productId: {}", productId);
        Span span = tracer.spanBuilder("findByProductId").startSpan();
        try {
            return recensioniRep.findByProductId(productId);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#userId")
    public List<Recensioni> findByUserId(String userId) {
        logger.info("Finding recensioni by userId: {}", userId);
        Span span = tracer.spanBuilder("findByUserId").startSpan();
        try {
            return recensioniRep.findByUserId(userId);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "'findAll'")
    public List<Recensioni> findAllRecensioni() {
        logger.info("Fetching all recensioni");
        Span span = tracer.spanBuilder("findAllRecensioni").startSpan();
        try {
            return recensioniRep.findAll();
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public Recensioni saveRecensione(Recensioni recensione) {
        logger.info("Saving new recensione: {}", recensione);
        Span span = tracer.spanBuilder("saveRecensione").startSpan();
        try {
            meterRegistry.counter("service.recensioni.save.count").increment();
            Recensioni savedRecensione = recensioniRep.save(recensione);
            kafkaTemplate.send(TOPIC_RECENSIONI_SAVE, "RecensioneCreata", savedRecensione);
            return savedRecensione;
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public Recensioni updateRecensione(String id, Recensioni recensione) {
        logger.info("Updating recensione with ID: {}", id);
        Span span = tracer.spanBuilder("updateRecensione").startSpan();
        try {
            meterRegistry.counter("service.recensioni.update.count").increment();
            if (recensioniRep.existsById(id)) {
                recensione.setId(id);
                Recensioni updatedRecensione = recensioniRep.save(recensione);
                kafkaTemplate.send(TOPIC_RECENSIONI_UPDATE, "RecensioneAggiornata", updatedRecensione);
                return updatedRecensione;
            } else {
                logger.warn("Recensione with ID {} not found", id);
                return null;
            }
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public boolean deleteRecensione(String id) {
        logger.info("Deleting recensione with ID: {}", id);
        Span span = tracer.spanBuilder("deleteRecensione").startSpan();
        try {
            if (recensioniRep.existsById(id)) {
                recensioniRep.deleteById(id);
                return true;
            }
            logger.warn("Recensione with ID {} not found", id);
            return false;
        } finally {
            span.end();
        }
    }

    public List<Recensioni> generateRandomRecensioni(int count) {
        logger.info("Generating {} random recensioni", count);
        Span span = tracer.spanBuilder("generateRandomRecensioni").startSpan();
        try {
            List<Recensioni> recensioniList = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                recensioniList.add(recensioniFaker.generateFakeReview());
            }
            return recensioniList;
        } finally {
            span.end();
        }
    }
}
