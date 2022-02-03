package com.coutomariel.libraryapi.domain.service;

import com.coutomariel.libraryapi.domain.model.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book book);
    Optional<Book> findById(Long id);
}
