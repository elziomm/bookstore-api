package br.com.santander.application.commands;

import br.com.santander.domain.entities.Book;
import br.com.santander.domain.gateways.BookStoreRepository;
import br.com.santander.infrastructure.http.OpenLibraryClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class BookCommand {

    private final BookStoreRepository bookStoreRepository;
    private final OpenLibraryClient openLibraryClient;

    @Inject
    public BookCommand(final BookStoreRepository bookStoreRepository,
                       final OpenLibraryClient openLibraryClient) {
        this.bookStoreRepository = bookStoreRepository;
        this.openLibraryClient = openLibraryClient;
    }

    public List<Book> getBooksByGenre(String genre) {
        List<Book> books = bookStoreRepository.findByGenre(genre);

        if (books.isEmpty()) {
            books = openLibraryClient.fetchBooksByGenre(genre).stream()
                    .map(book -> new Book(book.getTitle(), book.getAuthor(), book.getGenre(), book.getDescription()))
                    .toList();
            books.forEach(bookStoreRepository::save);
        }

        return books;
    }

    public List<Book> getBooksByAuthor(String author) {
        List<Book> books = bookStoreRepository.findByAuthor(author);

        if (books.isEmpty()) {
            books = openLibraryClient.fetchBooksByAuthor(author).stream()
                    .map(book -> new Book(book.getTitle(), book.getAuthor(), book.getGenre(), book.getDescription()))
                    .toList();
            books.forEach(bookStoreRepository::save);
        }

        return books;
    }

    public List<Book> getAllBooks() {
        return bookStoreRepository.findAll();
    }
}
