package com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;

@Repository
public interface PagamentiRep {
    
    List<Pagamenti> query(ParamQuery parmQuery, Pagamenti pagamenti);
    String saveAll(int number);
    String insertPayment(Pagamenti pagamenti);
    String updatePayment(int paymentID, Pagamenti pagamenti);
    String deletePayment(int paymentID);
}
