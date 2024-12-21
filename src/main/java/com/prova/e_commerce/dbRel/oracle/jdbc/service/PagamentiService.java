package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.PagamentiRep;
import com.prova.e_commerce.RabbitMQ.RabbitProducer;

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
public class PagamentiService {

    private static final Logger logger = LoggerFactory.getLogger(PagamentiService.class);

    @Autowired
    private PagamentiRep pagamentiRep;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RabbitProducer rabbitProducer;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    private static final String KAFKA_TOPIC_AGGIUNGI = "pagamenti-topic-aggiungi";
    private static final String KAFKA_TOPIC_AGGIORNA = "pagamenti-topic-aggiorna";

    public PagamentiService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.pagamenti.query.count");
        meterRegistry.counter("service.pagamenti.inserisci.count");
        meterRegistry.counter("service.pagamenti.aggiorna.count");
        meterRegistry.counter("service.pagamenti.elimina.count");
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #pagamenti.toString()")
    public List<Pagamenti> queryPagamenti(ParamQuery paramQuery, Pagamenti pagamenti) {
        logger.info("Esecuzione query avanzata sui pagamenti: paramQuery={}, pagamenti={}", paramQuery, pagamenti);
        Span span = tracer.spanBuilder("queryPagamenti").startSpan();
        try {
            meterRegistry.counter("service.pagamenti.query.count").increment();
            return pagamentiRep.query(paramQuery, pagamenti);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String inserisciPagamento(Pagamenti pagamenti) {
        logger.info("Inserimento nuovo pagamento: pagamenti={}", pagamenti);
        Span span = tracer.spanBuilder("inserisciPagamento").startSpan();
        try {
            meterRegistry.counter("service.pagamenti.inserisci.count").increment();
            String result = pagamentiRep.insertPayment(pagamenti);

            kafkaTemplate.send(KAFKA_TOPIC_AGGIUNGI, "Nuovo pagamento inserito: " + pagamenti.getPaymentsID());
            rabbitProducer.sendMessage(pagamenti);

            return result;
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String aggiornaPagamento(String paymentID, Pagamenti pagamenti) {
        logger.info("Aggiornamento pagamento: paymentID={}, pagamenti={}", paymentID, pagamenti);
        Span span = tracer.spanBuilder("aggiornaPagamento").startSpan();
        try {
            meterRegistry.counter("service.pagamenti.aggiorna.count").increment();
            String result = pagamentiRep.updatePayment(paymentID, pagamenti);

            kafkaTemplate.send(KAFKA_TOPIC_AGGIORNA, "Pagamento aggiornato: " + paymentID);

            return result;
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String eliminaPagamento(String paymentID) {
        logger.info("Eliminazione pagamento: paymentID={}", paymentID);
        Span span = tracer.spanBuilder("eliminaPagamento").startSpan();
        try {
            meterRegistry.counter("service.pagamenti.elimina.count").increment();
            return pagamentiRep.deletePayment(paymentID);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String salvaPagamentiCasuali(int numero) {
        logger.info("Salvataggio pagamenti casuali: numero={}", numero);
        Span span = tracer.spanBuilder("salvaPagamentiCasuali").startSpan();
        try {
            meterRegistry.counter("service.pagamenti.salva.casuali.count").increment();
            return pagamentiRep.saveAll(numero);
        } finally {
            span.end();
        }
    }
}
