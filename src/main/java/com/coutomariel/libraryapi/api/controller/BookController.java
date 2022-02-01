package com.coutomariel.libraryapi.api.controller;

import com.coutomariel.libraryapi.api.dto.BookDto;
import com.coutomariel.libraryapi.domain.model.Book;
import com.coutomariel.libraryapi.domain.service.BookServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private static Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private BookServiceImpl bookService;

    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody @Valid BookDto bookDto) {
        Book book = Book.builder().name(bookDto.getName()).author(bookDto.getAuthor()).isbn(bookDto.getIsbn()).build();
        Book entity = bookService.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(BookDto.from(entity));
    }

}
