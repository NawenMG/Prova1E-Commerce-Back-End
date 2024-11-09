package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Categorie;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.CategorieRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {
    
    @Autowired
    private CategorieRep categorieRep;
    
    /**
     * Metodo per eseguire una query avanzata sulle categorie in base a parametri dinamici.
     */
    public List<Categorie> queryCategorie(ParamQuery paramQuery, Categorie categorie) {
        return categorieRep.query(paramQuery, categorie);
    }

    /**
     * Metodo per inserire una nuova categoria.
     */
    public String inserisciCategoria(Categorie categorie) {
        return categorieRep.insertCategory(categorie);
    }

    /**
     * Metodo per aggiornare una categoria esistente in base all'ID.
     */
    public String aggiornaCategoria(int categoryID, Categorie categorie) {
        return categorieRep.updateCategory(categoryID, categorie);
    }

    /**
     * Metodo per eliminare una categoria in base all'ID.
     */
    public String eliminaCategoria(int categoryID) {
        return categorieRep.deleteCategory(categoryID);
    }

    /**
     * Metodo per salvare un insieme di categorie generate in modo casuale.
     */
    public String salvaCategorieCasuali(int numero) {
        return categorieRep.saveAll(numero);
    }
}
