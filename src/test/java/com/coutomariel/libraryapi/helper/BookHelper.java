package com.coutomariel.libraryapi.helper;

import com.coutomariel.libraryapi.api.dto.BookDto;
import com.coutomariel.libraryapi.domain.model.Book;

public class BookHelper {

    public static Book createMockValidBook() {
        return Book.builder().id(1L).name("Solid").author("Maurcio Aniche").isbn("zpto").build();
    }

    public static Book createMockValidBookToPersist() {
        return Book.builder().name("Solid").author("Maurcio Aniche").isbn("zpto").build();
    }

    public static BookDto createMockValidBookDto() {
        return BookDto.builder().name("Solid").author("Maurcio Aniche").isbn("zpto").build();
    }

    public static BookDto createMockInvalidBook() {
        return BookDto.builder().name("").author("").isbn("").build();
    }
}
