package br.com.santander.application.commands;

import br.com.santander.domain.entities.Book;
import br.com.santander.domain.exceptions.ImportingException;
import br.com.santander.domain.gateways.BookStoreRepository;
import br.com.santander.domain.utils.FakeBookDataGenerator;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class BookDataInitializer {

    private static final String FILE_PATH = "src/main/resources/datasets/amazon-books-dataset.csv";
    private static final Logger LOGGER = LoggerFactory.getLogger(BookDataInitializer.class);
    private final BookStoreRepository bookStoreRepository;
    private final KaggleDataImporter kaggleDataImporter;

    public BookDataInitializer(final BookStoreRepository bookStoreRepository,
                               final KaggleDataImporter kaggleDataImporter) {
        this.bookStoreRepository = bookStoreRepository;
        this.kaggleDataImporter = kaggleDataImporter;
    }

    public void onStartup(@Observes StartupEvent event) {
        LOGGER.info("Initializing example book data...");

        try {
            List<Book> bookList = FakeBookDataGenerator.generateFakeBooks(2);
            bookList.forEach(bookStoreRepository::save);

            kaggleDataImporter.importData(FILE_PATH);

            LOGGER.info("Example book data has been successfully initialized.");
        } catch (Exception e) {
            LOGGER.error("Error during book data initialization: {}", e.getMessage());
            throw new ImportingException("Failed to initialize book data: " + e.getMessage());
        }
    }
}
