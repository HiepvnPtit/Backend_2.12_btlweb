package javaSpring.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import javaSpring.entity.ReadingHistory;
import java.util.List;
import java.util.Optional;

public interface ReadingHistoryRepository extends JpaRepository<ReadingHistory, Long> {
    // Tìm lịch sử đọc gần nhất của user với cuốn sách này (để resume)
    Optional<ReadingHistory> findTopByUserIdAndBookIdOrderByEndTimeDesc(Long userId, Long bookId);
    
    // Lấy toàn bộ lịch sử của 1 user
    List<ReadingHistory> findByUserIdOrderByStartTimeDesc(Long userId);
}
