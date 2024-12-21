package com.prova.e_commerce.Config;

import com.arangodb.ArangoDB;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArangodbConfig {

    @Value("${arangodb.host}")
    private String host;

    @Value("${arangodb.port}")
    private int port;

    @Value("${arangodb.username}")
    private String username;

    @Value("${arangodb.password}")
    private String password;

    @Value("${arangodb.database}")
    private String databaseName;

    @Bean
    public ArangoDB arangoDB() {
        ArangoDB arangoDB = new ArangoDB.Builder()
                .host(host, port)
                .user(username)
                .password(password)
                .build();


        //MIGRAZIONE ARANGODB PER LA CREAZIONE DEL DATABASE
        // Verifica se il database esiste e crealo se non esiste
        if (!arangoDB.db().exists()) {
            Boolean db = arangoDB.createDatabase(databaseName);
            System.out.println("Database created!");
        }

        // Verifica se la collezione 'key_value_store' esiste, altrimenti creala
        if (!arangoDB.db(databaseName).collection("key_value_store").exists()) {
            arangoDB.db(databaseName).createCollection("key_value_store");
            System.out.println("Collection 'key_value_store' created!");
        }

        return arangoDB;
    }
}
