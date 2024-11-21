package com.prova.e_commerce.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class LocalDataSourceConfig {

    // Configurazione del DataSource per il database locale (utilizzando JDBC)
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.local")
    public DataSource dataSourceLocal() {
        return DataSourceBuilder.create().build(); // Crea il DataSource con le proprietà di configurazione
    }

    // Configurazione del TransactionManager per gestire le transazioni tramite JDBC
    @Bean
    @Primary
    public PlatformTransactionManager transactionManagerLocal(DataSource dataSourceLocal) {
        return new DataSourceTransactionManager(dataSourceLocal); // Usa DataSourceTransactionManager per JDBC
    }
}
