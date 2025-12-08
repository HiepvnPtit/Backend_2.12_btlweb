package javaSpring.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javaSpring.dto.request.*;
import javaSpring.entity.Book;
import javaSpring.entity.BorrowSlip;
import javaSpring.entity.BorrowSlipDetail;
import javaSpring.entity.User;
import javaSpring.repository.BookRepository;
import javaSpring.repository.BorrowSlipDetailRepository;
import javaSpring.repository.BorrowSlipRepository;
import javaSpring.repository.UserRepository;



@Service
public class BorrowSlipService {

    @Autowired private BorrowSlipRepository borrowSlipRepository;
    @Autowired private BorrowSlipDetailRepository borrowSlipDetailRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;

    // 1. TẠO PHIẾU MƯỢN (Mượn sách)
    @Transactional // Đảm bảo nếu lỗi ở bước nào thì rollback toàn bộ (không trừ kho sai)
    public BorrowSlip createBorrowSlip(BorrowSlipCreationRequest request) {
        // Tìm người đọc
        User reader = userRepository.findById(request.getReaderId())
                .orElseThrow(() -> new RuntimeException("Reader not found"));

        // Tạo phiếu mượn (Header)
        BorrowSlip slip = new BorrowSlip();
        slip.setReader(reader);
        slip.setStatus("BORROWED");
        slip.setNote(request.getNote());
        
        // Auto gen code
        String randomCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        slip.setSlipCode("SLIP-" + randomCode);

        // Lưu phiếu trước để lấy ID
        BorrowSlip savedSlip = borrowSlipRepository.save(slip);
        List<BorrowSlipDetail> details = new ArrayList<>();

        // Xử lý từng cuốn sách được chọn
        for (Long bookId : request.getBookIds()) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book with ID " + bookId + " not found"));

            // Kiểm tra sách còn trong kho không
            if (book.getAvailableQuantity() <= 0) {
                throw new RuntimeException("Book '" + book.getTitle() + "' is out of stock");
            }

            // Trừ số lượng tồn kho
            book.setAvailableQuantity(book.getAvailableQuantity() - 1);
            bookRepository.save(book);

            // Tạo chi tiết phiếu mượn
            BorrowSlipDetail detail = new BorrowSlipDetail();
            detail.setBorrowSlip(savedSlip);
            detail.setBook(book);
            detail.setBorrowDate(LocalDate.now());
            detail.setDueDate(LocalDate.now().plusDays(14)); // Mặc định mượn 2 tuần
            detail.setStatus("BORROWED");

            details.add(borrowSlipDetailRepository.save(detail));
        }

        savedSlip.setDetails(details);
        return savedSlip;
    }

    // 2. TRẢ SÁCH (Xử lý trên từng cuốn sách - Detail)
    @Transactional
    public BorrowSlipDetail returnBook(Long detailId) {
        BorrowSlipDetail detail = borrowSlipDetailRepository.findById(detailId)
                .orElseThrow(() -> new RuntimeException("Borrow detail not found"));

        if ("RETURNED".equals(detail.getStatus())) {
            throw new RuntimeException("This book has already been returned");
        }

        // Cập nhật trạng thái
        detail.setStatus("RETURNED");
        detail.setReturnDate(LocalDate.now());

        // Cộng lại số lượng sách vào kho
        Book book = detail.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);

        return borrowSlipDetailRepository.save(detail);
    }

    // 3. LẤY DANH SÁCH PHIẾU
    public List<BorrowSlip> getAllBorrowSlips() {
        return borrowSlipRepository.findAll();
    }

    // 4. LẤY CHI TIẾT 1 PHIẾU
    public BorrowSlip getBorrowSlip(Long id) {
        return borrowSlipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow slip not found"));
    }
    // Lấy phiếu mượn theo userId
    public List<BorrowSlip> getBorrowSlipsByUser(Long userId) {
        return borrowSlipRepository.findByUserId(userId);  // Lấy phiếu mượn của người dùng
    }

    // Lấy phiếu mượn theo bookId
    public BorrowSlip getBorrowSlipByBook(Long bookId) {
        return borrowSlipRepository.findByBookId(bookId);  // Lấy phiếu mượn của sách
    }

    // Lấy phiếu mượn theo createdAt
    public List<BorrowSlip> getBorrowSlipsByCreatedAt(String dateStr) {
        try {
            // 1. Xử lý chuỗi đầu vào. 
            // Nếu người dùng lỡ gửi cả giờ (vd: "2025-12-02 14:00"), ta chỉ cắt lấy 10 ký tự đầu "2025-12-02"
            String dateOnly = dateStr.length() > 10 ? dateStr.substring(0, 10) : dateStr;

            // 2. Chuyển String thành LocalDate (chỉ ngày)
            LocalDate date = LocalDate.parse(dateOnly); 

            // 3. Tính toán khoảng thời gian trong ngày đó
            LocalDateTime startOfDay = date.atStartOfDay();            // 2025-12-02 00:00:00
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);       // 2025-12-02 23:59:59.999999

            // 4. Gọi Repository tìm trong khoảng start -> end
            return borrowSlipRepository.findByCreatedAtBetween(startOfDay, endOfDay);

        } catch (Exception e) {
            throw new RuntimeException("Định dạng ngày không hợp lệ. Vui lòng dùng định dạng: yyyy-MM-dd (Ví dụ: 2025-12-02)");
        }
    }

    //xóa phiếu mượn theo id phiếu mượn
    public void deleteBorrowSlip(Long id) {
        // 1. Tìm phiếu mượn
        BorrowSlip slip = borrowSlipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow slip not found"));

        // 2. Xử lý hoàn trả số lượng sách vào kho (Inventory Restoration)
        // Duyệt qua từng chi tiết trong phiếu
        if (slip.getDetails() != null) {
            for (BorrowSlipDetail detail : slip.getDetails()) {
                // Chỉ cộng lại kho nếu sách đó CHƯA ĐƯỢC TRẢ (Status != RETURNED)
                // Nếu status là BORROWED hoặc PENDING, nghĩa là sách đang bị trừ kho, cần cộng lại.
                if (!"RETURNED".equalsIgnoreCase(detail.getStatus())) {
                    Book book = detail.getBook();
                    book.setAvailableQuantity(book.getAvailableQuantity() + 1);
                    bookRepository.save(book);
                }
            }
        }

        // 3. Xóa phiếu (Do có CascadeType.ALL ở Entity BorrowSlip, nó sẽ tự xóa luôn các Details)
        borrowSlipRepository.delete(slip);
    }

    // Xóa tất cả phiếu mượn của một user theo userId
    @Transactional
    public void deleteAllByUserId(Long userId) {
        // 1. Kiểm tra User có tồn tại không
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        // 2. Lấy danh sách phiếu mượn của User đó
        List<BorrowSlip> userSlips = borrowSlipRepository.findByReaderId(userId);

        if (userSlips.isEmpty()) {
            throw new RuntimeException("This user has no borrow slips to delete");
        }

        // 3. Xử lý hoàn trả kho sách trước khi xóa
        for (BorrowSlip slip : userSlips) {
            // Duyệt qua từng chi tiết trong phiếu
            for (BorrowSlipDetail detail : slip.getDetails()) {
                // Nếu sách đang ở trạng thái MƯỢN, thì phải cộng lại kho trước khi xóa
                if ("BORROWED".equals(detail.getStatus())) {
                    Book book = detail.getBook();
                    book.setAvailableQuantity(book.getAvailableQuantity() + 1);
                    bookRepository.save(book);
                }
            }
            
            // 4. Xóa phiếu (Cascade sẽ tự xóa các chi tiết kèm theo)
            borrowSlipRepository.delete(slip);
        }
    }
    
    // 5. CẬP NHẬT PHIẾU MƯỢN (Header + Detail)
     @Transactional
    public BorrowSlip updateBorrowSlip(Long id, BorrowSlipUpdateRequest request) {
        // 1. Tìm phiếu mượn cũ
        BorrowSlip existingSlip = borrowSlipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow slip not found"));

        // 2. Cập nhật thông tin cơ bản (Header)
        if (request.getReaderId() != null) {
            User newReader = userRepository.findById(request.getReaderId())
                    .orElseThrow(() -> new RuntimeException("Reader not found"));
            existingSlip.setReader(newReader);
        }
        if (request.getNote() != null) existingSlip.setNote(request.getNote());
        
        // Giả sử update ngày mượn cho toàn bộ sách trong phiếu
        LocalDate newBorrowDate = request.getBorrowDate() != null ? request.getBorrowDate() : LocalDate.now();
        LocalDate newDueDate = request.getDueDate() != null ? request.getDueDate() : newBorrowDate.plusDays(14);

        // 3. XỬ LÝ DANH SÁCH SÁCH (QUAN TRỌNG)
        List<BorrowSlipDetail> currentDetails = existingSlip.getDetails();
        
        // Lấy danh sách ID sách hiện tại trong DB
        List<Long> currentBookIds = currentDetails.stream()
                .map(detail -> detail.getBook().getId())
                .collect(Collectors.toList());

        // Danh sách ID sách mới từ Request
        List<Long> newBookIds = request.getBookIds();

        // 3a. Tìm sách bị loại bỏ (Có trong cũ nhưng không có trong mới)
        // -> Trả lại kho, xóa detail
        List<BorrowSlipDetail> detailsToRemove = currentDetails.stream()
                .filter(detail -> !newBookIds.contains(detail.getBook().getId()))
                .collect(Collectors.toList());

        for (BorrowSlipDetail detail : detailsToRemove) {
            Book book = detail.getBook();
            book.setAvailableQuantity(book.getAvailableQuantity() + 1); // Cộng kho
            bookRepository.save(book);
            borrowSlipDetailRepository.delete(detail); // Xóa dòng chi tiết
            existingSlip.getDetails().remove(detail); // Xóa khỏi list trong memory
        }

        // 3b. Tìm sách thêm mới (Có trong mới nhưng không có trong cũ)
        // -> Trừ kho, tạo detail
        List<Long> booksToAddIds = newBookIds.stream()
                .filter(bookId -> !currentBookIds.contains(bookId))
                .collect(Collectors.toList());

        for (Long bookId : booksToAddIds) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book ID " + bookId + " not found"));

            if (book.getAvailableQuantity() <= 0) {
                throw new RuntimeException("Book '" + book.getTitle() + "' is out of stock");
            }

            book.setAvailableQuantity(book.getAvailableQuantity() - 1); // Trừ kho
            bookRepository.save(book);

            BorrowSlipDetail newDetail = new BorrowSlipDetail();
            newDetail.setBorrowSlip(existingSlip);
            newDetail.setBook(book);
            newDetail.setStatus("BORROWED");
            newDetail.setBorrowDate(newBorrowDate);
            newDetail.setDueDate(newDueDate);
            
            borrowSlipDetailRepository.save(newDetail);
        }

        // 3c. Cập nhật ngày tháng cho các sách CŨ còn giữ lại
        for (BorrowSlipDetail detail : existingSlip.getDetails()) {
             // Chỉ update những cuốn chưa bị xóa
             if (newBookIds.contains(detail.getBook().getId())) {
                 detail.setBorrowDate(newBorrowDate);
                 detail.setDueDate(newDueDate);
                 borrowSlipDetailRepository.save(detail);
             }
        }

        return borrowSlipRepository.save(existingSlip);
    }
}
