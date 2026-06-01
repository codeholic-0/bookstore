package com.deba.bookstore.exception;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(Long id){
        super("Book Not found with id: " + id);
    }
}
