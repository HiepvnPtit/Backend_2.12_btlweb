package javaSpring.service;

import java.util.HashSet;
import java.util.List;

import javaSpring.entity.Author;
import javaSpring.entity.Book;
import javaSpring.entity.Category;
import javaSpring.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javaSpring.dto.request.BookCreationRequest;

import javaSpring.repository.TagRepository;
import javaSpring.repository.AuthorRepository;
import javaSpring.repository.CategoryRepository;
import javaSpring.repository.EbookPageRepository;
import javaSpring.repository.BookRepository;
import javaSpring.repository.BorrowSlipDetailRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import javaSpring.repository.ReadingHistoryRepository;

@Service
public class BookService {
    @Autowired private BookRepository bookRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private TagRepository tagRepository;
    @Autowired private BorrowSlipDetailRepository borrowSlipDetailRepository;
     @Autowired private ReadingHistoryRepository readingHistoryRepository;
      @Autowired private EbookPageRepository ebookPageRepository;
    

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
        // return bookRepository.findAll();
        // Thay vì findAll(), chỉ lấy sách còn Active
        return bookRepository.findByIsActiveTrue();
    }

    // Lấy thông tin sách theo id
    public Book getBook(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    // Lấy sách theo authorId
    public List<Book> getBooksByAuthor(Long authorId) {
        return bookRepository.findBooksByAuthorId(authorId);  // Truy vấn sách theo authorId
    }
    // Lấy sách theo userId
    public List<Book> getBooksByUser(Long userId) {
        return bookRepository.findBooksByUserId(userId);
    }

    // Lấy sách theo CategoryId và TagIds
    public List<Book> getBooksByCategoryAndTags(Long categoryId, List<Long> tagIds) {
    return bookRepository.findBooksByCategoryAndTags(categoryId, tagIds);
}
    // Tìm sách theo tên
    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);  // Tìm sách theo tên không phân biệt hoa thường
    }

    // Cập nhật thông tin sách
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

    // Lấy sách theo phân trang (pageNumber)
    public List<Book> getBooksByPage(int pageNumber) {
        // Quy tắc: 
        // Người dùng nhập trang 1 -> Hệ thống lấy index 0
        // Người dùng nhập trang 2 -> Hệ thống lấy index 1
        int pageIndex = (pageNumber > 0) ? pageNumber - 1 : 0;
        int pageSize = 20; // Cố định 20 sách/trang theo yêu cầu

        // Tạo đối tượng phân trang
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        // Gọi repository
        Page<Book> bookPage = bookRepository.findAll(pageable);

        // Trả về List kết quả
        return bookPage.getContent();
    }


    // Xóa sách
    @Transactional // Bắt buộc có để đảm bảo tính toàn vẹn dữ liệu
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Bước 1: Kiểm tra ràng buộc
        // A. Kiểm tra xem sách giấy đã từng được mượn chưa?
        boolean hasPhysicalHistory = borrowSlipDetailRepository.existsByBookId(bookId);
        
        // B. Kiểm tra xem sách ebook đã từng được đọc chưa? (Dùng ReadingHistory)
        boolean hasDigitalHistory = readingHistoryRepository.existsByBookId(bookId);

        // Bước 2: Quyết định Xóa Mềm hay Xóa Cứng
        if (hasPhysicalHistory || hasDigitalHistory) {
            // === XÓA MỀM (SOFT DELETE) ===
            // Nếu đã có lịch sử dùng, chỉ ẩn sách đi
            book.setIsActive(false);
            bookRepository.save(book);
        } else {
            // === XÓA CỨNG (HARD DELETE) ===
            // Nếu chưa ai dùng, xóa vĩnh viễn khỏi Database
            
            // B2.1: Phải xóa các trang Ebook (EbookPage) của sách này trước
            // (Nếu không xóa page trước, DB sẽ báo lỗi khóa ngoại)
            ebookPageRepository.deleteByBookId(bookId);
            
            // B2.2: Xóa sách
            // Các bảng trung gian (sach_tac_gia, sach_tag) sẽ tự xóa nhờ Cascade của JPA
            bookRepository.delete(book);
        }
    }
}
