package com.programacion.distribuida.books.clients;

import com.programacion.distribuida.books.dto.AuthorDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
//@RegisterRestClient(baseUri = "http://localhost:9090")
//@RegisterRestClient(configKey = "authors-api")
@RegisterRestClient(baseUri = "stork://authors-api")
public interface AuthorRestClient {

    @GET
    @Path("/{id}")
    @Retry(maxRetries = 3)
    @Fallback(fallbackMethod = "findByIdFallback")
    AuthorDto findById(@PathParam("id") Integer id);

    default AuthorDto findByIdFallback(Integer id) {
        var dto = new AuthorDto();
        dto.setId(-1);
        dto.setFirstName("noname");
        dto.setLastName("");

        return dto;
    }

}
