package com.programacion.distribuida.authors.config;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;

import javax.sql.DataSource;

@ApplicationScoped
public class FlywayConfig {

    @Inject
    private DataSource dataSource;

    @PostConstruct
    public void migrate() {
        FluentConfiguration config = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration");

        Flyway flyway = new Flyway(config);
        flyway.migrate();
    }
}