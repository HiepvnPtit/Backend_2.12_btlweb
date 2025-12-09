package javaSpring.controller;

import javaSpring.dto.request.BookCreationRequest;
import javaSpring.dto.response.APIResponse;
import javaSpring.entity.Book;
import javaSpring.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // Tạo sách mới
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") 
    @PostMapping
    public APIResponse<Book> createBook(@RequestBody BookCreationRequest request) {
        APIResponse<Book> response = new APIResponse<>();
        response.setResult(bookService.createBook(request));  // Tạo sách từ service
        return response;
    }

    // Lấy tất cả sách
    @GetMapping
    public APIResponse<List<Book>> getAllBooks() {
        APIResponse<List<Book>> response = new APIResponse<>();
        response.setResult(bookService.getAllBooks());  // Lấy tất cả sách từ service
        return response;
    }

    // Lấy sách theo ID
    @GetMapping("/{id}")
    public APIResponse<Book> getBook(@PathVariable Long id) {
        APIResponse<Book> response = new APIResponse<>();
        response.setResult(bookService.getBook(id));  // Lấy sách theo id từ service
        return response;
    }

    // Lấy sách theo authorId
    @GetMapping("/author/{authorId}")
    public APIResponse<List<Book>> getBooksByAuthor(@PathVariable Long authorId) {
        APIResponse<List<Book>> response = new APIResponse<>();
        response.setResult(bookService.getBooksByAuthor(authorId));  // Lấy sách theo tác giả từ service
        return response;
    }

    // Lấy sách theo userId
    @GetMapping("/user/{userId}")
    @PreAuthorize("@userSecurity.hasUserId(authentication, #userId)")
    public APIResponse<List<Book>> getBooksByUser(@PathVariable Long userId) {
        APIResponse<List<Book>> response = new APIResponse<>();
        response.setResult(bookService.getBooksByUser(userId));  // Lấy sách theo người dùng từ service
        return response;
    }

    // Lấy sách theo CategoryId và tagIds
    @GetMapping("/category/{categoryId}/tags")
    public APIResponse<List<Book>> getBooksByCategoryAndTags(@PathVariable Long categoryId, @RequestParam List<Long> tagIds) {
        APIResponse<List<Book>> response = new APIResponse<>();
        response.setResult(bookService.getBooksByCategoryAndTags(categoryId, tagIds));  // Lấy sách theo thể loại và tag từ service
        return response;
    }
    
    // Tìm sách theo tên
    @GetMapping("/searchByTitle")
    public APIResponse<List<Book>> getBooksByTitle(@RequestParam String title) {
        APIResponse<List<Book>> response = new APIResponse<>();
        response.setResult(bookService.getBooksByTitle(title));  // Tìm sách theo tên từ service
        return response;
    }
    
    // Cập nhật sách theo ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{id}")
    public APIResponse<Book> updateBook(@PathVariable Long id, @RequestBody BookCreationRequest request) {
        APIResponse<Book> response = new APIResponse<>();
        response.setResult(bookService.updateBook(id, request));  // Cập nhật sách từ service
        return response;
    }
    // Xóa sách theo ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") 
    @DeleteMapping("/{id}")
    public APIResponse<Void> deleteBook(@PathVariable Long id) {
        APIResponse<Void> response = new APIResponse<>();
        response.setMessage("Book deleted successfully"); 
        bookService.deleteBook(id);  // Xóa sách theo ID từ service
        return response;
    }

    // Lấy sách theo phân trang
    // URL: http://localhost:8080/books/page/1 (Lấy 20 sách đầu)
    // URL: http://localhost:8080/books/page/2 (Lấy 20 sách tiếp theo)
    @GetMapping("/page/{pageNumber}")
    public APIResponse<List<Book>> getBooksByPage(@PathVariable int pageNumber) {
        APIResponse<List<Book>> response = new APIResponse<>();
        response.setResult(bookService.getBooksByPage(pageNumber));
        return response;
    }

    // Lấy 10 sách mới nhất
    @GetMapping("10books")
    public APIResponse<List<Book>> getTop10Books() {
        APIResponse<List<Book>> response = new APIResponse<>();
        response.setResult(bookService.getTop10NewestBooks());
        return response;
    }
}
