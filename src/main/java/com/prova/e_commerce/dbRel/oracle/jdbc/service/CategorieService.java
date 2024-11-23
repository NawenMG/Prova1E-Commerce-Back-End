package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Categorie;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.CategorieRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {
    
    @Autowired
    private CategorieRep categorieRep;
    
    /**
     * Metodo per eseguire una query avanzata sulle categorie in base a parametri dinamici.
     * Utilizza Caffeine per 10 minuti e Redis per 30 minuti.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #categorie.toString()")
    public List<Categorie> queryCategorie(ParamQuery paramQuery, Categorie categorie) {
        return categorieRep.query(paramQuery, categorie);
    }

    /**
     * Metodo per inserire una nuova categoria.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String inserisciCategoria(Categorie categorie) {
        return categorieRep.insertCategory(categorie);
    }

    /**
     * Metodo per aggiornare una categoria esistente in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String aggiornaCategoria(String categoryID, Categorie categorie) {
        return categorieRep.updateCategory(categoryID, categorie);
    }

    /**
     * Metodo per eliminare una categoria in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String eliminaCategoria(String categoryID) {
        return categorieRep.deleteCategory(categoryID);
    }

    /**
     * Metodo per salvare un insieme di categorie generate in modo casuale.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String salvaCategorieCasuali(int numero) {
        return categorieRep.saveAll(numero);
    }
}
