package com.prova.e_commerce.dbKey.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import com.prova.e_commerce.dbKey.model.SottoClassi.Prodotto;

public class WishList {

    @NotNull(message = "L'ID utente è obbligatorio")
    @Size(min = 3, max = 100, message = "L'ID utente deve essere tra 3 e 100 caratteri")
    private String _key;  // La chiave del documento in ArangoDB (equivalente a DynamoDB Partition Key)

    @NotNull(message = "La lista dei prodotti non può essere nulla")
    private List<Prodotto> products;

    // Metodo per ottenere la chiave del documento in ArangoDB
    public String getKey() {
        return _key;
    }

    // Metodo per impostare la chiave del documento in ArangoDB
    public void setKey(String key) {
        this._key = key;
    }

    // Getter e setter per la lista dei prodotti
    public List<Prodotto> getProducts() {
        return products;
    }

    public void setProducts(List<Prodotto> products) {
        this.products = products;
    }
}
