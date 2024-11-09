package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Resi;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.ResiRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResiService {
    @Autowired
    private ResiRep resiRep;

    /**
     * Esegue una query personalizzata sui resi in base ai parametri specificati.
     */
    public List<Resi> queryResi(ParamQuery paramQuery, Resi resi) {
        return resiRep.query(paramQuery, resi);
    }

    /**
     * Inserisce un nuovo reso nel database.
     */
    public String inserisciReso(Resi resi) {
        return resiRep.insertReturn(resi);
    }

    /**
     * Aggiorna i dati di un reso specifico identificato dall'ID.
     */
    public String aggiornaReso(int returnID, Resi resi) {
        return resiRep.updateReturn(returnID, resi);
    }

    /**
     * Elimina un reso specifico dal database in base all'ID.
     */
    public String eliminaReso(int returnID) {
        return resiRep.deleteReturn(returnID);
    }

    /**
     * Genera un numero specificato di resi con dati casuali e li salva nel database.
     */
    public String salvaResiCasuali(int numero) {
        return resiRep.saveAll(numero);
    }
}
