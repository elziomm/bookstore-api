package br.com.santander.application.commands;

import br.com.santander.domain.entities.Book;
import br.com.santander.domain.gateways.BookStoreRepository;
import br.com.santander.domain.utils.CSVReaderUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class KaggleDataImporter {

    private final BookStoreRepository bookStoreRepository;

    @Inject
    public KaggleDataImporter(final BookStoreRepository bookStoreRepository) {
        this.bookStoreRepository = bookStoreRepository;
    }

    public void importData(String filePath) {
        System.out.println("Starting Kaggle data import from file: " + filePath);

        try {
            List<Book> bookDTOs = CSVReaderUtil.readBooksFromCSV(filePath);

            bookDTOs.stream()
                    .map(book -> new Book(book.getTitle(), book.getAuthor(), book.getGenre(), book.getDescription()))
                    .forEach(bookStoreRepository::save);

            System.out.println("Successfully imported " + bookDTOs.size() + " books from Kaggle dataset.");
        } catch (IOException e) {
            System.err.println("Error importing Kaggle data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
