package com.prova.e_commerce.dbG.controller.rest;

import com.prova.e_commerce.dbG.model.NodoCategoriaProdotto;
import com.prova.e_commerce.dbG.model.NodoLocazioneUtente;
import com.prova.e_commerce.dbG.model.NodoProdotto;
import com.prova.e_commerce.dbG.model.NodoUtente;
import com.prova.e_commerce.dbG.service.ServiceGraphDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ControllerRestGraphDB {

    @Autowired
    private ServiceGraphDB serviceGraphDB;

    // POST - Crea un nuovo Utente
    @PostMapping("/utenti")
    public ResponseEntity<NodoUtente> creaUtente(@RequestBody NodoUtente utente) {
        NodoUtente nuovoUtente = serviceGraphDB.creaUtente(utente);
        return new ResponseEntity<>(nuovoUtente, HttpStatus.CREATED);
    }

    // POST - Crea un nuovo Prodotto
    @PostMapping("/prodotti")
    public ResponseEntity<NodoProdotto> creaProdotto(@RequestBody NodoProdotto prodotto) {
        NodoProdotto nuovoProdotto = (NodoProdotto) serviceGraphDB.creaProdotto(prodotto);
        return new ResponseEntity<>(nuovoProdotto, HttpStatus.CREATED);
    }

    // POST - Crea una nuova CategoriaProdotto
    @PostMapping("/categorie-prodotti")
    public ResponseEntity<NodoCategoriaProdotto> creaCategoriaProdotto(@RequestBody NodoCategoriaProdotto categoriaProdotto) {
        NodoCategoriaProdotto nuovaCategoria = serviceGraphDB.creaCategoriaProdotto(categoriaProdotto);
        return new ResponseEntity<>(nuovaCategoria, HttpStatus.CREATED);
    }

    // POST - Crea una nuova LocazioneUtente
    @PostMapping("/locazioni-utenti")
    public ResponseEntity<NodoLocazioneUtente> creaLocazioneUtente(@RequestBody NodoLocazioneUtente locazioneUtente) {
        NodoLocazioneUtente nuovaLocazione = serviceGraphDB.creaLocazioneUtente(locazioneUtente);
        return new ResponseEntity<>(nuovaLocazione, HttpStatus.CREATED);
    }

    // PUT - Aggiorna un Utente esistente
    @PutMapping("/utenti/{id}")
    public ResponseEntity<NodoUtente> aggiornaUtente(@PathVariable Long id, @RequestBody NodoUtente utente) {
        try {
            NodoUtente utenteAggiornato = serviceGraphDB.aggiornaUtente(id, utente);
            return new ResponseEntity<>(utenteAggiornato, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // PUT - Aggiorna un Prodotto esistente
    @PutMapping("/prodotti/{id}")
    public ResponseEntity<NodoProdotto> aggiornaProdotto(@PathVariable Long id, @RequestBody NodoProdotto prodotto) {
        try {
            NodoProdotto prodottoAggiornato = serviceGraphDB.aggiornaProdotto(id, prodotto);
            return new ResponseEntity<>(prodottoAggiornato, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // PUT - Aggiorna una CategoriaProdotto esistente
    @PutMapping("/categorie-prodotti/{id}")
    public ResponseEntity<NodoCategoriaProdotto> aggiornaCategoriaProdotto(@PathVariable Long id, @RequestBody NodoCategoriaProdotto categoriaProdotto) {
        try {
            NodoCategoriaProdotto categoriaProdottoAggiornata = serviceGraphDB.aggiornaCategoriaProdotto(id, categoriaProdotto);
            return new ResponseEntity<>(categoriaProdottoAggiornata, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // PUT - Aggiorna una LocazioneUtente esistente
    @PutMapping("/locazioni-utenti/{id}")
    public ResponseEntity<NodoLocazioneUtente> aggiornaLocazioneUtente(@PathVariable Long id, @RequestBody NodoLocazioneUtente locazioneUtente) {
        try {
            NodoLocazioneUtente locazioneUtenteAggiornata = serviceGraphDB.aggiornaLocazioneUtente(id, locazioneUtente);
            return new ResponseEntity<>(locazioneUtenteAggiornata, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE - Elimina un Utente
    @DeleteMapping("/utenti/{id}")
    public ResponseEntity<Void> eliminaUtente(@PathVariable Long id) {
        try {
            serviceGraphDB.eliminaUtente(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE - Elimina un Prodotto
    @DeleteMapping("/prodotti/{id}")
    public ResponseEntity<Void> eliminaProdotto(@PathVariable Long id) {
        try {
            serviceGraphDB.eliminaProdotto(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE - Elimina una CategoriaProdotto
    @DeleteMapping("/categorie-prodotti/{id}")
    public ResponseEntity<Void> eliminaCategoriaProdotto(@PathVariable Long id) {
        try {
            serviceGraphDB.eliminaCategoriaProdotto(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE - Elimina una LocazioneUtente
    @DeleteMapping("/locazioni-utenti/{id}")
    public ResponseEntity<Void> eliminaLocazioneUtente(@PathVariable Long id) {
        try {
            serviceGraphDB.eliminaLocazioneUtente(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
