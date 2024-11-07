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
    basePackages = "com.prova.e_commerce.repository.redshift", // Il pacchetto dei repository per Redshift
    entityManagerFactoryRef = "redshiftEntityManagerFactory",
    transactionManagerRef = "redshiftTransactionManager"
)
public class RedshiftConfig {

    // Configurazione del DataSource per Redshift
    @Bean(name = "redshiftDataSource")
    public DataSource redshiftDataSource() {
        return DataSourceBuilder.create()
                .url("${spring.datasource.redshift.url}")
                .username("${spring.datasource.redshift.username}")
                .password("${spring.datasource.redshift.password}")
                .driverClassName("${spring.datasource.redshift.driver-class-name}")
                .build();
    }

    // Configurazione dell'EntityManagerFactory per JPA (per Redshift)
    @Bean(name = "redshiftEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean redshiftEntityManagerFactory(@Autowired DataSource redshiftDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(redshiftDataSource);
        em.setPackagesToScan("com.prova.e_commerce.model.redshift"); // Pacchetto delle entità per Redshift
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Configurazioni Hibernate specifiche per Redshift (PostgreSQL Dialect)
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"); // Redshift usa PostgreSQL Dialect
        em.setJpaPropertyMap(properties);

        return em;
    }

    // Configurazione del JpaTransactionManager per la gestione delle transazioni
    @Bean(name = "redshiftTransactionManager")
    public JpaTransactionManager redshiftTransactionManager(@Autowired LocalContainerEntityManagerFactoryBean redshiftEntityManagerFactory) {
        return new JpaTransactionManager(redshiftEntityManagerFactory.getObject());
    }

    // Configurazione di JdbcTemplate per Redshift
    @Bean(name = "redshiftJdbcTemplate")
    public JdbcTemplate redshiftJdbcTemplate(@Autowired DataSource redshiftDataSource) {
        return new JdbcTemplate(redshiftDataSource);
    }
}
 */