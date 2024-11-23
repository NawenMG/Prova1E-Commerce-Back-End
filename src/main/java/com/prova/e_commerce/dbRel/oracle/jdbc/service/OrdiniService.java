package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Ordini;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.OrdiniRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdiniService {
    
    @Autowired
    private OrdiniRep ordiniRep;

    /**
     * Metodo per eseguire una query avanzata sugli ordini in base a parametri dinamici.
     * Utilizza Caffeine per 10 minuti e Redis per 30 minuti.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #ordini.toString()")
    public List<Ordini> queryOrdini(ParamQuery paramQuery, Ordini ordini) {
        return ordiniRep.query(paramQuery, ordini);
    }

    /**
     * Metodo per inserire un nuovo ordine.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String inserisciOrdine(Ordini ordini) {
        return ordiniRep.insertOrdini(ordini);
    }

    /**
     * Metodo per aggiornare un ordine esistente in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String aggiornaOrdine(String orderID, Ordini ordini) {
        return ordiniRep.updateOrdini(orderID, ordini);
    }

    /**
     * Metodo per eliminare un ordine in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String eliminaOrdine(String orderID) {
        return ordiniRep.deleteOrdini(orderID);
    }

    /**
     * Metodo per generare un numero specificato di ordini con dati casuali.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String salvaOrdiniCasuali(int numero) {
        return ordiniRep.saveAll(numero);
    }
}
