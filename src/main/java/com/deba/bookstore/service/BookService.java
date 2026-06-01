package com.deba.bookstore.service;

import org.springframework.stereotype.Service;

import com.deba.bookstore.repository.BookRepository;
import com.deba.bookstore.entity.Book;
import com.deba.bookstore.dto.BookResponse;
import com.deba.bookstore.dto.CreateBookRequest;
import com.deba.bookstore.exception.BookNotFoundException;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponse createBook(CreateBookRequest req) {
        Book book = toEntity(req);
        Book savedBook = bookRepository.save(book);
        return toResponse(savedBook);
    }

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public BookResponse getBookById(Long id) throws BookNotFoundException {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return toResponse(book);
    }

    public BookResponse updateBook(Long id, CreateBookRequest req) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setIsbn(req.getIsbn());
        book.setPrice(req.getPrice());
        book.setPublishedDate(req.getPublishedDate());
        Book updatedBook = bookRepository.save(book);
        return toResponse(updatedBook);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(book);
    }

    private BookResponse toResponse(Book book) {
        BookResponse res = new BookResponse();
        res.setId(book.getId());
        res.setTitle(book.getTitle());
        res.setAuthor(book.getAuthor());
        res.setIsbn(book.getIsbn());
        res.setPrice(book.getPrice());
        res.setPublishedDate(book.getPublishedDate());
        return res;
    }

    private Book toEntity(CreateBookRequest req) {
        Book book = new Book();
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setIsbn(req.getIsbn());
        book.setPrice(req.getPrice());
        book.setPublishedDate(req.getPublishedDate());
        return book;
    }
}
