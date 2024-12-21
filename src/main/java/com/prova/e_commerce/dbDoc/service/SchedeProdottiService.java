package com.prova.e_commerce.dbDoc.service;

import com.prova.e_commerce.dbDoc.entity.SchedeProdotti;
import com.prova.e_commerce.dbDoc.parametri.ParamQueryDbDoc;
import com.prova.e_commerce.dbDoc.randomData.SchedeProdottiFaker;
import com.prova.e_commerce.dbDoc.repository.interfacce.SchedeProdottiRep;
import com.prova.e_commerce.dbDoc.repository.interfacce.SchedeProdottiRepCustom;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SchedeProdottiService {

    private static final Logger logger = LoggerFactory.getLogger(SchedeProdottiService.class);
    private static final String TOPIC_SCHEDA_PRODOTTI_SAVE = "scheda-prodotti-topic-save";
    private static final String TOPIC_SCHEDA_PRODOTTI_UPDATE = "scheda-prodotti-topic-update";

    @Autowired
    private SchedeProdottiRep schedeProdottiRep;

    @Autowired
    private SchedeProdottiFaker prodottiFaker;

    @Autowired
    private SchedeProdottiRepCustom schedeProdottiRepCustom;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    public SchedeProdottiService(MeterRegistry meterRegistry) {
        // Inizializza metriche personalizzate
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.schedeprodotti.save.count");
        meterRegistry.counter("service.schedeprodotti.update.count");
        meterRegistry.counter("service.schedeprodotti.query.count");
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#nome", unless = "#result == null")
    public List<SchedeProdotti> findByNome(String nome) {
        logger.info("Finding schede prodotti by nome: {}", nome);
        Span span = tracer.spanBuilder("findByNome").startSpan();
        try {
            return schedeProdottiRep.findByNome(nome);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#prezzo", unless = "#result == null")
    public List<SchedeProdotti> findByPrezzoLessThan(BigDecimal prezzo) {
        logger.info("Finding schede prodotti with prezzo less than: {}", prezzo);
        Span span = tracer.spanBuilder("findByPrezzoLessThan").startSpan();
        try {
            return schedeProdottiRep.findByPrezzoLessThan(prezzo);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQueryDbDoc.query", unless = "#result == null")
    public List<SchedeProdotti> queryDynamic(ParamQueryDbDoc paramQueryDbDoc, SchedeProdotti schedeProdotti) {
        logger.info("Executing dynamic query for schede prodotti");
        Span span = tracer.spanBuilder("queryDynamic").startSpan();
        try {
            meterRegistry.counter("service.schedeprodotti.query.count").increment();
            return schedeProdottiRepCustom.query(paramQueryDbDoc, schedeProdotti);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public SchedeProdotti insert(SchedeProdotti prodotto) {
        logger.info("Inserting new prodotto: {}", prodotto);
        Span span = tracer.spanBuilder("insert").startSpan();
        try {
            meterRegistry.counter("service.schedeprodotti.save.count").increment();
            SchedeProdotti savedProdotto = schedeProdottiRep.save(prodotto);
            kafkaTemplate.send(TOPIC_SCHEDA_PRODOTTI_SAVE, "ProdottoCreato", savedProdotto);
            return savedProdotto;
        } finally {
            span.end();
        }
    }

    public List<SchedeProdotti> generateRandomSchedeProdotti(int count) {
        logger.info("Generating {} random schede prodotti", count);
        Span span = tracer.spanBuilder("generateRandomSchedeProdotti").startSpan();
        try {
            List<SchedeProdotti> schedeProdottiList = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                schedeProdottiList.add(prodottiFaker.generateFakeProduct());
            }
            return schedeProdottiList;
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    @Transactional
    public SchedeProdotti update(String id, SchedeProdotti prodotto) {
        logger.info("Updating prodotto with ID: {}", id);
        Span span = tracer.spanBuilder("update").startSpan();
        try {
            meterRegistry.counter("service.schedeprodotti.update.count").increment();
            Optional<SchedeProdotti> existingProdotto = schedeProdottiRep.findById(id);
            if (existingProdotto.isPresent()) {
                SchedeProdotti updatedProdotto = existingProdotto.get();
                updatedProdotto.setNome(prodotto.getNome());
                updatedProdotto.setPrezzo(prodotto.getPrezzo());
                updatedProdotto.setParametriDescrittivi(prodotto.getParametriDescrittivi());
                SchedeProdotti savedProdotto = schedeProdottiRep.save(updatedProdotto);
                kafkaTemplate.send(TOPIC_SCHEDA_PRODOTTI_UPDATE, "ProdottoAggiornato", savedProdotto);
                return savedProdotto;
            } else {
                logger.warn("Prodotto with ID {} not found", id);
                throw new RuntimeException("Prodotto con ID " + id + " non trovato.");
            }
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void deleteById(String id) {
        logger.info("Deleting prodotto with ID: {}", id);
        Span span = tracer.spanBuilder("deleteById").startSpan();
        try {
            if (schedeProdottiRep.existsById(id)) {
                schedeProdottiRep.deleteById(id);
            } else {
                logger.warn("Prodotto with ID {} not found", id);
                throw new RuntimeException("Prodotto con ID " + id + " non trovato.");
            }
        } finally {
            span.end();
        }
    }
}
