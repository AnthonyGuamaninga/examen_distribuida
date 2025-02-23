package com.programacion.distribuida.books.rest;

import com.programacion.distribuida.books.clients.AuthorRestClient;
import com.programacion.distribuida.books.db.Book;
import com.programacion.distribuida.books.dto.BookDto;
import com.programacion.distribuida.books.repo.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/books")
@Tag(name = "Books", description = "Gestiona información sobre books")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
@Transactional
public class BookRest {

    @Inject
    BookRepository repository;

    @Inject
    @RestClient
    AuthorRestClient authorRest;

    @GET
    @Operation(
            summary = "Obtener todos los libros",
            description = "Devuelve una lista de libros disponibles"
    )
    @APIResponse(
            responseCode = "200",
            description = "Lista de libros obtenida correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookDto.class))
    )
    public List<BookDto> findAll(){
        // version 4 ---------------------------
        return  repository.streamAll()
                .map( book -> {
                    System.out.println("Buscando author con id: "+book.getAuthorId());
                    var author = authorRest.findById(book.getAuthorId());

                    var bookDto = new BookDto();

                    bookDto.setId(book.getId());
                    bookDto.setTitle(book.getTitle());
                    bookDto.setIsbn(book.getIsbn());
                    bookDto.setPrice(book.getPrice());
                    bookDto.setAuthorName(author.getFirstName()+ " " + author.getLastName());
                    return bookDto;
                })
                .toList();
        
    }

    @GET
    @Operation(
            summary = "Obtener un libro por su id (identidicador)",
            description = "Se realiza la busqueda de un Book por su id"
    )
    @APIResponse(
            responseCode = "200",
            description = "Busqueda por id obtenida correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookDto.class))
    )
    @Path("/{id}")
    public Response findById(@PathParam("id") Integer id){
        var obj = repository.findByIdOptional(id);

        if(obj.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var book = obj.get();
        var author = authorRest.findById(book.getAuthorId());

        var dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPrice(book.getPrice());
        dto.setAuthorName(author.getFirstName()+ " " + author.getLastName());

        return Response.ok(dto).build();
    }

    @POST
    @Operation(
            summary = "Registrar un libro",
            description = "Se envia un json con los atributos que posee book"
    )
    @APIResponse(
            responseCode = "200",
            description = "Book registrado correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class))
    )
    public Response create(Book book){
        repository.persist(book);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Operation(
            summary = "Actulizar book",
            description = "Se envía un objeto es formato json para actualizar a un book ya existente a través de su id"
    )
    @APIResponse(
            responseCode = "200",
            description = "Book actualizado satisfactoria",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class))
    )
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, Book book){
        var obj = repository.update(id, book);

        if(obj.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(obj).build();
    }

    @DELETE
    @Operation(
            summary = "Elimina un book",
            description = "Se envia por el path el id del book que se desea eliminar"
    )
    @APIResponse(
            responseCode = "200",
            description = "Book eliminado de forma satisfactoria",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class))
    )
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id){
        var obj = repository.deleteById(id);
        if(!obj){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).build();
    }
}
