package br.com.santander.application.commands;

import br.com.santander.domain.entities.Book;
import br.com.santander.domain.gateways.BookStoreRepository;
import br.com.santander.domain.utils.CSVReaderUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class KaggleDataImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(KaggleDataImporter.class);
    private final BookStoreRepository bookStoreRepository;

    @Inject
    public KaggleDataImporter(final BookStoreRepository bookStoreRepository) {
        this.bookStoreRepository = bookStoreRepository;
    }

    public void importData(String filePath) throws IOException {
        LOGGER.info("Starting Kaggle data import from file: {}", filePath);

        List<Book> bookDTOs = CSVReaderUtil.readBooksFromCSV(filePath);

        bookDTOs.stream()
                .map(book -> new Book(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getGenre(),
                        book.getDescription()
                ))
                .forEach(bookStoreRepository::save);

        LOGGER.info("Successfully imported {} books from Kaggle dataset.", bookDTOs.size());
    }
}
