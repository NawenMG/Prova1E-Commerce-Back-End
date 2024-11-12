package com.prova.e_commerce.dbDoc.repository.interfacce;

import com.prova.e_commerce.dbDoc.entity.Recensioni;

import java.util.List;
import java.util.Map;

public interface RecensioniRepCustom {
    List<Recensioni> findByDynamicCriteria(
            String userId,
            String productId,
            Integer votoMin,
            Integer votoMax,
            String titolo,
            String descrizione,
            Map<String, String> parametriAggiuntivi);
}
