package br.com.santander.infrastructure.repository;

import br.com.santander.domain.entities.Book;
import br.com.santander.domain.gateways.BookStoreRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.conversions.Bson;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BookStoreDBRepository implements BookStoreRepository {
    private static final String DATABASE_NAME = "bookStore";
    private static final String COLLECTION_NAME = "books";
    private final MongoClient mongoClient;

    @Inject
    public BookStoreDBRepository(final MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void save(Book book) {
        Book persistedBook = findById(Math.toIntExact(book.getId()));

        if (persistedBook == null) {
            getCollection().insertOne(book);
        }
    }

    @Override
    public List<Book> findByGenre(String genre) {
        Bson filter = Filters.and(
                Filters.regex(
                        "genre",
                        ".*" + sanitizeInput(genre) + ".*",
                        "i"
                )
        );

        return getCollection().find(filter).into(new ArrayList<>());
    }

    @Override
    public List<Book> findByAuthor(String author) {
        Bson filter = Filters.and(
                Filters.regex(
                        "author",
                        ".*" + sanitizeInput(author) + ".*",
                        "i"
                )
        );

        return getCollection().find(filter).into(new ArrayList<>());
    }

    @Override
    public List<Book> findAll() {
        return getCollection().find().into(new ArrayList<>());
    }

    @Override
    public Book findById(Integer id) {
        Bson filter = Filters.and(Filters.eq("_id", id));

        return getCollection().find(filter).first();
    }

    private MongoCollection<Book> getCollection() {
        return this.mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME, Book.class);
    }

    private String sanitizeInput(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("[^\\p{ASCII}]", "").toLowerCase();
    }
}
