package com.programacion.distribuida.authors.config;

import io.helidon.config.Config;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class JpaConfig {
    EntityManagerFactory emf;

//    @PostConstruct
//    public void init() {
//        emf = Persistence.createEntityManagerFactory("pu-distribuida");
//    }
//
//    @Produces
//    public EntityManager em() {
//        return emf.createEntityManager();
//    }

    @PostConstruct
    public void init() {
        Config config = Config.global();

        Map<String, String> properties = new HashMap<>();
//        properties.put("jakarta.persistence.jdbc.driver", config.get("jdbc.driver").asString().get());
        properties.put("jakarta.persistence.jdbc.url", config.get("jdbc.url").asString().get());
        properties.put("jakarta.persistence.jdbc.user", config.get("jdbc.user").asString().get());
        properties.put("jakarta.persistence.jdbc.password", config.get("jdbc.password").asString().get());

        // Crea el EntityManagerFactory con las propiedades externalizadas
        emf = Persistence.createEntityManagerFactory("pu-distribuida", properties);
    }

    @Produces
    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

}
