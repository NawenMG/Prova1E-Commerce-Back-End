package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Resi;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.ResiRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResiService {

    @Autowired
    private ResiRep resiRep;

    /**
     * Metodo per eseguire una query avanzata sui resi in base a parametri dinamici.
     * Usa Caffeine per 10 minuti e Redis per 30 minuti.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #resi.toString()")
    public List<Resi> queryResi(ParamQuery paramQuery, Resi resi) {
        return resiRep.query(paramQuery, resi);
    }

    /**
     * Metodo per inserire un nuovo reso.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String inserisciReso(Resi resi) {
        return resiRep.insertReturn(resi);
    }

    /**
     * Metodo per aggiornare i dati di un reso specifico in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String aggiornaReso(String returnID, Resi resi) {
        return resiRep.updateReturn(returnID, resi);
    }

    /**
     * Metodo per eliminare un reso in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String eliminaReso(String returnID) {
        return resiRep.deleteReturn(returnID);
    }

    /**
     * Metodo per generare un numero specificato di resi con dati casuali e salvarli nel database.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String salvaResiCasuali(int numero) {
        return resiRep.saveAll(numero);
    }
}
