package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.PagamentiRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagamentiService {
    @Autowired
    private PagamentiRep pagamentiRep;

    /**
     * Metodo per eseguire una query avanzata sui pagamenti in base a parametri dinamici.
     */
    public List<Pagamenti> queryPagamenti(ParamQuery paramQuery, Pagamenti pagamenti) {
        return pagamentiRep.query(paramQuery, pagamenti);
    }

    /**
     * Metodo per inserire un nuovo pagamento.
     */
    public String inserisciPagamento(Pagamenti pagamenti) {
        return pagamentiRep.insertPayment(pagamenti);
    }

    /**
     * Metodo per aggiornare un pagamento esistente in base all'ID.
     */
    public String aggiornaPagamento(int paymentID, Pagamenti pagamenti) {
        return pagamentiRep.updatePayment(paymentID, pagamenti);
    }

    /**
     * Metodo per eliminare un pagamento in base all'ID.
     */
    public String eliminaPagamento(int paymentID) {
        return pagamentiRep.deletePayment(paymentID);
    }

    /**
     * Metodo per generare un numero specificato di pagamenti con dati casuali.
     */
    public String salvaPagamentiCasuali(int numero) {
        return pagamentiRep.saveAll(numero);
    }
}
