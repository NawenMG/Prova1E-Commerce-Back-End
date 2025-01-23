package com.prova.e_commerce.dbG.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Node
public class NodoProdotto {

    @Id
    private Long id;

    @NotNull(message = "ID utente obbligatorio")
    private Long idUtente;

    @NotNull(message = "Il nome del prodotto è obbligatorio")  // Assicurati che il nome non sia nullo
    @Size(min = 3, max = 100, message = "Il nome del prodotto deve avere tra 3 e 100 caratteri")  // Limita la lunghezza del nome
    private String nome;

    // Costruttori
    public NodoProdotto(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
