package com.coutomariel.libraryapi.domain.repository;

import com.coutomariel.libraryapi.domain.model.Book;
import com.coutomariel.libraryapi.helper.BookHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("Deve retornar falso quando não houver correspomdencia na base de dados")
    public void returnFalseWhenNotExistsByIsbn(){
        Boolean verifyByIsbn = bookRepository.existsByIsbn("zxpto");
        assertThat(verifyByIsbn).isFalse();
    }

    @Test
    @DisplayName("Deve retornar true quando não houver correspomdencia na base de dados")
    public void returnTrueWhenNotExistsByIsbn(){
        Book book = BookHelper.createMockValidBookToPersist();
        entityManager.persist(book);

        boolean verifyByIsbn = bookRepository.existsByIsbn(book.getIsbn());

        assertThat(verifyByIsbn).isTrue();
    }

}
