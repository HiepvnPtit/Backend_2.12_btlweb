package javaSpring.service;

import javaSpring.dto.request.ReadingSessionRequest;
import javaSpring.entity.Book;
import javaSpring.entity.ReadingHistory;
import javaSpring.entity.User;
import javaSpring.repository.BookRepository;
import javaSpring.repository.ReadingHistoryRepository;
import javaSpring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReadingHistoryService {
    @Autowired private ReadingHistoryRepository historyRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;

    // Lưu tiến độ đọc (Gọi hàm này mỗi khi user lật trang hoặc đóng sách)
    public ReadingHistory saveProgress(ReadingSessionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Tìm xem user có phiên đọc nào chưa kết thúc (hoặc vừa mới đọc gần đây) không
        
        // Luôn tạo mới phiên đọc hoặc cập nhật record cuối cùng
        ReadingHistory history = historyRepository.findTopByUserIdAndBookIdOrderByEndTimeDesc(request.getUserId(), request.getBookId())
                .orElse(null);

        // Nếu tìm thấy lịch sử cũ và mới đọc cách đây chưa lâu (ví dụ 1 tiếng), thì update tiếp
        if (history != null && history.getEndTime().plusHours(1).isAfter(LocalDateTime.now())) {
            history.setEndPage(request.getCurrentPage());
            history.setEndTime(LocalDateTime.now());
        } else {
            // Tạo phiên đọc mới
            history = new ReadingHistory();
            history.setUser(user);
            history.setBook(book);
            history.setStartPage(request.getCurrentPage()); // Bắt đầu từ trang hiện tại
            history.setEndPage(request.getCurrentPage());
            history.setStartTime(LocalDateTime.now());
            history.setEndTime(LocalDateTime.now());
            history.setDeviceInfo(request.getDeviceInfo());

        }

        return historyRepository.save(history);
    }

    // Lấy lịch sử đọc của user
    public List<ReadingHistory> getUserHistory(Long userId) {
        return historyRepository.findByUserIdOrderByStartTimeDesc(userId);
    }
    // Lấy lịch sử đọc sách theo bookId
    public List<ReadingHistory> getReadingHistoryByBook(Long bookId) {
        return historyRepository.findByBookId(bookId);
    }
    
}