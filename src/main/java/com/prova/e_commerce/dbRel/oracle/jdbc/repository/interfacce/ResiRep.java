package com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Resi;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;

@Repository
public interface ResiRep {
    List<Resi> query(ParamQuery parmQuery, Resi resi);
    void saveAll(int number);
    void insertReturn(Resi resi);
    void updateReturn(int returnID, Resi resi);
    void deleteReturn(int returnID);
}
