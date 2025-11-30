package javaSpring.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javaSpring.dto.request.BookCreationRequest;
import javaSpring.entity.*;
import javaSpring.repository.*;

@Service
public class BookService {
    @Autowired private BookRepository bookRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private TagRepository tagRepository;

    // Tạo sách mới
    public Book createBook(BookCreationRequest request) {
        if (bookRepository.existsByBookCode(request.getBookCode())) {
            throw new RuntimeException("Book code already exists");
        }

        Book book = new Book();
            book.setTitle(request.getTitle());
            book.setBookCode(request.getBookCode());
            book.setPublishYear(request.getPublishYear());
            book.setPrice(request.getPrice());
            book.setTotalQuantity(request.getTotalQuantity());
            book.setAvailableQuantity(request.getTotalQuantity()); // Ban đầu available = total
            book.setIsbn(request.getIsbn());
            book.setDescription(request.getDescription());


        // 1. Set Category (Bắt buộc)
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        book.setCategory(category);

        // 2. Set Authors (Many-to-Many)
        if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
            List<Author> authors = authorRepository.findAllById(request.getAuthorIds());
            book.setAuthors(new HashSet<>(authors));
        }

        // 3. Set Tags (Many-to-Many)
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(request.getTagIds());
            book.setTags(new HashSet<>(tags));
        }

        return bookRepository.save(book);
    }

    // Lấy danh sách tất cả sách
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Lấy thông tin sách theo id
    public Book getBook(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    // Xóa sách theo id
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book updateBook(Long bookId, BookCreationRequest request) {
        Book book = getBook(bookId); // Tái sử dụng hàm getBook ở dưới để code gọn hơn

        // Cập nhật thông tin sách
        book.setTitle(request.getTitle());
        book.setBookCode(request.getBookCode());
        book.setPublishYear(request.getPublishYear());
        book.setPrice(request.getPrice());

        // Cập nhật lại availableQuantity nếu totalQuantity thay đổi
        if (!book.getTotalQuantity().equals(request.getTotalQuantity())) {
            int quantityDiff = request.getTotalQuantity() - book.getTotalQuantity();
            book.setAvailableQuantity(book.getAvailableQuantity() + quantityDiff);
            book.setTotalQuantity(request.getTotalQuantity());
        }
        
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());

        // Cập nhật Category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        book.setCategory(category);

        // Cập nhật Authors
        if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
            List<Author> authors = authorRepository.findAllById(request.getAuthorIds());
            book.setAuthors(new HashSet<>(authors));
        } else {
            book.setAuthors(new HashSet<>());
        }

        // Cập nhật Tags
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(request.getTagIds());
            book.setTags(new HashSet<>(tags));
        } else {
            book.setTags(new HashSet<>());
        }
        return bookRepository.save(book);
    }
}