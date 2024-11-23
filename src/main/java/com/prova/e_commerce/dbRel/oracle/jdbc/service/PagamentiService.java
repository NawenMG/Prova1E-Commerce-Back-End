package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.PagamentiRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagamentiService {

    @Autowired
    private PagamentiRep pagamentiRep;

    /**
     * Metodo per eseguire una query avanzata sui pagamenti in base a parametri dinamici.
     * Utilizza Caffeine per 10 minuti e Redis per 30 minuti.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #pagamenti.toString()")
    public List<Pagamenti> queryPagamenti(ParamQuery paramQuery, Pagamenti pagamenti) {
        return pagamentiRep.query(paramQuery, pagamenti);
    }

    /**
     * Metodo per inserire un nuovo pagamento.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String inserisciPagamento(Pagamenti pagamenti) {
        return pagamentiRep.insertPayment(pagamenti);
    }

    /**
     * Metodo per aggiornare un pagamento esistente in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String aggiornaPagamento(String paymentID, Pagamenti pagamenti) {
        return pagamentiRep.updatePayment(paymentID, pagamenti);
    }

    /**
     * Metodo per eliminare un pagamento in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String eliminaPagamento(String paymentID) {
        return pagamentiRep.deletePayment(paymentID);
    }

    /**
     * Metodo per generare un numero specificato di pagamenti con dati casuali.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String salvaPagamentiCasuali(int numero) {
        return pagamentiRep.saveAll(numero);
    }
}
