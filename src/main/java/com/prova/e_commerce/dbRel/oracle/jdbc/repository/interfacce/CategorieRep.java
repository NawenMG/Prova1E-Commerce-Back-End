package com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Categorie;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;

@Repository
public interface CategorieRep {

    List<Categorie> query(ParamQuery parmQuery, Categorie categorie);
    String saveAll(int number);
    String insertCategory(Categorie category);
    String updateCategory(int categoryID, Categorie category);
    String deleteCategory(int categoryID);
}