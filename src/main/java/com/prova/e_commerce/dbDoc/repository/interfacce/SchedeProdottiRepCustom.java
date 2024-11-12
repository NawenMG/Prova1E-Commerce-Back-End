package com.prova.e_commerce.dbDoc.repository.interfacce;

import com.prova.e_commerce.dbDoc.entity.SchedeProdotti;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SchedeProdottiRepCustom {
    List<SchedeProdotti> findByDynamicCriteria(
        String nome,
        BigDecimal prezzoMin,
        BigDecimal prezzoMax,
        Map<String, String> parametriDescrittivi);
}
