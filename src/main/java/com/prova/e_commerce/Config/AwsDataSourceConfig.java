/* package com.prova.e_commerce.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.prova.e_commerce.dbRel.awsRds.repository", 
    entityManagerFactoryRef = "entityManagerFactoryAws",
    transactionManagerRef = "transactionManagerAws"
)
public class AwsDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "aws.datasource")
    @Qualifier("dataSourceAws")
    public DataSource dataSourceAws() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryAws(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSourceAws") DataSource dataSource) {  
        return builder
                .dataSource(dataSource)
                .packages("com.prova.e_commerce.dbRel.awsRds.entity") 
                .persistenceUnit("aws")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManagerAws(
            @Qualifier("entityManagerFactoryAws") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    // Configurazione Flyway per AWS RDS
    @Bean
    public Flyway flywayRemote(@Qualifier("dataSourceAws") DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration/oracle/aws")  // Definisci dove si trovano le migrazioni
                .baselineOnMigrate(true)  // Imposta baseline se non esistono migrazioni
                .load();
    }

    // Metodo per inizializzare le migrazioni Flyway
    @Bean
    public FlywayMigrationInitializer flywayMigrationInitializerAws(@Qualifier("flywayRemote") Flyway flyway) {
        return new FlywayMigrationInitializer(flyway);
    }
} */