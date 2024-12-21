package com.prova.e_commerce.dbTS.service;

import com.prova.e_commerce.dbTS.model.ServerResponse;
import com.prova.e_commerce.dbTS.randomData.ServerResponseFaker;
import com.prova.e_commerce.dbTS.repository.interfacce.ServerResponseRep;

import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ServerResponseService {

    private static final Logger logger = LoggerFactory.getLogger(ServerResponseService.class);

    @Autowired
    private ServerResponseRep serverResponseRep;

    @Autowired
    private ServerResponseFaker serverResponseFaker;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    private static final String KAFKA_TOPIC_SERVER_RESPONSE_AGGIUNGI = "server-response-topic-aggiungi";
    private static final String KAFKA_TOPIC_SERVER_RESPONSE_AGGIORNA = "server-response-topic-aggiorna";

    public ServerResponseService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.serverresponse.insert.count");
        meterRegistry.counter("service.serverresponse.update.count");
        meterRegistry.counter("service.serverresponse.delete.count");
        meterRegistry.counter("service.serverresponse.query.count");
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void insert(ServerResponse serverResponse) {
        logger.info("Inserimento risposta del server: {}", serverResponse.getServer());
        Span span = tracer.spanBuilder("insertServerResponse").startSpan();
        try {
            meterRegistry.counter("service.serverresponse.insert.count").increment();
            serverResponseRep.insert(serverResponse);
            kafkaTemplate.send(KAFKA_TOPIC_SERVER_RESPONSE_AGGIUNGI, "Nuova risposta del server inserita: " + serverResponse.getServer());
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void insertBatch(List<ServerResponse> serverResponseList) {
        logger.info("Inserimento batch di risposte del server. Numero di voci: {}", serverResponseList.size());
        Span span = tracer.spanBuilder("insertBatchServerResponse").startSpan();
        try {
            meterRegistry.counter("service.serverresponse.insert.count").increment(serverResponseList.size());
            serverResponseRep.insertBatch(serverResponseList);
            kafkaTemplate.send(KAFKA_TOPIC_SERVER_RESPONSE_AGGIUNGI, "Batch di risposte del server inserito. Numero di voci: " + serverResponseList.size());
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#oldResponse.id")
    public void update(ServerResponse oldResponse, ServerResponse newResponse) {
        logger.info("Aggiornamento risposta del server: {} -> {}", oldResponse.getServer(), newResponse.getServer());
        Span span = tracer.spanBuilder("updateServerResponse").startSpan();
        try {
            meterRegistry.counter("service.serverresponse.update.count").increment();
            serverResponseRep.update(oldResponse, newResponse);
            kafkaTemplate.send(KAFKA_TOPIC_SERVER_RESPONSE_AGGIORNA, "Risposta del server aggiornata. ID: " + oldResponse.getServer() + " -> " + newResponse.getServer());
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#server")
    public void deleteByServer(String server) {
        logger.info("Eliminazione risposte del server per server: {}", server);
        Span span = tracer.spanBuilder("deleteServerResponsesByServer").startSpan();
        try {
            meterRegistry.counter("service.serverresponse.delete.count").increment();
            serverResponseRep.deleteByServer(server);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "'timeRange:' + #startTime.toString() + ':' + #endTime.toString()")
    public List<ServerResponse> findByTimeRange(Instant startTime, Instant endTime) {
        logger.info("Ricerca risposte del server nell'intervallo temporale: {} - {}", startTime, endTime);
        Span span = tracer.spanBuilder("findServerResponsesByTimeRange").startSpan();
        try {
            meterRegistry.counter("service.serverresponse.query.count").increment();
            return serverResponseRep.findByTimeRange(startTime, endTime);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#server")
    public List<ServerResponse> findByServer(String server) {
        logger.info("Ricerca risposte del server per server: {}", server);
        Span span = tracer.spanBuilder("findServerResponsesByServer").startSpan();
        try {
            meterRegistry.counter("service.serverresponse.query.count").increment();
            return serverResponseRep.findByServer(server);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#endpoint")
    public List<ServerResponse> findByEndpoint(String endpoint) {
        logger.info("Ricerca risposte del server per endpoint: {}", endpoint);
        Span span = tracer.spanBuilder("findServerResponsesByEndpoint").startSpan();
        try {
            meterRegistry.counter("service.serverresponse.query.count").increment();
            return serverResponseRep.findByEndpoint(endpoint);
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "'averageResponseTimeByServer'")
    public Map<String, Double> getAverageResponseTimeByServer() {
        logger.info("Calcolo tempo medio di risposta per server");
        Span span = tracer.spanBuilder("getAverageResponseTimeByServer").startSpan();
        try {
            meterRegistry.counter("service.serverresponse.query.count").increment();
            return serverResponseRep.getAverageResponseTimeByServer();
        } finally {
            span.end();
        }
    }

    public List<ServerResponse> generateRandomServerResponseList(int count) {
        logger.info("Generazione lista random di ServerResponse. Count: {}", count);
        Span span = tracer.spanBuilder("generateRandomServerResponseList").startSpan();
        try {
            meterRegistry.counter("service.serverresponse.random.generate.count").increment(count);
            List<ServerResponse> list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                list.add(serverResponseFaker.generateRandomServerResponse());
            }
            return list;
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "'server:' + #server + ':endpoint:' + #endpoint + ':timeRange:' + #startTime.toString() + ':' + #endTime.toString()")
public List<ServerResponse> findByServerEndpointAndTimeRange(
        String server, String endpoint, Instant startTime, Instant endTime) {
    logger.info("Ricerca risposte per server: {}, endpoint: {}, intervallo: {} - {}", server, endpoint, startTime, endTime);
    Span span = tracer.spanBuilder("findByServerEndpointAndTimeRange").startSpan();
    try {
        meterRegistry.counter("service.serverresponse.query.count").increment();
        return serverResponseRep.findByServerEndpointAndTimeRange(server, endpoint, startTime, endTime);
    } finally {
        span.end();
    }
}

}
