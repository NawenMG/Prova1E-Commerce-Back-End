package com.prova.e_commerce.dbG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.CacheEvict;
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
public class ServiceGraphDB {

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

     @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_EVENTI_VISITA = "eventi-prodotti-topic-visita";  
    private static final String TOPIC_EVENTI_ACQUISTA = "eventi-prodotti-topic-acquista";
    private static final String TOPIC_EVENTI_APPARTENENZA_CATEGORIA = "eventi-appartenenza-categoria";
    private static final String TOPIC_EVENTI_PROVENIENZA_UTENTI = "eventi-provenienza-utenti";

    public void visitaProdotto(Long utenteId, Long prodottoId) {
        customRepNeo4j.visitaProdotto(utenteId, prodottoId);
        // Invia evento Kafka per la visita
        kafkaTemplate.send(TOPIC_EVENTI_VISITA, "ProdottoVisitato", "Utente " + utenteId + " ha visitato il prodotto " + prodottoId);
    }
    

    public void acquistoProdotto(Long utenteId, Long prodottoId) {
        customRepNeo4j.acquistoProdotto(utenteId, prodottoId);
        // Invia evento Kafka per l'acquisto
        kafkaTemplate.send(TOPIC_EVENTI_ACQUISTA, "ProdottoAcquistato", "Utente " + utenteId + " ha acquistato il prodotto " + prodottoId);
    }
    

    public void appartenenzaCategoria(Long prodottoId, String categoriaNome) {
        customRepNeo4j.appartenenzaCategoria(prodottoId, categoriaNome);
        // Invia evento Kafka per l'appartenenza alla categoria
        kafkaTemplate.send(TOPIC_EVENTI_APPARTENENZA_CATEGORIA, "ProdottoCategoriaAggiunta", "Prodotto " + prodottoId + " appartiene alla categoria " + categoriaNome);
    }
    

    public void provenienzaGeografica(Long utenteId, Long prodottoId) {
        customRepNeo4j.provenienzaGeografica(utenteId, prodottoId);
        // Invia evento Kafka per la provenienza geografica
        kafkaTemplate.send(TOPIC_EVENTI_PROVENIENZA_UTENTI, "ProdottoProvenienzaGeografica", "L'utente " + utenteId + " proviene da una certa località per il prodotto " + prodottoId);
    }
    

    // Metodi POST (Creazione di nodi)
    
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public NodoUtente creaUtente(NodoUtente utente) {
        return nodoUtenteRep.save(utente);
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public NodoProdotto creaProdotto(NodoProdotto prodotto) {
        return  nodoProdottoRep.save(prodotto);
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public NodoCategoriaProdotto creaCategoriaProdotto(NodoCategoriaProdotto categoriaProdotto) {
        return nodoCategoriaProdottoRep.save(categoriaProdotto);
    }

    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public NodoLocazioneUtente creaLocazioneUtente(NodoLocazioneUtente locazioneUtente) {
        return nodoLocazioneUtenteRep.save(locazioneUtente);
    }

    // Metodi PUT (Aggiornamento di nodi)

    @Cacheable(value = {"caffeine", "redis"}, key = "#id", unless = "#result == null")
    public NodoUtente aggiornaUtente(Long id, NodoUtente utente) {
        if (nodoUtenteRep.existsById(id)) {
            utente.setId(id);
            return nodoUtenteRep.save(utente);
        } else {
            throw new RuntimeException("Utente non trovato con ID: " + id);
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#id", unless = "#result == null")
    public NodoProdotto aggiornaProdotto(Long id, NodoProdotto prodotto) {
        if (nodoProdottoRep.existsById(id)) {
            prodotto.setId(id);
            return nodoProdottoRep.save(prodotto);
        } else {
            throw new RuntimeException("Prodotto non trovato con ID: " + id);
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#id", unless = "#result == null")
    public NodoCategoriaProdotto aggiornaCategoriaProdotto(Long id, NodoCategoriaProdotto categoriaProdotto) {
        if (nodoCategoriaProdottoRep.existsById(id)) {
            categoriaProdotto.setId(id);
            return nodoCategoriaProdottoRep.save(categoriaProdotto);
        } else {
            throw new RuntimeException("Categoria prodotto non trovata con ID: " + id);
        }
    }

    @Cacheable(value = {"caffeine", "redis"}, key = "#id", unless = "#result == null")
    public NodoLocazioneUtente aggiornaLocazioneUtente(Long id, NodoLocazioneUtente locazioneUtente) {
        if (nodoLocazioneUtenteRep.existsById(id)) {
            locazioneUtente.setId(id);
            return nodoLocazioneUtenteRep.save(locazioneUtente);
        } else {
            throw new RuntimeException("Locazione utente non trovata con ID: " + id);
        }
    }

    // Metodi DELETE (Eliminazione di nodi)

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void eliminaUtente(Long id) {
        if (nodoUtenteRep.existsById(id)) {
            nodoUtenteRep.deleteById(id);
        } else {
            throw new RuntimeException("Utente non trovato con ID: " + id);
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void eliminaProdotto(Long id) {
        if (nodoProdottoRep.existsById(id)) {
            nodoProdottoRep.deleteById(id);
        } else {
            throw new RuntimeException("Prodotto non trovato con ID: " + id);
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void eliminaCategoriaProdotto(Long id) {
        if (nodoCategoriaProdottoRep.existsById(id)) {
            nodoCategoriaProdottoRep.deleteById(id);
        } else {
            throw new RuntimeException("Categoria prodotto non trovata con ID: " + id);
        }
    }

    @CacheEvict(value = {"caffeine", "redis"}, key = "#id")
    public void eliminaLocazioneUtente(Long id) {
        if (nodoLocazioneUtenteRep.existsById(id)) {
            nodoLocazioneUtenteRep.deleteById(id);
        } else {
            throw new RuntimeException("Locazione utente non trovata con ID: " + id);
        }
    }
}
