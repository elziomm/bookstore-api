package br.com.santander.infrastructure.http;

import br.com.santander.domain.entities.Book;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.lang.String.format;

@ApplicationScoped
public class OpenLibraryClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenLibraryClient.class);
    private static final String SEARCH_BOOK_QUERY = "%s/search.json?%s&fields=title,author_name&limit=10";
    private static final String AUTHOR_NAME = "author_name";
    private static final String TITLE = "title";

    @ConfigProperty(name = "services.openlibrary.api.url")
    public String baseUrl;
    private final Gson gson;
    private final Client client;
    private final Random random;

    @Inject
    GPT4Client gpt4Client;

    public OpenLibraryClient() {
        this.random = new Random();
        this.gson = new Gson();
        this.client = ClientBuilder.newClient();
    }

    public List<Book> fetchBooksByGenre(String genre) {
        List<Book> books = fetchBooks("subject=" + genre);
        return completionInfo(books);
    }

    public List<Book> fetchBooksByAuthor(String author) {
        List<Book> books = fetchBooks("author=" + author);
        return completionInfo(books);
    }

    private List<Book> completionInfo(List<Book> books) {
        for (Book book : books) {
            if (book.getGenre().isEmpty() || book.getDescription().isEmpty()) {
                String prompt = buildPromptForBook(book);
                Optional<String> generatedText = gpt4Client.generateText(prompt);

                generatedText.ifPresent(text -> {
                    if (book.getGenre().isEmpty()) {
                        book.setGenre(text.split("\\.")[0]);
                    }
                    if (book.getDescription().isEmpty()) {
                        book.setDescription(text);
                    }
                });
            }
        }
        return books;
    }

    private List<Book> fetchBooks(String queryParam) {
        try {
            LOGGER.info("Fetching books with query: {}", queryParam);

            Response response = client
                    .target(format(SEARCH_BOOK_QUERY, baseUrl, queryParam))
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            return parseBooksFromResponse(response.readEntity(String.class));
        } catch (Exception e) {
            LOGGER.error("Error fetching books: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Book> parseBooksFromResponse(String jsonResponse) {
        List<Book> books = new ArrayList<>();

        try {
            JsonObject rootObject = gson.fromJson(jsonResponse, JsonObject.class);
            JsonArray docsArray = rootObject.getAsJsonArray("docs");

            for (JsonElement element : docsArray) {
                JsonObject bookObject = element.getAsJsonObject();
                books.add(parseBook(bookObject));
            }
        } catch (Exception e) {
            LOGGER.error("Failed to parse books from response: {}", e.getMessage());
        }

        return books;
    }

    private Book parseBook(JsonObject bookObject) {
        Long id = (long) random.nextInt(1000);
        String title = getString(bookObject);
        String author = getFirstString(bookObject);
        return new Book(id, title, author, "", "");
    }

    private String getString(JsonObject jsonObject) {
        return jsonObject.has(TITLE) ? jsonObject.get(TITLE).getAsString() : "";
    }

    private String getFirstString(JsonObject jsonObject) {
        if (jsonObject.has(AUTHOR_NAME) && jsonObject.get(AUTHOR_NAME).isJsonArray()) {
            JsonArray array = jsonObject.getAsJsonArray(AUTHOR_NAME);
            return !array.isEmpty() ? array.get(0).getAsString() : "";
        }
        return "";
    }

    private String buildPromptForBook(Book book) {
        return "Generate a genre and description for a book titled '" + book.getTitle() + "' by " + book.getAuthor() + ".";
    }
}
