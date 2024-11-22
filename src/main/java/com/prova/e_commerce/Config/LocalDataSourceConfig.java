package com.prova.e_commerce.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.flywaydb.core.Flyway;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;

@Configuration
@EnableTransactionManagement
public class LocalDataSourceConfig {

    @Bean
    @Qualifier("dataSourceLocal")
    @ConfigurationProperties(prefix = "spring.datasource.local")
    public DataSource dataSourceLocal() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("transactionManagerLocal")
    public PlatformTransactionManager transactionManagerLocal(@Qualifier("dataSourceLocal") DataSource dataSourceLocal) {
        return new DataSourceTransactionManager(dataSourceLocal);
    }

    @Bean
    @Qualifier("jdbcTemplateLocal")
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSourceLocal") DataSource dataSourceLocal) {
        return new JdbcTemplate(dataSourceLocal);
    }

    @Bean
    public Flyway flywayLocal(@Qualifier("dataSourceLocal") DataSource dataSourceLocal) {
        return Flyway.configure()
                     .dataSource(dataSourceLocal)
                     .locations("classpath:db/migration/oracle/locale")
                     .baselineOnMigrate(true)
                     .load();
    }

    // Metodo separato per eseguire le migrazioni
    @Bean
    public FlywayMigrationInitializer flywayMigrationInitializerLocal(@Qualifier("flywayLocal") Flyway flyway) {
        return new FlywayMigrationInitializer(flyway);
    }
}
