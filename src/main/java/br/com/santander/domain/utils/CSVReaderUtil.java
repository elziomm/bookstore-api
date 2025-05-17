package br.com.santander.domain.utils;

import br.com.santander.domain.entities.Book;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CSVReaderUtil {

    private CSVReaderUtil() {
        // Constructor
    }

    public static List<Book> readBooksFromCSV(String filePath) throws IOException {
        List<Book> books = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length >= 5) {
                    Book bookDTO = new Book(
                            Long.valueOf(fields[0].trim()),
                            fields[1].trim(),
                            fields[2].trim(),
                            "",
                            ""
                    );
                    books.add(bookDTO);
                }
            }
        }

        return books;
    }
}
