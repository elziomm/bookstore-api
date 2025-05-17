package br.com.santander.domain.utils;

import br.com.santander.domain.entities.Book;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;

public class FakeBookDataGenerator {

    private FakeBookDataGenerator() {
        // Private constructor to prevent instantiation
    }

    private static final Faker faker = new Faker();

    public static List<Book> generateFakeBooks(int count) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Book book = new Book(
                    faker.number().randomNumber(),
                    faker.book().title(),
                    faker.book().author(),
                    faker.book().genre(),
                    faker.lorem().paragraph()
            );
            books.add(book);
        }
        return books;
    }
}
