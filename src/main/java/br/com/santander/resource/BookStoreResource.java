package br.com.santander.resource;


import br.com.santander.application.commands.BookCommand;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookStoreResource {

    private final BookCommand bookCommand;

    @Inject
    public BookStoreResource(final BookCommand bookCommand) {
        this.bookCommand = bookCommand;
    }

    @GET
    @Path("/books")
    public Response getAllBooks() {
        try {
            return Response
                    .ok(bookCommand.getAllBooks())
                    .build();
        } catch (Exception exception) {
            System.out.println("exception = " + exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/books/genre/{genre}")
    public Response getBooksByGenre(@PathParam("genre") String genre) {
        try {
            return Response
                    .ok(bookCommand.getBooksByGenre(genre))
                    .build();
        } catch (Exception exception) {
            System.out.println("exception = " + exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/books/author/{author}")
    public Response getBooksByAuthor(@PathParam("author") String author) {
        try {
            return Response
                    .ok(bookCommand.getBooksByAuthor(author))
                    .build();
        } catch (Exception exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
