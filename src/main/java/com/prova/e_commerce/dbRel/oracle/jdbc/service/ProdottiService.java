package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Prodotti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.ProdottiRep;
import com.prova.e_commerce.storage.OzoneService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ProdottiService {

    private static final Logger logger = LoggerFactory.getLogger(ProdottiService.class);

    @Autowired
    private ProdottiRep prodottiRep;

    @Autowired
    private OzoneService ozoneService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    private static final String KAFKA_TOPIC_PRODOTTI_AGGIUNGI = "prodotti-topic-aggiungi";
    private static final String KAFKA_TOPIC_PRODOTTI_AGGIORNA = "prodotti-topic-aggiorna";

    public ProdottiService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.prodotti.query.count");
        meterRegistry.counter("service.prodotti.inserisci.count");
        meterRegistry.counter("service.prodotti.aggiorna.count");
        meterRegistry.counter("service.prodotti.elimina.count");
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #prodotti.toString()")
    public List<Prodotti> queryProdotti(ParamQuery paramQuery, Prodotti prodotti) {
        logger.info("Esecuzione query avanzata sui prodotti: paramQuery={}, prodotti={}", paramQuery, prodotti);
        Span span = tracer.spanBuilder("queryProdotti").startSpan();
        try {
            meterRegistry.counter("service.prodotti.query.count").increment();
            return prodottiRep.query(paramQuery, prodotti);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String inserisciProdotto(Prodotti prodotto) {
        logger.info("Inserimento nuovo prodotto: prodotto={}", prodotto);
        Span span = tracer.spanBuilder("inserisciProdotto").startSpan();
        try {
            meterRegistry.counter("service.prodotti.inserisci.count").increment();
            kafkaTemplate.send(KAFKA_TOPIC_PRODOTTI_AGGIUNGI, "Nuovo prodotto inserito: " + prodotto.getProductId());
            return prodottiRep.insertProduct(prodotto);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String aggiornaProdotto(String productID, Prodotti prodotto) {
        logger.info("Aggiornamento prodotto: productID={}, prodotto={}", productID, prodotto);
        Span span = tracer.spanBuilder("aggiornaProdotto").startSpan();
        try {
            meterRegistry.counter("service.prodotti.aggiorna.count").increment();
            kafkaTemplate.send(KAFKA_TOPIC_PRODOTTI_AGGIORNA, "Prodotto aggiornato: " + productID);
            return prodottiRep.updateProduct(productID, prodotto);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String eliminaProdotto(String productID) {
        logger.info("Eliminazione prodotto: productID={}", productID);
        Span span = tracer.spanBuilder("eliminaProdotto").startSpan();
        try {
            meterRegistry.counter("service.prodotti.elimina.count").increment();
            return prodottiRep.deleteProduct(productID);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String salvaProdottiCasuali(int numero) {
        logger.info("Salvataggio prodotti casuali: numero={}", numero);
        Span span = tracer.spanBuilder("salvaProdottiCasuali").startSpan();
        try {
            meterRegistry.counter("service.prodotti.salva.casuali.count").increment();
            return prodottiRep.saveAll(numero);
        } finally {
            span.end();
        }
    }
}
