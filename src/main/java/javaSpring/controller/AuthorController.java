package javaSpring.controller;

import javaSpring.dto.request.AuthorCreationRequest;
import javaSpring.dto.response.APIResponse;
import javaSpring.entity.Author;
import javaSpring.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    @Autowired 
    private AuthorService authorService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public APIResponse<Author> createAuthor(@RequestBody AuthorCreationRequest request) {
        APIResponse<Author> response = new APIResponse<>();
        response.setResult(authorService.createAuthor(request));
        return response;
    }

    @GetMapping
    public APIResponse<List<Author>> getAllAuthors() {
        APIResponse<List<Author>> response = new APIResponse<>();
        response.setResult(authorService.getAllAuthors());
        return response;
    }

    // lấy tác giả theo tên
    @GetMapping("/authorName/{authorName}")
    public APIResponse<List<Author>> getAuthorsByAuthorName(@PathVariable String authorName) {
        APIResponse<List<Author>> response = new APIResponse<>();
        response.setResult(authorService.getAuthorsByAuthorName(authorName));
        return response;
    }

    @GetMapping("/{authorId}")
    public APIResponse<Author> getAuthor(@PathVariable Long authorId) {
        APIResponse<Author> response = new APIResponse<>();
        response.setResult(authorService.getAuthor(authorId));
        return response;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{authorId}")
    public APIResponse<Author> updateAuthor(@PathVariable Long authorId, @RequestBody AuthorCreationRequest request) {
        APIResponse<Author> response = new APIResponse<>();
        response.setResult(authorService.updateAuthor(authorId, request));
        return response;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") 
    @DeleteMapping("/{authorId}")
    public APIResponse<Void> deleteAuthor(@PathVariable Long authorId) {
        authorService.deleteAuthor(authorId);
        APIResponse<Void> response = new APIResponse<>();
        return response;
    }
}
