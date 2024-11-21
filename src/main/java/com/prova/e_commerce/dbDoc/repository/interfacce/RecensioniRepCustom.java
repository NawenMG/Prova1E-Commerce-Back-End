package com.prova.e_commerce.dbDoc.repository.interfacce;

import com.prova.e_commerce.dbDoc.entity.Recensioni;
import com.prova.e_commerce.dbDoc.parametri.ParamQueryDbDoc;

import java.util.List;

public interface RecensioniRepCustom {
        List<Recensioni> query(ParamQueryDbDoc paramQueryDbDoc, Recensioni recensioni);

}
