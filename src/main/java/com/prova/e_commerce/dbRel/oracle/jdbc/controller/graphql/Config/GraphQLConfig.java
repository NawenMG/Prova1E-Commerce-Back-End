package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql.CategorieControllerGraphql;
import com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql.OrdiniControllerGraphql;
import com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql.PagamentiControllerGraphql;
import com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql.ProdottiControllerGraphql;
import com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql.ResiControllerGraphql;
import com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql.UsersControllerGraphql;

import graphql.schema.idl.TypeRuntimeWiring;

@Configuration
public class GraphQLConfig {

    @Autowired
    private CategorieControllerGraphql categorieController;
    @Autowired
    private OrdiniControllerGraphql ordiniController;
    @Autowired
    private PagamentiControllerGraphql pagamentiController;
    @Autowired
    private ProdottiControllerGraphql prodottiController;
    @Autowired
    private ResiControllerGraphql resiController;
    @Autowired
    private UsersControllerGraphql usersController;

    // Definisce la configurazione del wiring tra lo schema GraphQL e i metodi dei controller
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> {
            wiringBuilder
                // Configura la query principale
                .type(TypeRuntimeWiring.newTypeWiring("Query")
                    .dataFetcher("categorie", categorieController::categorie) // Funzione per la query "categorie"
                    .dataFetcher("ordini", ordiniController::ordini)           // Funzione per la query "ordini"
                    .dataFetcher("pagamenti", pagamentiController::pagamenti)   // Funzione per la query "pagamenti"
                    .dataFetcher("prodotti", prodottiController::prodotti)     // Funzione per la query "prodotti"
                    .dataFetcher("resi", resiController::resi)                 // Funzione per la query "resi"
                    .dataFetcher("users", usersController::users));            // Funzione per la query "users"
        };
    }
}
