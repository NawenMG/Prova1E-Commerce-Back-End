package com.prova.e_commerce.dbDoc.service;

import com.prova.e_commerce.dbDoc.entity.SchedeProdotti;
import com.prova.e_commerce.dbDoc.parametri.ParamQueryDbDoc;
import com.prova.e_commerce.dbDoc.randomData.SchedeProdottiFaker;
import com.prova.e_commerce.dbDoc.repository.interfacce.SchedeProdottiRep;
import com.prova.e_commerce.dbDoc.repository.interfacce.SchedeProdottiRepCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SchedeProdottiService {

    @Autowired
    private SchedeProdottiRep schedeProdottiRep;

    @Autowired
    private SchedeProdottiFaker prodottiFaker;

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
    public List<SchedeProdotti> queryDynamic(ParamQueryDbDoc paramQueryDbDoc, SchedeProdotti schedeProdotti) {
        return schedeProdottiRepCustom.query(paramQueryDbDoc, schedeProdotti);
    }

    // Metodo per inserire un nuovo prodotto
    public SchedeProdotti insert(SchedeProdotti prodotto) {
        return schedeProdottiRep.save(prodotto);
    }

    //Generazione di una lista fittizia di schede prodotti
     public List<SchedeProdotti> generateRandomSchedeProdotti(int count) {
        List<SchedeProdotti> schedeProdottiList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            schedeProdottiList.add(prodottiFaker.generateFakeProduct());
        }
        return schedeProdottiList;
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
