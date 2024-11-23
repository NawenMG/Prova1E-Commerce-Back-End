package com.prova.e_commerce.dbCol.service;

import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneTransizioniRep;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.model.ArchiviazioneTransizioni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchiviazioneTransizioniService {

    @Autowired
    private ArchiviazioneTransizioniRep archiviazioneTransizioniRep;

    /**
     * Cache per eseguire una query dinamica sulle transizioni (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #transizione.toString()")
    public List<ArchiviazioneTransizioni> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneTransizioni transizione) {
        return archiviazioneTransizioniRep.queryDinamica(paramQuery, transizione);
    }

    /**
     * Cache per trovare una transizione tramite ID (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#id")
    public ArchiviazioneTransizioni findTransizioneById(String id) {
        return archiviazioneTransizioniRep.findTransizioneById(id);
    }

    /**
     * Cache per trovare tutte le transizioni (10 minuti in Caffeine, 30 minuti in Redis).
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "'findAllTransizioni'")
    public List<ArchiviazioneTransizioni> findAllTransizioni() {
        return archiviazioneTransizioniRep.findAllTransizioni();
    }

    /**
     * Metodo per salvare una nuova transizione e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public void saveTransizione(ArchiviazioneTransizioni transizione) {
        archiviazioneTransizioniRep.saveTransizione(transizione);
    }

    /**
     * Metodo per aggiornare una transizione esistente in base all'ID e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void updateTransizione(String id, ArchiviazioneTransizioni transizione) {
        archiviazioneTransizioniRep.updateTransizione(id, transizione);
    }

    /**
     * Metodo per eliminare una transizione in base all'ID e invalidare la cache corrispondente.
     */
    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void deleteTransizione(String id) {
        archiviazioneTransizioniRep.deleteTransizione(id);
    }
}
