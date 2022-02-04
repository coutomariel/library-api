package com.coutomariel.libraryapi.domain.service;

import com.coutomariel.libraryapi.domain.model.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book book);
    Book findById(Long id);
    void delete(Long id);
}
