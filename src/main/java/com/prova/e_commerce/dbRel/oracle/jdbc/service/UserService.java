package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.User;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    public UserService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.user.query.count");
        meterRegistry.counter("service.user.create.count");
        meterRegistry.counter("service.user.update.count");
        meterRegistry.counter("service.user.delete.count");
    }

    public Optional<User> findByNomeUtente(String nomeUtente) {
        logger.info("Ricerca utente per nome: {}", nomeUtente);
        Span span = tracer.spanBuilder("findByNomeUtente").startSpan();
        try {
            meterRegistry.counter("service.user.query.count").increment();
            return userRepository.findByNomeUtente(nomeUtente);
        } finally {
            span.end();
        }
    }

    public Optional<User> findByEmail(String email) {
        logger.info("Ricerca utente per email: {}", email);
        Span span = tracer.spanBuilder("findByEmail").startSpan();
        try {
            meterRegistry.counter("service.user.query.count").increment();
            return userRepository.findByEmail(email);
        } finally {
            span.end();
        }
    }

    public boolean existsByNomeUtente(String nomeUtente) {
        logger.info("Verifica esistenza utente per nome: {}", nomeUtente);
        Span span = tracer.spanBuilder("existsByNomeUtente").startSpan();
        try {
            meterRegistry.counter("service.user.query.count").increment();
            return userRepository.existsByNomeUtente(nomeUtente);
        } finally {
            span.end();
        }
    }

    public boolean existsByEmail(String email) {
        logger.info("Verifica esistenza utente per email: {}", email);
        Span span = tracer.spanBuilder("existsByEmail").startSpan();
        try {
            meterRegistry.counter("service.user.query.count").increment();
            return userRepository.existsByEmail(email);
        } finally {
            span.end();
        }
    }

    public User createUser(User user) {
        logger.info("Creazione utente: {}", user);
        Span span = tracer.spanBuilder("createUser").startSpan();
        try {
            meterRegistry.counter("service.user.create.count").increment();
            if (userRepository.existsByNomeUtente(user.getNomeUtente())) {
                throw new IllegalArgumentException("Nome utente già esistente");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email già utilizzata");
            }
            return userRepository.save(user);
        } finally {
            span.end();
        }
    }

    public User updateUser(User user) {
        logger.info("Aggiornamento utente: {}", user);
        Span span = tracer.spanBuilder("updateUser").startSpan();
        try {
            meterRegistry.counter("service.user.update.count").increment();
            if (!userRepository.findByNomeUtente(user.getNomeUtente()).isPresent()) {
                throw new IllegalArgumentException("Utente non trovato");
            }
            return userRepository.save(user);
        } finally {
            span.end();
        }
    }

    public void deleteUser(String nomeUtente) {
        logger.info("Eliminazione utente: {}", nomeUtente);
        Span span = tracer.spanBuilder("deleteUser").startSpan();
        try {
            meterRegistry.counter("service.user.delete.count").increment();
            Optional<User> user = userRepository.findByNomeUtente(nomeUtente);
            if (user.isPresent()) {
                userRepository.delete(user.get());
            } else {
                throw new IllegalArgumentException("Utente non trovato");
            }
        } finally {
            span.end();
        }
    }

    public Iterable<User> getAllUsers() {
        logger.info("Recupero di tutti gli utenti.");
        Span span = tracer.spanBuilder("getAllUsers").startSpan();
        try {
            meterRegistry.counter("service.user.query.count").increment();
            return userRepository.findAll();
        } finally {
            span.end();
        }
    }
}