package com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Prodotti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;

@Repository
public interface ProdottiRep {

    List<Prodotti> query(ParamQuery parmQuery, Prodotti prodotti);
    String saveAll(int number);
    String insertProduct(Prodotti prodotti, String UserID);
    String updateProduct(String productID, Prodotti prodotti);
    String deleteProduct(String productID);
}
