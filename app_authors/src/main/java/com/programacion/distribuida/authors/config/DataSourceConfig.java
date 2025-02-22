package com.programacion.distribuida.authors.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

@ApplicationScoped
public class DataSourceConfig {

    @Produces
    @ApplicationScoped
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:postgresql://localhost:5432/distribuida");
        ds.setUsername("postgres");
        ds.setPassword("postgres");
        ds.setDriverClassName("org.postgresql.Driver");
        return ds;
    }
}