package javaSpring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import javaSpring.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    boolean existsByBookCode(String bookCode);

    // Tìm theo tên (Không đổi)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // 1. Theo Tác giả (Authors)
    // Entity Book có biến 'authors' -> a.id
    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    List<Book> findBooksByAuthorId(@Param("authorId") Long authorId);

    // 2. Theo UserId (Lịch sử mượn)
    // Book -> borrowSlipDetails -> borrowSlip -> reader -> id
    @Query("SELECT DISTINCT b FROM Book b " +
           "JOIN b.borrowSlipDetails bsd " +
           "JOIN bsd.borrowSlip bs " +
           "WHERE bs.reader.id = :userId")
    List<Book> findBooksByUserId(@Param("userId") Long userId);

    // 3. Theo Category và Tags
    // Book -> category -> id
    // Book -> tags -> id
    @Query("SELECT DISTINCT b FROM Book b " +
           "JOIN b.category c " +
           "JOIN b.tags t " +
           "WHERE c.id = :categoryId " +
           "AND t.id IN :tagIds")
    List<Book> findBooksByCategoryAndTags(@Param("categoryId") Long categoryId, 
                                          @Param("tagIds") List<Long> tagIds);

                                          
    // 4. Lấy sách có nội dung ebook (ebookPages không rỗng)
    // Logic: Lấy các cuốn sách (DISTINCT để không trùng) 
    // mà có liên kết (JOIN) với bảng ebookPages
    @Query("SELECT DISTINCT b FROM Book b JOIN b.ebookPages")
    List<Book> findAllBooksWithEbookContent();

    // Tìm tất cả sách chưa bị xóa mềm
       List<Book> findByIsActiveTrue();
}
