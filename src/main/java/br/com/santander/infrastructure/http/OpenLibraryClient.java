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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OpenLibraryClient {

    private static final String BASE_URL = "https://openlibrary.org";
    private final Gson gson;
    private final Client client;

    @Inject
    GPT4Client gpt4Client;

    public OpenLibraryClient() {
        this.gson = new Gson();
        this.client = ClientBuilder.newClient();
    }

    public List<Book> fetchBooksByGenre(String genre) {
        return fetchBooks("subject=" + genre);
    }

    public List<Book> fetchBooksByAuthor(String author) {
        List<Book> books = fetchBooks("author=" + author);

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
        String url = BASE_URL + "/search.json?" + queryParam;

        Response response = client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return parseBooksFromResponse(response.readEntity(String.class));
        } else {
            System.err.println("Failed to fetch books: " + response.getStatus());
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
            System.err.println("Failed to parse books from response: " + e.getMessage());
        }

        return books;
    }

    private Book parseBook(JsonObject bookObject) {
        String title = getString(bookObject, "title");
        String author = getFirstString(bookObject, "author_name");
        String genre = getFirstString(bookObject, "subject");
        String description = getStringFromJsonObject(bookObject, "first_sentence", "value");
        return new Book(title, author, genre, description);
    }

    private String getString(JsonObject jsonObject, String key) {
        return jsonObject.has(key) ? jsonObject.get(key).getAsString() : "";
    }

    private String getFirstString(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && jsonObject.get(key).isJsonArray()) {
            JsonArray array = jsonObject.getAsJsonArray(key);
            return !array.isEmpty() ? array.get(0).getAsString() : "";
        }
        return "";
    }

    private String getStringFromJsonObject(JsonObject jsonObject, String key, String subKey) {
        if (jsonObject.has(key) && jsonObject.get(key).isJsonObject()) {
            JsonObject subObject = jsonObject.getAsJsonObject(key);
            return subObject.has(subKey) ? subObject.get(subKey).getAsString() : "";
        }
        return "";
    }

    private String buildPromptForBook(Book book) {
        return "Generate a genre and description for a book titled '" + book.getTitle() + "' by " + book.getAuthor() + ".";
    }
}
