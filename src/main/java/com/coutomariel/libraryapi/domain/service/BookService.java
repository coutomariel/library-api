package com.coutomariel.libraryapi.domain.service;

import com.coutomariel.libraryapi.api.dto.BookUpdateDto;
import com.coutomariel.libraryapi.domain.model.Book;

public interface BookService {
    Book save(Book book);
    Book findById(Long id);
    void delete(Long id);
    Book update(Long id, BookUpdateDto request);
}
