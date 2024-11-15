package com.prova.e_commerce.dbG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prova.e_commerce.dbG.model.NodoCategoriaProdotto;
import com.prova.e_commerce.dbG.model.NodoLocazioneUtente;
import com.prova.e_commerce.dbG.model.NodoProdotto;
import com.prova.e_commerce.dbG.model.NodoUtente;
import com.prova.e_commerce.dbG.repository.interfacce.CustomRepNeo4j;
import com.prova.e_commerce.dbG.repository.interfacce.NodoCategoriaProdottoRep;
import com.prova.e_commerce.dbG.repository.interfacce.NodoLocazioneUtenteRep;
import com.prova.e_commerce.dbG.repository.interfacce.NodoProdottoRep;
import com.prova.e_commerce.dbG.repository.interfacce.NodoUtenteRep;

@Service
public class ServiceGraphDB{

    @Autowired
    private NodoUtenteRep nodoUtenteRep;

    @Autowired
    private NodoProdottoRep nodoProdottoRep;

    @Autowired
    private NodoCategoriaProdottoRep nodoCategoriaProdottoRep;

    @Autowired
    private NodoLocazioneUtenteRep nodoLocazioneUtenteRep;

    @Autowired
    private CustomRepNeo4j customRepNeo4j;

    public void visitaProdotto(Long utenteId, Long prodottoId) {
        customRepNeo4j.visitaProdotto(utenteId, prodottoId);
    }

    public void acquistoProdotto(Long utenteId, Long prodottoId) {
        customRepNeo4j.acquistoProdotto(utenteId, prodottoId);
    }

    public void appartenenzaCategoria(Long prodottoId, String categoriaNome) {
        customRepNeo4j.appartenenzaCategoria(prodottoId, categoriaNome);
    }

    public void provenienzaGeografica(Long utenteId, Long prodottoId) {
        customRepNeo4j.provenienzaGeografica(utenteId, prodottoId);
    }



     // Metodi POST (Creazione di nodi)
    
     public NodoUtente creaUtente(NodoUtente utente) {
        return nodoUtenteRep.save(utente);
    }

    public NodoProdottoRep creaProdotto(NodoProdotto prodotto) {
        return (NodoProdottoRep) nodoProdottoRep.save(prodotto);
    }

    public NodoCategoriaProdotto creaCategoriaProdotto(NodoCategoriaProdotto categoriaProdotto) {
        return nodoCategoriaProdottoRep.save(categoriaProdotto);
    }

    public NodoLocazioneUtente creaLocazioneUtente(NodoLocazioneUtente locazioneUtente) {
        return nodoLocazioneUtenteRep.save(locazioneUtente);
    }

    // Metodi PUT (Aggiornamento di nodi)

    public NodoUtente aggiornaUtente(Long id, NodoUtente utente) {
        if (nodoUtenteRep.existsById(id)) {
            utente.setId(id);
            return nodoUtenteRep.save(utente);
        } else {
            throw new RuntimeException("Utente non trovato con ID: " + id);
        }
    }

    public NodoProdotto aggiornaProdotto(Long id, NodoProdotto prodotto) {
        if (nodoProdottoRep.existsById(id)) {
            prodotto.setId(id);
            return nodoProdottoRep.save(prodotto);
        } else {
            throw new RuntimeException("Prodotto non trovato con ID: " + id);
        }
    }

    public NodoCategoriaProdotto aggiornaCategoriaProdotto(Long id, NodoCategoriaProdotto categoriaProdotto) {
        if (nodoCategoriaProdottoRep.existsById(id)) {
            categoriaProdotto.setId(id);
            return nodoCategoriaProdottoRep.save(categoriaProdotto);
        } else {
            throw new RuntimeException("Categoria prodotto non trovata con ID: " + id);
        }
    }

    public NodoLocazioneUtente aggiornaLocazioneUtente(Long id, NodoLocazioneUtente locazioneUtente) {
        if (nodoLocazioneUtenteRep.existsById(id)) {
            locazioneUtente.setId(id);
            return nodoLocazioneUtenteRep.save(locazioneUtente);
        } else {
            throw new RuntimeException("Locazione utente non trovata con ID: " + id);
        }
    }

    // Metodi DELETE (Eliminazione di nodi)

    public void eliminaUtente(Long id) {
        if (nodoUtenteRep.existsById(id)) {
            nodoUtenteRep.deleteById(id);
        } else {
            throw new RuntimeException("Utente non trovato con ID: " + id);
        }
    }

    public void eliminaProdotto(Long id) {
        if (nodoProdottoRep.existsById(id)) {
            nodoProdottoRep.deleteById(id);
        } else {
            throw new RuntimeException("Prodotto non trovato con ID: " + id);
        }
    }

    public void eliminaCategoriaProdotto(Long id) {
        if (nodoCategoriaProdottoRep.existsById(id)) {
            nodoCategoriaProdottoRep.deleteById(id);
        } else {
            throw new RuntimeException("Categoria prodotto non trovata con ID: " + id);
        }
    }

    public void eliminaLocazioneUtente(Long id) {
        if (nodoLocazioneUtenteRep.existsById(id)) {
            nodoLocazioneUtenteRep.deleteById(id);
        } else {
            throw new RuntimeException("Locazione utente non trovata con ID: " + id);
        }
    }
}
