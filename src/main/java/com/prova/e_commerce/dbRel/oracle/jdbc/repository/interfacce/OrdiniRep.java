package com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Ordini;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;

@Repository
public interface OrdiniRep {

    List<Ordini> query(ParamQuery parmQuery, Ordini ordini);
    String saveAll(int number);
    String insertOrdini(Ordini ordini);
    String updateOrdini(int orderID, Ordini ordini);
    String deleteOrdini(int orderID);
}