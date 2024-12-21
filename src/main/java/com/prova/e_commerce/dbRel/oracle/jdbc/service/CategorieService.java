package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Categorie;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.CategorieRep;
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
public class CategorieService {

    private static final Logger logger = LoggerFactory.getLogger(CategorieService.class);

    @Autowired
    private CategorieRep categorieRep;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    private static final String TOPIC_EVENTI_CATEGORIA_AGGIUNGI = "eventi-categoria-topic-aggiungi";
    private static final String TOPIC_EVENTI_CATEGORIA_AGGIORNA = "eventi-categoria-topic-aggiorna";

    public CategorieService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.categorie.query.count");
        meterRegistry.counter("service.categorie.inserisci.count");
        meterRegistry.counter("service.categorie.aggiorna.count");
        meterRegistry.counter("service.categorie.elimina.count");
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #categorie.toString()")
    public List<Categorie> queryCategorie(ParamQuery paramQuery, Categorie categorie) {
        logger.info("Esecuzione query avanzata sulle categorie: paramQuery={}, categorie={}", paramQuery, categorie);
        Span span = tracer.spanBuilder("queryCategorie").startSpan();
        try {
            meterRegistry.counter("service.categorie.query.count").increment();
            return categorieRep.query(paramQuery, categorie);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String inserisciCategoria(Categorie categorie) {
        logger.info("Inserimento nuova categoria: categorie={}", categorie);
        Span span = tracer.spanBuilder("inserisciCategoria").startSpan();
        try {
            meterRegistry.counter("service.categorie.inserisci.count").increment();
            String result = categorieRep.insertCategory(categorie);
            kafkaTemplate.send(TOPIC_EVENTI_CATEGORIA_AGGIUNGI, "Inserimento", "Categoria inserita: " + categorie.getName());
            return result;
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String aggiornaCategoria(String categoryID, Categorie categorie) {
        logger.info("Aggiornamento categoria: categoryID={}, categorie={}", categoryID, categorie);
        Span span = tracer.spanBuilder("aggiornaCategoria").startSpan();
        try {
            meterRegistry.counter("service.categorie.aggiorna.count").increment();
            String result = categorieRep.updateCategory(categoryID, categorie);
            kafkaTemplate.send(TOPIC_EVENTI_CATEGORIA_AGGIORNA, "Aggiornamento", "Categoria aggiornata con ID: " + categoryID);
            return result;
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String eliminaCategoria(String categoryID) {
        logger.info("Eliminazione categoria: categoryID={}", categoryID);
        Span span = tracer.spanBuilder("eliminaCategoria").startSpan();
        try {
            meterRegistry.counter("service.categorie.elimina.count").increment();
            return categorieRep.deleteCategory(categoryID);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String salvaCategorieCasuali(int numero) {
        logger.info("Salvataggio categorie casuali: numero={}", numero);
        Span span = tracer.spanBuilder("salvaCategorieCasuali").startSpan();
        try {
            meterRegistry.counter("service.categorie.salva.casuali.count").increment();
            return categorieRep.saveAll(numero);
        } finally {
            span.end();
        }
    }
}
