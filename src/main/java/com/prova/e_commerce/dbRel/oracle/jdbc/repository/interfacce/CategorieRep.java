package com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Categorie;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;

@Repository
public interface CategorieRep {

    List<Categorie> query(ParamQuery parmQuery, Categorie categorie);
    void saveAll(int number);
    void insertCategory(Categorie category);
    void updateCategory(int categoryID, Categorie category);
    void deleteCategory(int categoryID);
}