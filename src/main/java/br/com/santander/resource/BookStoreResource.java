package br.com.santander.resource;


import br.com.santander.application.commands.BookCommand;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookStoreResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookStoreResource.class);
    private final BookCommand bookCommand;

    @Inject
    public BookStoreResource(final BookCommand bookCommand) {
        this.bookCommand = bookCommand;
    }

    @GET
    @Path("/books")
    public Response getAllBooks() {
        try {
            LOGGER.info("Getting all books");
            return Response
                    .ok(bookCommand.getAllBooks())
                    .build();
        } catch (Exception exception) {
            LOGGER.error("Error getting all books", exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/books/{id}")
    public Response getBookById(@PathParam("id") Integer id) {
        try {
            LOGGER.info("Getting book by ID: {}", id);
            return Response
                    .ok(bookCommand.getBookById(id))
                    .build();
        } catch (Exception exception) {
            LOGGER.error("Error getting book by ID: {}", id, exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/books/genre/{genre}")
    public Response getBooksByGenre(@PathParam("genre") String genre) {
        try {
            LOGGER.info("Getting books by genre: {}", genre);
            return Response
                    .ok(bookCommand.getBooksByGenre(genre))
                    .build();
        } catch (Exception exception) {
            LOGGER.error("Error getting books by genre: {}", genre, exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/books/author/{author}")
    public Response getBooksByAuthor(@PathParam("author") String author) {
        try {
            LOGGER.info("Getting books by author: {}", author);
            return Response
                    .ok(bookCommand.getBooksByAuthor(author))
                    .build();
        } catch (Exception exception) {
            LOGGER.error("Error getting books by author: {}", author, exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
