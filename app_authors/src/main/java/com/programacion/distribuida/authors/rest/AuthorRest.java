package com.programacion.distribuida.authors.rest;

import com.programacion.distribuida.authors.db.Author;
import com.programacion.distribuida.authors.repo.AuthorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/authors")
@Tag(name = "Authors", description = "Gestiona información sobre authors")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
public class AuthorRest {

    @Inject
    private AuthorRepository repository;

    @Inject
    @ConfigProperty(name = "server.port", defaultValue = "8080") // Cambia "quarkus.http.port" por "server.port"
    Integer port;

    AtomicInteger counter = new AtomicInteger(1);

    @GET
    @Operation(
            summary = "Obtener un autor por su id (identificador)",
            description = "Se realiza la búsqueda de un Author por su id"
    )
    @APIResponse(
            responseCode = "200",
            description = "Author obtenido correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Author.class))
    )
    @Path("/{id}")
    public Response findById(@PathParam("id") Integer id) throws UnknownHostException {
        int value = counter.getAndIncrement();
        if (value % 5 != 0) {
            String msg = String.format("Intento %d ==> error", value);
            System.out.println("*********** " + msg);
            throw new RuntimeException(msg);
        }

        System.out.printf("%s: Server %d\n", LocalDateTime.now(), port);

        Author author = repository.findBy(id);
        if (author == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        String txt = String.format("[%s:%d]-%s", ipAddress, port, author.getFirstName());

        Author responseAuthor = new Author();
        responseAuthor.setId(author.getId());
        responseAuthor.setFirstName(txt);
        responseAuthor.setLastName(author.getLastName());

        return Response.ok(responseAuthor).build();
    }

    @GET
    @Operation(
            summary = "Obtener todos los autores",
            description = "Devuelve una lista de autores disponibles"
    )
    @APIResponse(
            responseCode = "200",
            description = "Lista de autores obtenida correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Author.class))
    )
    public List<Author> findAll() {
        return repository.findAll();
    }

    @POST
    @Operation(
            summary = "Registrar un autor",
            description = "Se envía un JSON con los atributos que posee el author"
    )
    @APIResponse(
            responseCode = "200",
            description = "Registro de autor realizado correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Author.class))
    )
    public Response create(Author author) {
        repository.save(author);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Operation(
            summary = "Actualizar autor",
            description = "Se envía un objeto en formato JSON para actualizar a un author ya existente a través de su id"
    )
    @APIResponse(
            responseCode = "200",
            description = "Actualización de author satisfactoria",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Author.class))
    )
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, Author author) {
        Author existingAuthor = repository.findBy(id);
        if (existingAuthor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        //Author existingAuthor = optionalAuthor.get();
        existingAuthor.setFirstName(author.getFirstName());
        existingAuthor.setLastName(author.getLastName());
        repository.save(existingAuthor);

        return Response.ok(existingAuthor).build();
    }

    @DELETE
    @Operation(
            summary = "Eliminar un author",
            description = "Se envía por el path el id del author que se desea eliminar"
    )
    @APIResponse(
            responseCode = "200",
            description = "Author eliminado de forma satisfactoria",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Author.class))
    )
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) {
        Author author = repository.findBy(id);
        if (author == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        repository.remove(author);
        return Response.status(Response.Status.OK).build();
    }
}