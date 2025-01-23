package com.prova.e_commerce.dbKey.service;

import com.prova.e_commerce.dbKey.model.SettingSite;
import com.prova.e_commerce.dbKey.repository.interfacce.SettingSiteRep;
import com.prova.e_commerce.security.security1.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class SettingSiteService {

    private static final Logger logger = LoggerFactory.getLogger(SettingSiteService.class);

    @Autowired
    private SettingSiteRep settingSiteRep;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private Tracer tracer;

    public SettingSiteService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("service.settingsite.trova.count");
        meterRegistry.counter("service.settingsite.salva.count");
        meterRegistry.counter("service.settingsite.reset.count");
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#userId", unless = "#result == null")
    public Optional<SettingSite> trovaImpostazioni(String userId) {
        userId = SecurityUtils.getCurrentUsername();
        logger.info("Recupero impostazioni per l'utente: userId={}", userId);
        Span span = tracer.spanBuilder("trovaImpostazioni").startSpan();
        try {
            meterRegistry.counter("service.settingsite.trova.count").increment();
            return settingSiteRep.trovaImpostazioni(userId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void salvaImpostazioni(String userId, SettingSite settings) {
        userId = SecurityUtils.getCurrentUsername();
        logger.info("Salvataggio impostazioni per l'utente: userId={}, settings={}", userId, settings);
        Span span = tracer.spanBuilder("salvaImpostazioni").startSpan();
        try {
            meterRegistry.counter("service.settingsite.salva.count").increment();
            settingSiteRep.salvaImpostazioni(userId, settings);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    public void resetImpostazioni(String userId) {
        userId = SecurityUtils.getCurrentUsername();
        logger.info("Reset delle impostazioni per l'utente: userId={}", userId);
        Span span = tracer.spanBuilder("resetImpostazioni").startSpan();
        try {
            meterRegistry.counter("service.settingsite.reset.count").increment();
            settingSiteRep.resetImpostazioni(userId);
        } finally {
            span.end();
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#userId", allEntries = false)
    private void evictSettingCache(String userId) {
        logger.info("Invalidazione della cache per l'utente: userId={}", userId);
    }
}
