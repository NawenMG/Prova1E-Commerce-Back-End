package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Ordini;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.OrdiniRep;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OrdiniService {

    private static final Logger logger = LoggerFactory.getLogger(OrdiniService.class);

    @Autowired
    private OrdiniRep ordiniRep;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    private static final String PAYMENT_QUEUE = "paymentQueue";
    private static final String SHIPPING_QUEUE = "shippingQueue";

    public OrdiniService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.ordini.query.count");
        meterRegistry.counter("service.ordini.processa.count");
        meterRegistry.counter("service.ordini.aggiorna.count");
        meterRegistry.counter("service.ordini.elimina.count");
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #ordini.toString()")
    public List<Ordini> queryOrdini(ParamQuery paramQuery, Ordini ordini) {
        logger.info("Esecuzione query avanzata sugli ordini: paramQuery={}, ordini={}", paramQuery, ordini);
        Span span = tracer.spanBuilder("queryOrdini").startSpan();
        try {
            meterRegistry.counter("service.ordini.query.count").increment();
            return ordiniRep.query(paramQuery, ordini);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String processaOrdine(Pagamenti pagamenti, Ordini ordini) {
        logger.info("Processamento ordine: pagamenti={}, ordini={}", pagamenti, ordini);
        Span span = tracer.spanBuilder("processaOrdine").startSpan();
        try {
            meterRegistry.counter("service.ordini.processa.count").increment();

            rabbitTemplate.convertAndSend(PAYMENT_QUEUE, pagamenti);
            boolean pagamentoEsito = verificaPagamentoEsito();

            if (pagamentoEsito) {
                rabbitTemplate.convertAndSend(SHIPPING_QUEUE, ordini);
                boolean spedizioneEsito = verificaSpedizioneEsito();

                if (spedizioneEsito) {
                    return ordiniRep.insertOrdini(ordini);
                } else {
                    throw new RuntimeException("Errore durante la spedizione.");
                }
            } else {
                throw new RuntimeException("Pagamento rifiutato.");
            }
        } finally {
            span.end();
        }
    }

    private boolean verificaPagamentoEsito() {
        logger.info("Verifica esito pagamento.");
        return true;
    }

    private boolean verificaSpedizioneEsito() {
        logger.info("Verifica esito spedizione.");
        return true;
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String aggiornaOrdine(String orderID, Ordini ordini) {
        logger.info("Aggiornamento ordine: orderID={}, ordini={}", orderID, ordini);
        Span span = tracer.spanBuilder("aggiornaOrdine").startSpan();
        try {
            meterRegistry.counter("service.ordini.aggiorna.count").increment();
            return ordiniRep.updateOrdini(orderID, ordini);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String eliminaOrdine(String orderID) {
        logger.info("Eliminazione ordine: orderID={}", orderID);
        Span span = tracer.spanBuilder("eliminaOrdine").startSpan();
        try {
            meterRegistry.counter("service.ordini.elimina.count").increment();
            return ordiniRep.deleteOrdini(orderID);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String salvaOrdiniCasuali(int numero) {
        logger.info("Salvataggio ordini casuali: numero={}", numero);
        Span span = tracer.spanBuilder("salvaOrdiniCasuali").startSpan();
        try {
            meterRegistry.counter("service.ordini.salva.casuali.count").increment();
            return ordiniRep.saveAll(numero);
        } finally {
            span.end();
        }
    }
}
