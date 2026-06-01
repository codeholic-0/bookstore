package com.deba.bookstore.service;

import com.deba.bookstore.dto.BookResponse;
import com.deba.bookstore.dto.CreateBookRequest;
import com.deba.bookstore.entity.Book;
import com.deba.bookstore.exception.BookNotFoundException;
import com.deba.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private CreateBookRequest createSampleRequest() {
        CreateBookRequest req = new CreateBookRequest();
        req.setTitle("1984");
        req.setAuthor("George Orwell");
        req.setIsbn("9780451524935");
        req.setPrice(new BigDecimal("9.99"));
        req.setPublishedDate(LocalDate.of(1949, 6, 8));
        return req;
    }

    private Book createSampleBook(Long id) {
        Book book = new Book();
        book.setId(id);
        book.setTitle("1984");
        book.setAuthor("George Orwell");
        book.setIsbn("9780451524935");
        book.setPrice(new BigDecimal("9.99"));
        book.setPublishedDate(LocalDate.of(1949, 6, 8));
        return book;
    }

    @Test
    void createBook_ShouldReturnBookResponse() {
        CreateBookRequest req = createSampleRequest();
        Book savedBook = createSampleBook(1L);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookResponse result = bookService.createBook(req);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("1984", result.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void getAllBooks_ShouldReturnList() {
        when(bookRepository.findAll()).thenReturn(List.of(createSampleBook(1L), createSampleBook(2L)));

        List<BookResponse> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() {
        Book book = createSampleBook(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponse result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("1984", result.getTitle());
    }

    @Test
    void getBookById_WhenBookNotFound_ShouldThrowException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(99L));
    }

    @Test
    void updateBook_WhenBookExists_ShouldReturnUpdatedBook() {
        Book existingBook = createSampleBook(1L);
        CreateBookRequest req = createSampleRequest();
        req.setTitle("Animal Farm");
        req.setAuthor("George Orwell");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        BookResponse result = bookService.updateBook(1L, req);

        assertNotNull(result);
        assertEquals("Animal Farm", result.getTitle());
        assertEquals("George Orwell", result.getAuthor());
    }

    @Test
    void updateBook_WhenBookNotFound_ShouldThrowException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(99L, createSampleRequest()));
    }

    @Test
    void deleteBook_WhenBookExists_ShouldDelete() {
        Book book = createSampleBook(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void deleteBook_WhenBookNotFound_ShouldThrowException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(99L));
    }
}
