package com.prova.e_commerce.Config;

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

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.example.repository.aws", // pacchetto repository per il database AWS
    entityManagerFactoryRef = "entityManagerFactoryAws",
    transactionManagerRef = "transactionManagerAws"
)
public class AwsDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.aws")
    public DataSource dataSourceAws() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryAws(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSourceAws") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.model.aws") // pacchetto contenente le entità del database AWS
                .persistenceUnit("aws")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManagerAws(
            @Qualifier("entityManagerFactoryAws") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
