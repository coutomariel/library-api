package com.coutomariel.libraryapi.api.controller;

import com.coutomariel.libraryapi.api.dto.BookDto;
import com.coutomariel.libraryapi.domain.model.Book;
import com.coutomariel.libraryapi.domain.service.BookServiceImpl;
import com.coutomariel.libraryapi.exception.BussinessException;
import com.coutomariel.libraryapi.exception.ResourceNotFoundException;
import com.coutomariel.libraryapi.helper.BookHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BookControllerTest {

    private static final String BOOK_API = "/api/books";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BookServiceImpl bookService;

    @Test
    @DisplayName("Deve criar um book válido.")
    public void createValidBook() throws Exception {

        BookDto book = BookHelper.createMockValidBookDto();
        Book bookSaved = BookHelper.createMockValidBook();

        given(bookService.save(any(Book.class))).willReturn(bookSaved);

        MockHttpServletRequestBuilder request = post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value(book.getName()))
                .andExpect(jsonPath("author").value(book.getAuthor()))
                .andExpect(jsonPath("isbn").value(book.getIsbn()))
        ;
    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficiente para a criação do livro")
    public void createInvalidBook() throws Exception {

        BookDto book = BookHelper.createMockInvalidBook();

        MockHttpServletRequestBuilder request = post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(3)))
        ;
    }

    @Test
    @DisplayName("Deve lançar uma bussiness exception quando isbn for dupilcado")
    public void shoudNotCreateBookWithDuplicatedIsbn() throws Exception {

        BookDto book = BookHelper.createMockValidBookDto();

        given(bookService.save(any(Book.class)))
                .willThrow(new BussinessException("Isbn duplicado!"));

        MockHttpServletRequestBuilder request = post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0].message").value("Isbn duplicado!"))
        ;

    }

    @Test
    @DisplayName("Deve obter um book atraves de uma busca pelo id")
    public void shoudGetBookById() throws Exception {

        Book book = BookHelper.createMockValidBook();
        given(bookService.findById(1L)).willReturn(book);

        MockHttpServletRequestBuilder request = get(BOOK_API + "/" + book.getId());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value(book.getName()))
                .andExpect(jsonPath("author").value(book.getAuthor()))
        ;

    }

    @Test
    @DisplayName("Deve retornar not foud quando nao encontrar book com id solicitado")
    public void shoudReturnNotFoundWhenBookNotExistsById() throws Exception {
        given(bookService.findById(anyLong())).willThrow(new ResourceNotFoundException("Resource not found exception."));
        MockHttpServletRequestBuilder request = get(BOOK_API + "/1");

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
        ;
    }

    @Test
    @DisplayName("Deve ecluir um book book com id solicitado")
    public void shoudDeleteBookById() throws Exception {
        Book book = BookHelper.createMockValidBook();
        book.setId(1L);
        MockHttpServletRequestBuilder request = delete(BOOK_API + "/1");

        mockMvc.perform(request)
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    @DisplayName("Deve retornar not found quando não encontrar um book com o respectivo id")
    public void shoudReturnNotFoundWhenBookToDeleteNotExistsById() throws Exception {
        doThrow(new ResourceNotFoundException("Resource not found exception.")).when(bookService).delete(anyLong());

        MockHttpServletRequestBuilder request = delete(BOOK_API + "/1");
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0].message").value("Resource not found exception."))
        ;
    }

}
