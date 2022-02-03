package com.coutomariel.libraryapi.domain.service;

import com.coutomariel.libraryapi.domain.model.Book;
import com.coutomariel.libraryapi.domain.repository.BookRepository;
import com.coutomariel.libraryapi.exception.BussinessException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BussinessException("Isbn duplicado");
        }
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.of(bookRepository.findById(id)).orElse(Optional.empty());
    }
}
