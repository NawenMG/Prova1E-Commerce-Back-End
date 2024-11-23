package com.prova.e_commerce.dbCol.service;

import com.prova.e_commerce.dbCol.model.ArchiviazioneOrdini;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneOrdiniRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchiviazioneOrdiniService {

    @Autowired
    private ArchiviazioneOrdiniRep archiviazioneOrdiniRep;

    /**
     * Cache per eseguire una query dinamica sugli ordini (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #ordine.toString()")
    public List<ArchiviazioneOrdini> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneOrdini ordine) {
        return archiviazioneOrdiniRep.queryDinamica(paramQuery, ordine);
    }

    /**
     * Cache per trovare un ordine tramite ID (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#id")
    public ArchiviazioneOrdini findOrdineById(String id) {
        return archiviazioneOrdiniRep.findOrdineById(id);
    }

    /**
     * Cache per trovare tutti gli ordini (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "'findAll'")
    public List<ArchiviazioneOrdini> findAllOrdini() {
        return archiviazioneOrdiniRep.findAllOrdini();
    }

    /**
     * Metodo per salvare un nuovo ordine e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void saveOrdine(ArchiviazioneOrdini ordine) {
        archiviazioneOrdiniRep.saveOrdine(ordine);
    }

    /**
     * Metodo per aggiornare un ordine esistente e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void updateOrdine(String id, ArchiviazioneOrdini ordine) {
        archiviazioneOrdiniRep.updateOrdine(id, ordine);
    }

    /**
     * Metodo per eliminare un ordine e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void deleteOrdine(String id) {
        archiviazioneOrdiniRep.deleteOrdine(id);
    }
}
