package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql.Config;

import com.coxautodev.graphql.tools.SchemaParser;
import com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql.CategorieControllerGraphql;
import com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql.OrdiniControllerGraphql;
import com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql.PagamentiControllerGraphql;
import com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql.ProdottiControllerGraphql;
import com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql.ResiControllerGraphql;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLConfig {

    @Bean
    public GraphQL graphQL() {
        // Unire più schemi e resolver
        GraphQLSchema graphQLSchema = SchemaParser.newParser()
                .file("graphql/schema/Categorie.graphqls")  // Schema per le categorie
                .file("graphql/schema/Ordini.graphqls")   // Schema per gli ordini
                .file("graphql/schema/Pagamenti.graphqls")    // Schema per i pagamenti
                .file("graphql/schema/Prodotti.graphqls")  //Schema per i prodotti
                .file("graphql/schema/Resi.graphqls")  //Schema per i resi
                .file("graphql/schema/Users.graphqls") //Schema per Users
                .resolvers(new CategorieControllerGraphql())  // Resolver per categorie
                .resolvers(new OrdiniControllerGraphql())   // Resolver per ordini
                .resolvers(new PagamentiControllerGraphql()) // Resolver per pagamenti
                .resolvers(new ProdottiControllerGraphql()) // Resolver per prodotti
                .resolvers(new ResiControllerGraphql()) //Resolver per resi
                .resolvers(new UsersControllerGraphql()) //Resolver per users
                .build()
                .makeExecutableSchema();

        return GraphQL.newGraphQL(graphQLSchema).build();
    }
}
