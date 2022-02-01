package com.coutomariel.libraryapi.domain.service;

import com.coutomariel.libraryapi.domain.model.Book;
import com.coutomariel.libraryapi.domain.repository.BookRepository;
import com.coutomariel.libraryapi.helper.BookHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService bookService;

    @MockBean
    BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        this.bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        Book book = Book.builder().name("Clean Code").author("Bob Martin").isbn("xpto").build();
        Book mockBook = Book.builder().id(1L).name("Clean Code").author("Bob Martin").isbn("xpto").build();

        when(bookRepository.save(any(Book.class))).thenReturn(mockBook);

        Book bookSaved = bookService.save(book);

        verify(bookRepository, times(1)).save(any(Book.class));

        assertThat(bookSaved.getId()).isNotNull();
        assertThat(bookSaved.getName()).isEqualTo(book.getName());
        assertThat(bookSaved.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    @DisplayName("Deve lançar bussiness exeption ao tentar criar um livro com isbn duplicado")
    public void shouldNotCreateBookWithDuplicatedIsbn() {
        Book book = BookHelper.createMockValidBook();
        when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(true);

        Throwable exception = catchThrowable(() -> bookService.save(book));

        verify(bookRepository, never()).save(any(Book.class));
        assertThat(exception.getMessage().equals("Isbn duplicado"));

    }

}