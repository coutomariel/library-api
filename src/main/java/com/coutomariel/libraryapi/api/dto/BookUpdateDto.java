package com.coutomariel.libraryapi.api.dto;

import com.coutomariel.libraryapi.domain.model.Book;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookUpdateDto {

    private String name;
    private String author;
    private String isbn;

    public Book toEntity(Book book) {
        return Book.builder()
                .id(book.getId()).
                name(this.name != null ? this.name : book.getName()).
                author(this.author != null ? this.author : book.getAuthor()).
                isbn(this.isbn != null ? this.isbn : book.getIsbn()).
                build();
    }
}
