/* package com.prova.e_commerce.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.prova.e_commerce.repository.oracle", // Il pacchetto dei repository per Oracle
    entityManagerFactoryRef = "oracleEntityManagerFactory",
    transactionManagerRef = "oracleTransactionManager"
)
public class OracleConfig {

    // Configurazione del DataSource per Oracle
    @Bean(name = "oracleDataSource")
    public DataSource oracleDataSource() {
        return DataSourceBuilder.create()
                .url("${spring.datasource.oracle.url}")
                .username("${spring.datasource.oracle.username}")
                .password("${spring.datasource.oracle.password}")
                .driverClassName("${spring.datasource.oracle.driver-class-name}")
                .build();
    }

    // Configurazione dell'EntityManagerFactory per JPA (per Oracle)
    @Bean(name = "oracleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(@Autowired DataSource oracleDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(oracleDataSource);
        em.setPackagesToScan("com.prova.e_commerce.model.oracle"); // Pacchetto delle entità per Oracle
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Configurazioni Hibernate specifiche per Oracle
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect"); // Usa il dialect appropriato per la tua versione di Oracle
        em.setJpaPropertyMap(properties);

        return em;
    }

    // Configurazione del JpaTransactionManager per la gestione delle transazioni
    @Bean(name = "oracleTransactionManager")
    public JpaTransactionManager oracleTransactionManager(@Autowired LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory) {
        return new JpaTransactionManager(oracleEntityManagerFactory.getObject());
    }

    // Configurazione di JdbcTemplate per Oracle
    @Bean(name = "oracleJdbcTemplate")
    public JdbcTemplate oracleJdbcTemplate(@Autowired DataSource oracleDataSource) {
        return new JdbcTemplate(oracleDataSource);
    }
}
 */