package com.prova.e_commerce.dbG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.prova.e_commerce.dbG.model.NodoCategoriaProdotto;
import com.prova.e_commerce.dbG.model.NodoLocazioneUtente;
import com.prova.e_commerce.dbG.model.NodoProdotto;
import com.prova.e_commerce.dbG.model.NodoUtente;
import com.prova.e_commerce.dbG.repository.interfacce.CustomRepNeo4j;
import com.prova.e_commerce.dbG.repository.interfacce.NodoCategoriaProdottoRep;
import com.prova.e_commerce.dbG.repository.interfacce.NodoLocazioneUtenteRep;
import com.prova.e_commerce.dbG.repository.interfacce.NodoProdottoRep;
import com.prova.e_commerce.dbG.repository.interfacce.NodoUtenteRep;
import com.prova.e_commerce.security.security1.SecurityUtils;

import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ServiceGraphDB {

    private static final Logger logger = LoggerFactory.getLogger(ServiceGraphDB.class);

    private static final String TOPIC_EVENTI_VISITA = "eventi-prodotti-topic-visita";
    private static final String TOPIC_EVENTI_ACQUISTA = "eventi-prodotti-topic-acquista";
    private static final String TOPIC_EVENTI_APPARTENENZA_CATEGORIA = "eventi-appartenenza-categoria";
    private static final String TOPIC_EVENTI_PROVENIENZA_UTENTI = "eventi-provenienza-utenti";

    @Autowired
    private NodoUtenteRep nodoUtenteRep;

    @Autowired
    private NodoProdottoRep nodoProdottoRep;

    @Autowired
    private NodoCategoriaProdottoRep nodoCategoriaProdottoRep;

    @Autowired
    private NodoLocazioneUtenteRep nodoLocazioneUtenteRep;

    @Autowired
    private CustomRepNeo4j customRepNeo4j;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    public ServiceGraphDB(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.graphdb.visita.count");
        meterRegistry.counter("service.graphdb.acquisto.count");
        meterRegistry.counter("service.graphdb.appartenenza.count");
        meterRegistry.counter("service.graphdb.provenienza.count");
    }

    // Metodo per ottenere tutte le visite effettuate da un utente su prodotti
    @Cacheable(value = {"caffeine", "redis"}, key = "'visiteUtente-findAll'")
    public List<String> getVisiteUtente(Long utenteId) {
        logger.info("Recupero visite per utenteId={}", utenteId);
        return customRepNeo4j.getVisiteUtente(utenteId).stream()
                .map(visita -> "Utente " + utenteId + " ha visitato il prodotto " + visita)
                .collect(Collectors.toList());
    }

    // Metodo per ottenere tutti gli acquisti effettuati da un utente
    @Cacheable(value = {"caffeine", "redis"}, key = "'acquistiUtente-findAll'")
    public List<String> getAcquistiUtente(Long utenteId) {
        logger.info("Recupero acquisti per utenteId={}", utenteId);
        return customRepNeo4j.getAcquistiUtente(utenteId).stream()
                .map(acquisto -> "Utente " + utenteId + " ha acquistato il prodotto " + acquisto)
                .collect(Collectors.toList());
    }

    // Metodo per ottenere tutte le categorie assegnate a un prodotto
    @Cacheable(value = {"caffeine", "redis"}, key = "'categorieProdotto-findAll'")
    public List<String> getCategorieProdotto(Long prodottoId) {
        logger.info("Recupero categorie per prodottoId={}", prodottoId);
        return customRepNeo4j.getCategorieProdotto(prodottoId).stream()
                .map(categoria -> "Prodotto " + prodottoId + " appartiene alla categoria " + categoria)
                .collect(Collectors.toList());
    }

    // Metodo per ottenere la provenienza geografica di un utente
    @Cacheable(value = {"caffeine", "redis"}, key = "'provenienzeUtente-findAll'")
    public List<String> getProvenienzeUtente(Long utenteId) {
        logger.info("Recupero provenienze per utenteId={}", utenteId);
        return customRepNeo4j.getProvenienzeUtente(utenteId).stream()
                .map(provenienza -> "L'utente " + utenteId + " proviene da " + provenienza)
                .collect(Collectors.toList());
    }

    public void visitaProdotto(Long utenteId, Long prodottoId) {
        logger.info("Visita prodotto: utenteId={}, prodottoId={}", utenteId, prodottoId);
        Span span = tracer.spanBuilder("visitaProdotto").startSpan();
        try {
            meterRegistry.counter("service.graphdb.visita.count").increment();
            customRepNeo4j.visitaProdotto(utenteId, prodottoId);
            kafkaTemplate.send(TOPIC_EVENTI_VISITA, "ProdottoVisitato", "Utente " + utenteId + " ha visitato il prodotto " + prodottoId);
        } finally {
            span.end();
        }
    }

    public void acquistoProdotto(Long utenteId, Long prodottoId) {
        logger.info("Acquisto prodotto: utenteId={}, prodottoId={}", utenteId, prodottoId);
        Span span = tracer.spanBuilder("acquistoProdotto").startSpan();
        try {
            meterRegistry.counter("service.graphdb.acquisto.count").increment();
            customRepNeo4j.acquistoProdotto(utenteId, prodottoId);
            kafkaTemplate.send(TOPIC_EVENTI_ACQUISTA, "ProdottoAcquistato", "Utente " + utenteId + " ha acquistato il prodotto " + prodottoId);
        } finally {
            span.end();
        }
    }

    public void appartenenzaCategoria(Long prodottoId, String categoriaNome) {
        logger.info("Appartenenza categoria: prodottoId={}, categoriaNome={}", prodottoId, categoriaNome);
        Span span = tracer.spanBuilder("appartenenzaCategoria").startSpan();
        try {
            meterRegistry.counter("service.graphdb.appartenenza.count").increment();
            customRepNeo4j.appartenenzaCategoria(prodottoId, categoriaNome);
            kafkaTemplate.send(TOPIC_EVENTI_APPARTENENZA_CATEGORIA, "ProdottoCategoriaAggiunta", "Prodotto " + prodottoId + " appartiene alla categoria " + categoriaNome);
        } finally {
            span.end();
        }
    }

    public void provenienzaGeografica(Long utenteId, Long prodottoId) {
        logger.info("Provenienza geografica: utenteId={}, prodottoId={}", utenteId, prodottoId);
        Span span = tracer.spanBuilder("provenienzaGeografica").startSpan();
        try {
            meterRegistry.counter("service.graphdb.provenienza.count").increment();
            customRepNeo4j.provenienzaGeografica(utenteId, prodottoId);
            kafkaTemplate.send(TOPIC_EVENTI_PROVENIENZA_UTENTI, "ProdottoProvenienzaGeografica", "L'utente " + utenteId + " proviene da una certa località per il prodotto " + prodottoId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public NodoUtente creaUtente(NodoUtente utente) {
        logger.info("Creazione utente: {}", utente);
        Span span = tracer.spanBuilder("creaUtente").startSpan();
        try {
            return nodoUtenteRep.save(utente, SecurityUtils.getCurrentUsername());
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public NodoProdotto creaProdotto(NodoProdotto prodotto) {
        logger.info("Creazione prodotto: {}", prodotto);
        Span span = tracer.spanBuilder("creaProdotto").startSpan();
        try {
            return nodoProdottoRep.save(prodotto, SecurityUtils.getCurrentUsername());
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public NodoCategoriaProdotto creaCategoriaProdotto(NodoCategoriaProdotto categoriaProdotto) {
        logger.info("Creazione categoria prodotto: {}", categoriaProdotto);
        Span span = tracer.spanBuilder("creaCategoriaProdotto").startSpan();
        try {
            return nodoCategoriaProdottoRep.save(categoriaProdotto, SecurityUtils.getCurrentUsername());
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public NodoLocazioneUtente creaLocazioneUtente(NodoLocazioneUtente locazioneUtente) {
        logger.info("Creazione locazione utente: {}", locazioneUtente);
        Span span = tracer.spanBuilder("creaLocazioneUtente").startSpan();
        try {
            return nodoLocazioneUtenteRep.save(locazioneUtente, SecurityUtils.getCurrentUsername());
        } finally {
            span.end();
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#id", unless = "#result == null")
    public NodoUtente aggiornaUtente(Long id, NodoUtente utente) {
        if (nodoUtenteRep.existsById(id)) {
            logger.info("Aggiornamento utente con ID: {}", id);
            utente.setId(id);
            return nodoUtenteRep.save(utente);
        } else {
            throw new RuntimeException("Utente non trovato con ID: " + id);
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#id", unless = "#result == null")
    public NodoProdotto aggiornaProdotto(Long id, NodoProdotto prodotto) {
        if (nodoProdottoRep.existsById(id)) {
            logger.info("Aggiornamento prodotto con ID: {}", id);
            prodotto.setId(id);
            return nodoProdottoRep.save(prodotto);
        } else {
            throw new RuntimeException("Prodotto non trovato con ID: " + id);
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#id", unless = "#result == null")
    public NodoCategoriaProdotto aggiornaCategoriaProdotto(Long id, NodoCategoriaProdotto categoriaProdotto) {
        if (nodoCategoriaProdottoRep.existsById(id)) {
            logger.info("Aggiornamento categoria prodotto con ID: {}", id);
            categoriaProdotto.setId(id);
            return nodoCategoriaProdottoRep.save(categoriaProdotto);
        } else {
            throw new RuntimeException("Categoria prodotto non trovata con ID: " + id);
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#id", unless = "#result == null")
    public NodoLocazioneUtente aggiornaLocazioneUtente(Long id, NodoLocazioneUtente locazioneUtente) {
        if (nodoLocazioneUtenteRep.existsById(id)) {
            logger.info("Aggiornamento locazione utente con ID: {}", id);
            locazioneUtente.setId(id);
            return nodoLocazioneUtenteRep.save(locazioneUtente);
        } else {
            throw new RuntimeException("Locazione utente non trovata con ID: " + id);
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void eliminaUtente(Long id) {
        logger.info("Eliminazione utente con ID: {}", id);
        if (nodoUtenteRep.existsById(id)) {
            nodoUtenteRep.deleteById(id);
        } else {
            throw new RuntimeException("Utente non trovato con ID: " + id);
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void eliminaProdotto(Long id) {
        logger.info("Eliminazione prodotto con ID: {}", id);
        if (nodoProdottoRep.existsById(id)) {
            nodoProdottoRep.deleteById(id);
        } else {
            throw new RuntimeException("Prodotto non trovato con ID: " + id);
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void eliminaCategoriaProdotto(Long id) {
        logger.info("Eliminazione categoria prodotto con ID: {}", id);
        if (nodoCategoriaProdottoRep.existsById(id)) {
            nodoCategoriaProdottoRep.deleteById(id);
        } else {
            throw new RuntimeException("Categoria prodotto non trovata con ID: " + id);
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void eliminaLocazioneUtente(Long id) {
        logger.info("Eliminazione locazione utente con ID: {}", id);
        if (nodoLocazioneUtenteRep.existsById(id)) {
            nodoLocazioneUtenteRep.deleteById(id);
        } else {
            throw new RuntimeException("Locazione utente non trovata con ID: " + id);
        }
    }
}
