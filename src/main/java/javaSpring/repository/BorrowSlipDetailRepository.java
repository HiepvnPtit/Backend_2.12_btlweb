package javaSpring.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javaSpring.entity.BorrowSlipDetail;

@Repository
public interface BorrowSlipDetailRepository extends JpaRepository<BorrowSlipDetail, Long> {

    // 1. Lấy danh sách chi tiết của một phiếu mượn
    // Spring hiểu BorrowSlipId là tìm thuộc tính borrowSlip, sau đó lấy Id
    List<BorrowSlipDetail> findByBorrowSlip_Id(Long borrowSlipId);

    // 2. Tìm theo trạng thái
    List<BorrowSlipDetail> findByStatus(String status);

    // 3. Tìm sách quá hạn (Status = BORROWED và DueDate < date)
    List<BorrowSlipDetail> findByStatusAndDueDateBefore(String status, LocalDate date);

    // 4. Xem lịch sử mượn của một cuốn sách
    List<BorrowSlipDetail> findByBook_Id(Long bookId);
}