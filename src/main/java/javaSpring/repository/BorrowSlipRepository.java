package javaSpring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javaSpring.entity.BorrowSlip;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BorrowSlipRepository extends JpaRepository<BorrowSlip, Long> {
    boolean existsBySlipCode(String slipCode);
   // 1. Theo UserId
    // Entity BorrowSlip có biến 'reader' -> reader.id
    @Query("SELECT bs FROM BorrowSlip bs WHERE bs.reader.id = :userId")
    List<BorrowSlip> findByUserId(@Param("userId") Long userId);

    // 2. Theo BookId
    // BorrowSlip -> details -> book -> id
    @Query("SELECT DISTINCT bs FROM BorrowSlip bs " +
           "JOIN bs.details bsd " +
           "WHERE bsd.book.id = :bookId")
    BorrowSlip findByBookId(@Param("bookId") Long bookId);

    // 3. Theo ngày tạo (Cần đúng kiểu dữ liệu LocalDateTime)
    @Query("SELECT bs FROM BorrowSlip bs WHERE bs.createdAt = :createdAt")
    List<BorrowSlip> findByCreatedAt(@Param("createdAt") LocalDateTime createdAt);
    List<BorrowSlip> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
