package javaSpring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import javaSpring.entity.EbookPage;

import java.util.List;

public interface EbookPageRepository extends JpaRepository<EbookPage, Long> {
    // Lấy tất cả trang của 1 cuốn sách, sắp xếp theo thứ tự
    List<EbookPage> findByBookIdOrderByPageNumberAsc(Long bookId);
}