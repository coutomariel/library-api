package com.coutomariel.libraryapi.domain.service;

import com.coutomariel.libraryapi.api.dto.BookUpdateDto;
import com.coutomariel.libraryapi.domain.model.Book;
import com.coutomariel.libraryapi.domain.repository.BookRepository;
import com.coutomariel.libraryapi.exception.BussinessException;
import com.coutomariel.libraryapi.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;


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
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Resource not found exception."));
    }

    @Override
    public void delete(Long id) {
        if(!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found exception.");
        }
        bookRepository.deleteById(id);
    }

    @Override
    public Book update(Long id, BookUpdateDto bookUpdateDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found exception."));
        return bookRepository.save(bookUpdateDto.toEntity(book));
    }
}
