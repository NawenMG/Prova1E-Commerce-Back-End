package com.prova.e_commerce.dbG.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class NodoCategoriaProdotto {

    @Id
    private Long id;


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
