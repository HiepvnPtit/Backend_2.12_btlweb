package javaSpring.controller;

import javaSpring.dto.request.BookCreationRequest;
import javaSpring.dto.response.APIResponse;
import javaSpring.entity.Book;
import javaSpring.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired private BookService bookService;

    @PostMapping
    public APIResponse<Book> createBook(@RequestBody BookCreationRequest request) {
        APIResponse<Book> response = new APIResponse<>();
        response.setResult(bookService.createBook(request));
        return response;
    }

    @GetMapping
    public APIResponse<List<Book>> getAllBooks() {
        APIResponse<List<Book>> response = new APIResponse<>();
        response.setResult(bookService.getAllBooks());
        return response;
    }
    
    @GetMapping("/{id}")
    public APIResponse<Book> getBook(@PathVariable Long id) {
        APIResponse<Book> response = new APIResponse<>();
        response.setResult(bookService.getBook(id));
        return response;
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> deleteBook(@PathVariable Long id) {
        APIResponse<Void> response = new APIResponse<>();
        bookService.deleteBook(id);
        return response;
    }

}