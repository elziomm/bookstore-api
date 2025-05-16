package br.com.santander.domain.gateways;

import br.com.santander.domain.entities.Book;

import java.util.List;

public interface BookStoreRepository {
    void save(Book book);

    List<Book> findByGenre(String genre);

    List<Book> findByAuthor(String author);

    List<Book> findAll();
}
