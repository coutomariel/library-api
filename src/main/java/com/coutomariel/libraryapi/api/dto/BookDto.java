package com.coutomariel.libraryapi.api.dto;

import com.coutomariel.libraryapi.domain.model.Book;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String author;
    @NotEmpty
    private String isbn;

    public static BookDto from(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .build();
    }
}
