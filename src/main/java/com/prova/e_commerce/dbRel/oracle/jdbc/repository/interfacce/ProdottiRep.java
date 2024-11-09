package com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Prodotti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;

@Repository
public interface ProdottiRep {

    List<Prodotti> query(ParamQuery parmQuery, Prodotti prodotti);
    void saveAll(int number);
    void insertProduct(Prodotti prodotti);
    void updateProduct(int productID, Prodotti prodotti);
    void deleteProduct(int productID);
}
