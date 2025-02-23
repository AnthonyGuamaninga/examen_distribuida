package com.programacion.distribuida.authors;

import com.google.gson.GsonBuilder;
import com.programacion.distribuida.authors.db.Author;
import com.programacion.distribuida.authors.service.AuthorsService;
import io.helidon.config.Config;
import io.helidon.health.HealthCheckResponse;
import io.helidon.health.HealthCheckType;
import io.helidon.openapi.OpenApiFeature;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import io.helidon.webserver.observe.ObserveFeature;
import io.helidon.webserver.observe.health.HealthObserver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.inject.spi.CDI;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.UUID;

@ApplicationScoped
public class PrincipalAuthorsRest {

    static void handleInsert(ServerRequest req, ServerResponse res) {
        var servicio = CDI.current().select(AuthorsService.class).get();
        var gson = new GsonBuilder().setPrettyPrinting().create();
        String json = req.content().as(String.class);
        Author author = gson.fromJson(json, Author.class);
        var msg = servicio.insertAuthor(author);
        res.send(gson.toJson(msg));
    }

    static void handleUpdate(ServerRequest req, ServerResponse res) {
        var servicio = CDI.current().select(AuthorsService.class).get();
        var gson = new GsonBuilder().setPrettyPrinting().create();
        String json = req.content().as(String.class);
        Author author = gson.fromJson(json, Author.class);
        var msg = servicio.putAuthor(author);
        res.send(gson.toJson(msg));
    }

    static void handleFindById(ServerRequest req, ServerResponse res) {
        var servicio = CDI.current().select(AuthorsService.class).get();
        var gson = new GsonBuilder().setPrettyPrinting().create();
        Integer id = Integer.parseInt( req.path().pathParameters().get("id"));
        var msg = servicio.getById(id);
        res.send(gson.toJson(msg));
    }

    static void handleFindAll(ServerRequest req, ServerResponse res) {
        var servicio = CDI.current().select(AuthorsService.class).get();
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var msg = servicio.getAll();
        res.send(gson.toJson(msg));
    }

    static void handleDelete(ServerRequest req, ServerResponse res) {
        var servicio = CDI.current().select(AuthorsService.class).get();
        var gson = new GsonBuilder().setPrettyPrinting().create();
        String json = req.content().as(String.class);
        Author author = gson.fromJson(json, Author.class);
        var msg = servicio.putAuthor(author);
        res.send(gson.toJson(msg));
    }



    public static void main(String[] args) {
        Config config = Config.create();
        Config.global(config);

        SeContainer container = SeContainerInitializer.newInstance().initialize();

//        Server.builder()
//                .build()
//                .start();

        WebServer.builder()
                .config(config.get("server"))
                .addFeature(OpenApiFeature.create(config.get("openapi")))
                .routing( it -> it
                        .get("/authors", PrincipalAuthorsRest::handleFindAll)
                        .get("/authors/{id}", PrincipalAuthorsRest::handleFindById)
                        .post("/authors", PrincipalAuthorsRest::handleInsert)
                        .put("/authors", PrincipalAuthorsRest::handleUpdate)
                        .delete("/authors", PrincipalAuthorsRest::handleDelete)
                )
                .port(config.get("helidon.port").asInt().get())
                .build()
                .start();

        long serverStartTime = 0;
        ObserveFeature observe = ObserveFeature.builder()
                .config(config.get("server.features.observe"))
                .addObserver(HealthObserver.builder()
                        .useSystemServices(true) // Include Helidon-provided health checks.
                        .addCheck(() -> HealthCheckResponse.builder()
                                        .status(System.currentTimeMillis() - serverStartTime >= 8000)
                                        .detail("time", System.currentTimeMillis())
                                        .build(),
                                HealthCheckType.READINESS,
                                "live-after-8-seconds")
                        .build())
                .build();
    }
}
