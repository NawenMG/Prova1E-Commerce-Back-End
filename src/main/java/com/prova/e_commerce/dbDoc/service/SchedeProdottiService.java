package com.prova.e_commerce.dbDoc.service;

import com.prova.e_commerce.dbDoc.entity.SchedeProdotti;
import com.prova.e_commerce.dbDoc.repository.interfacce.SchedeProdottiRep;
import com.prova.e_commerce.dbDoc.repository.interfacce.SchedeProdottiRepCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SchedeProdottiService {

    @Autowired
    private SchedeProdottiRep schedeProdottiRep;

    @Autowired
    private SchedeProdottiRepCustom schedeProdottiRepCustom;

    // Metodo per cercare prodotti per nome
    public List<SchedeProdotti> findByNome(String nome) {
        return schedeProdottiRep.findByNome(nome);
    }

    // Metodo per cercare prodotti con prezzo inferiore a una certa soglia
    public List<SchedeProdotti> findByPrezzoLessThan(BigDecimal prezzo) {
        return schedeProdottiRep.findByPrezzoLessThan(prezzo);
    }

    // Metodo per cercare prodotti con criteri dinamici
    public List<SchedeProdotti> findByDynamicCriteria(
            String nome,
            BigDecimal prezzoMin,
            BigDecimal prezzoMax,
            Map<String, String> parametriDescrittivi) {
        return schedeProdottiRepCustom.findByDynamicCriteria(nome, prezzoMin, prezzoMax, parametriDescrittivi);
    }

    // Metodo per inserire un nuovo prodotto
    public SchedeProdotti insert(SchedeProdotti prodotto) {
        return schedeProdottiRep.save(prodotto);
    }

    // Metodo per aggiornare un prodotto esistente
    @Transactional
    public SchedeProdotti update(String id, SchedeProdotti prodotto) {
        Optional<SchedeProdotti> existingProdotto = schedeProdottiRep.findById(id);
        if (existingProdotto.isPresent()) {
            SchedeProdotti updatedProdotto = existingProdotto.get();
            updatedProdotto.setNome(prodotto.getNome());
            updatedProdotto.setPrezzo(prodotto.getPrezzo());
            updatedProdotto.setParametriDescrittivi(prodotto.getParametriDescrittivi());
            return schedeProdottiRep.save(updatedProdotto);
        } else {
            throw new RuntimeException("Prodotto con ID " + id + " non trovato.");
        }
    }

    // Metodo per eliminare un prodotto per ID
    public void deleteById(String id) {
        if (schedeProdottiRep.existsById(id)) {
            schedeProdottiRep.deleteById(id);
        } else {
            throw new RuntimeException("Prodotto con ID " + id + " non trovato.");
        }
    }
}
