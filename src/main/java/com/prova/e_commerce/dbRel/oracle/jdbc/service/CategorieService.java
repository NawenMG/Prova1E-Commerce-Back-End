package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Categorie;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.CategorieRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {
    
    @Autowired
    private CategorieRep categorieRep;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;  // Template per inviare messaggi Kafka

    private static final String TOPIC_EVENTI_CATEGORIA_AGGIUNGI = "eventi-categoria-topic-aggiungi";  
    private static final String TOPIC_EVENTI_CATEGORIA_AGGIORNA = "eventi-categoria-topic-aggiorna";  


    
    /**
     * Metodo per eseguire una query avanzata sulle categorie in base a parametri dinamici.
     * Utilizza Caffeine per 10 minuti e Redis per 30 minuti.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #categorie.toString()")
    public List<Categorie> queryCategorie(ParamQuery paramQuery, Categorie categorie) {
        return categorieRep.query(paramQuery, categorie);
    }

    /**
     * Metodo per inserire una nuova categoria.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Pubblica un evento Kafka per l'inserimento della categoria.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String inserisciCategoria(Categorie categorie) {
        String result = categorieRep.insertCategory(categorie);

        // Pubblica un messaggio Kafka per l'inserimento della nuova categoria
        kafkaTemplate.send(TOPIC_EVENTI_CATEGORIA_AGGIUNGI, "Inserimento", "Categoria inserita: " + categorie.getName());

        return result;
    }

    /**
     * Metodo per aggiornare una categoria esistente in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Pubblica un evento Kafka per l'aggiornamento della categoria.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String aggiornaCategoria(String categoryID, Categorie categorie) {
        String result = categorieRep.updateCategory(categoryID, categorie);

        // Pubblica un messaggio Kafka per l'aggiornamento della categoria
        kafkaTemplate.send(TOPIC_EVENTI_CATEGORIA_AGGIORNA, "Aggiornamento", "Categoria aggiornata con ID: " + categoryID);

        return result;
    }

    /**
     * Metodo per eliminare una categoria in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Pubblica un evento Kafka per l'eliminazione della categoria.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String eliminaCategoria(String categoryID) {
        String result = categorieRep.deleteCategory(categoryID);

        return result;
    }

    /**
     * Metodo per salvare un insieme di categorie generate in modo casuale.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Pubblica un evento Kafka per il salvataggio delle categorie.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String salvaCategorieCasuali(int numero) {
        String result = categorieRep.saveAll(numero);

        return result;
    }
}
