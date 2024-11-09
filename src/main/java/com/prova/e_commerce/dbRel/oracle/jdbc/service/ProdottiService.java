package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Prodotti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.ProdottiRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdottiService {
    @Autowired
    private ProdottiRep prodottiRep;

    /**
     * Esegue una query personalizzata sui prodotti in base ai parametri specificati.
     */
    public List<Prodotti> queryProdotti(ParamQuery paramQuery, Prodotti prodotti) {
        return prodottiRep.query(paramQuery, prodotti);
    }

    /**
     * Inserisce un nuovo prodotto nel database.
     */
    public String inserisciProdotto(Prodotti prodotti) {
        return prodottiRep.insertProduct(prodotti);
    }

    /**
     * Aggiorna i dati di un prodotto specifico identificato dall'ID.
     */
    public String aggiornaProdotto(int productID, Prodotti prodotti) {
        return prodottiRep.updateProduct(productID, prodotti);
    }

    /**
     * Elimina un prodotto specifico dal database in base all'ID.
     */
    public String eliminaProdotto(int productID) {
        return prodottiRep.deleteProduct(productID);
    }

    /**
     * Genera un numero specificato di prodotti con dati casuali e li salva nel database.
     */
    public String salvaProdottiCasuali(int numero) {
        return prodottiRep.saveAll(numero);
    }
}
