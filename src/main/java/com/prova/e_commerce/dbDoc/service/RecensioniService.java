package com.prova.e_commerce.dbDoc.service;

import com.prova.e_commerce.dbDoc.entity.Recensioni;
import com.prova.e_commerce.dbDoc.parametri.ParamQueryDbDoc;
import com.prova.e_commerce.dbDoc.randomData.RecensioniFaker;
import com.prova.e_commerce.dbDoc.repository.interfacce.RecensioniRep;
import com.prova.e_commerce.dbDoc.repository.interfacce.RecensioniRepCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecensioniService {

    @Autowired
    private RecensioniRep recensioniRep;

    @Autowired
    private RecensioniRepCustom recensioniRepCustom;

    @Autowired
    private RecensioniFaker recensioniFaker;

    // Metodo per trovare recensioni per un determinato productId
    public List<Recensioni> findByProductId(String productId) {
        return recensioniRep.findByProductId(productId);
    }

    // Metodo per trovare recensioni per un determinato userId
    public List<Recensioni> findByUserId(String userId) {
        return recensioniRep.findByUserId(userId);
    }

    // Metodo per trovare recensioni con criteri dinamici
    public List<Recensioni> queryDynamic(ParamQueryDbDoc paramQueryDbDoc, Recensioni recensioni) {
        return recensioniRepCustom.query(paramQueryDbDoc, recensioni);
    }

    // Metodo per inserire una nuova recensione
    public Recensioni saveRecensione(Recensioni recensione) {
        return recensioniRep.save(recensione);
    }

    // Metodo per aggiornare una recensione esistente
    public Recensioni updateRecensione(String id, Recensioni recensione) {
        if (recensioniRep.existsById(id)) {
            recensione.setId(id); // Impostiamo l'ID per l'update
            return recensioniRep.save(recensione);
        } else {
            return null; // Puoi gestire il caso di recensione non trovata
        }
    }

    //Generazione di una lista fittizia di recensioni
     public List<Recensioni> generateRandomRecensioni(int count) {
        List<Recensioni> recensioniList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            recensioniList.add(recensioniFaker.generateFakeReview());
        }
        return recensioniList;
    }

    // Metodo per eliminare una recensione
    public boolean deleteRecensione(String id) {
        if (recensioniRep.existsById(id)) {
            recensioniRep.deleteById(id);
            return true;
        }
        return false; // Se la recensione non esiste
    }
}
