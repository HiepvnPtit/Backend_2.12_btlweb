package javaSpring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import javaSpring.entity.EbookPage;
import java.util.List;

public interface EbookPageRepository extends JpaRepository<EbookPage, Long> {
    // Lấy danh sách trang để hiển thị
    List<EbookPage> findByBookIdOrderByPageNumberAsc(Long bookId);
    
    // Xóa tất cả trang của 1 cuốn sách (Dùng khi xóa cứng Book)
    void deleteByBookId(Long bookId);
}