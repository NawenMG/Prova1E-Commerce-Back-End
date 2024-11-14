package com.prova.e_commerce.dbDoc.repository.interfacce;

import com.prova.e_commerce.dbDoc.entity.SchedeProdotti;
import com.prova.e_commerce.dbDoc.parametri.ParamQueryDbDoc;

import java.util.List;

public interface SchedeProdottiRepCustom {
    List<SchedeProdotti> query(ParamQueryDbDoc paramQueryDbDoc, SchedeProdotti schedeProdotti);
}
