package com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Ordini;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;

@Repository
public interface OrdiniRep {

    List<Ordini> query(ParamQuery parmQuery, Ordini ordini);
    void saveAll(int number);
    void insertOrdini(Ordini ordini);
    void updateOrdini(int orderID, Ordini ordini);
    void deleteOrdini(int orderID);
}