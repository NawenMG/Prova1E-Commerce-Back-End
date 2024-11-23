package com.prova.e_commerce.dbG.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Node
public class NodoCategoriaProdotto {

    @Id
    private Long id;

    @NotNull(message = "Il nome della categoria prodotto è obbligatorio")  // Validazione che il nome non sia nullo
    @Size(min = 3, max = 100, message = "Il nome della categoria prodotto deve avere tra 3 e 100 caratteri") // Validazione della lunghezza del nome
    private String nome;

    // Costruttori, getter e setter
    public NodoCategoriaProdotto(String nome) {
        this.nome = nome;
    }

    public Long id() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
