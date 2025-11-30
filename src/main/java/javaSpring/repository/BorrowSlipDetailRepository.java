package javaSpring.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javaSpring.entity.BorrowSlipDetail;

@Repository
public interface BorrowSlipDetailRepository extends JpaRepository<BorrowSlipDetail, Long> {

    // 1. Lấy danh sách chi tiết của một phiếu mượn (Dựa vào ID phiếu cha)
    List<BorrowSlipDetail> findByBorrowSlipId(Long borrowSlipId);

    // 2. Tìm tất cả các cuốn sách đang ở trạng thái cụ thể (Ví dụ: Tìm tất cả sách đang "BORROWED")
    List<BorrowSlipDetail> findByStatus(String status);

    // 3. Tìm các cuốn sách đã quá hạn (Status = BORROWED và Hạn trả < Ngày hiện tại)
    // Dùng để tính tiền phạt hoặc gửi mail nhắc nhở
    List<BorrowSlipDetail> findByStatusAndDueDateBefore(String status, LocalDate date);

    // 4. Xem lịch sử mượn của một cuốn sách cụ thể (Ai đã từng mượn cuốn này?)
    List<BorrowSlipDetail> findByBookId(Long bookId);
}