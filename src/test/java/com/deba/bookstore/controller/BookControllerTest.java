package com.deba.bookstore.controller;

import com.deba.bookstore.dto.BookResponse;
import com.deba.bookstore.dto.CreateBookRequest;
import com.deba.bookstore.exception.BookNotFoundException;
import com.deba.bookstore.service.BookService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new com.deba.bookstore.exception.GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    private BookResponse createSampleResponse(Long id) {
        BookResponse res = new BookResponse();
        res.setId(id);
        res.setTitle("1984");
        res.setAuthor("George Orwell");
        res.setIsbn("9780451524935");
        res.setPrice(new BigDecimal("9.99"));
        res.setPublishedDate(LocalDate.of(1949, 6, 8));
        return res;
    }

    @Test
    void createBook_ShouldReturn201() throws Exception {
        BookResponse response = createSampleResponse(1L);
        when(bookService.createBook(any(CreateBookRequest.class))).thenReturn(response);

        String requestJson = objectMapper.writeValueAsString(createSampleResponse(null));

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("1984"));
    }

    @Test
    void getAllBooks_ShouldReturn200() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(createSampleResponse(1L), createSampleResponse(2L)));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getBookById_ShouldReturn200() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(createSampleResponse(1L));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getBookById_WhenNotFound_ShouldReturn404() throws Exception {
        when(bookService.getBookById(99L)).thenThrow(new BookNotFoundException(99L));

        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBook_ShouldReturn200() throws Exception {
        BookResponse response = createSampleResponse(1L);
        response.setTitle("Animal Farm");
        when(bookService.updateBook(eq(1L), any(CreateBookRequest.class))).thenReturn(response);

        String requestJson = objectMapper.writeValueAsString(createSampleResponse(null));

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Animal Farm"));
    }

    @Test
    void deleteBook_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBook_WhenNotFound_ShouldReturn404() throws Exception {
        doThrow(new BookNotFoundException(99L)).when(bookService).deleteBook(99L);

        mockMvc.perform(delete("/api/books/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBook_WithInvalidInput_ShouldReturn400() throws Exception {
        String invalidJson = """
                {
                    "title": "",
                    "author": "",
                    "isbn": "",
                    "price": -5
                }
                """;

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
